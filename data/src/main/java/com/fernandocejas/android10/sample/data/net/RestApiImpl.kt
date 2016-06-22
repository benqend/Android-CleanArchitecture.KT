/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.net

import android.content.Context
import android.net.ConnectivityManager
import com.fernandocejas.android10.sample.data.entity.UserEntity
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityJsonMapper
import com.fernandocejas.android10.sample.data.exception.NetworkConnectionException
import com.fernandocejas.frodo.annotation.RxLogObservable
import java.net.MalformedURLException
import rx.Observable

/**
 * [RestApi] implementation for retrieving data from the network.
 */
class RestApiImpl
/**
 * Constructor of the class

 * @param context [android.content.Context].
 * *
 * @param userEntityJsonMapper [UserEntityJsonMapper].
 */
(context: Context?, private val userEntityJsonMapper: UserEntityJsonMapper) : RestApi {

    private val context: Context

    init {
        if (context == null || userEntityJsonMapper == null) {
            throw IllegalArgumentException("The constructor parameters cannot be null!!!")
        }
        this.context = context.applicationContext
    }

    @RxLogObservable
    override fun userEntityList(): Observable<List<UserEntity>> {
        return Observable.create<List<UserEntity>> { subscriber ->
            if (isThereInternetConnection) {
                try {
                    val responseUserEntities = userEntitiesFromApi
                    if (responseUserEntities != null) {
                        subscriber.onNext(userEntityJsonMapper.transformUserEntityCollection(
                                responseUserEntities))
                        subscriber.onCompleted()
                    } else {
                        subscriber.onError(NetworkConnectionException())
                    }
                } catch (e: Exception) {
                    subscriber.onError(NetworkConnectionException(e))
                }

            } else {
                subscriber.onError(NetworkConnectionException())
            }
        }
    }

    @RxLogObservable
    override fun userEntityById(userId: Int): Observable<UserEntity> {
        return Observable.create<UserEntity> { subscriber ->
            if (isThereInternetConnection) {
                try {
                    val responseUserDetails = getUserDetailsFromApi(userId)
                    if (responseUserDetails != null) {
                        subscriber.onNext(userEntityJsonMapper.transformUserEntity(responseUserDetails))
                        subscriber.onCompleted()
                    } else {
                        subscriber.onError(NetworkConnectionException())
                    }
                } catch (e: Exception) {
                    subscriber.onError(NetworkConnectionException(e))
                }

            } else {
                subscriber.onError(NetworkConnectionException())
            }
        }
    }

    private val userEntitiesFromApi: String?
        @Throws(MalformedURLException::class)
        get() = ApiConnection.createGET(RestApi.API_URL_GET_USER_LIST).requestSyncCall()

    @Throws(MalformedURLException::class)
    private fun getUserDetailsFromApi(userId: Int): String? {
        val apiUrl = RestApi.API_URL_GET_USER_DETAILS + userId + ".json"
        return ApiConnection.createGET(apiUrl).requestSyncCall()
    }

    /**
     * Checks if the device has any active internet connection.

     * @return true device with internet connection, otherwise false.
     */
    private val isThereInternetConnection: Boolean
        get() {
            val isConnected: Boolean

            val connectivityManager = this.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting

            return isConnected
        }
}
