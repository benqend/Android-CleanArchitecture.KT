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
package com.fernandocejas.android10.sample.data.repository.datasource

import android.content.Context
import com.fernandocejas.android10.sample.data.cache.UserCache
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityJsonMapper
import com.fernandocejas.android10.sample.data.net.RestApi
import com.fernandocejas.android10.sample.data.net.RestApiImpl
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Factory that creates different implementations of [UserDataStore].
 */
@Singleton
class UserDataStoreFactory
@Inject
constructor(context: Context, private val userCache: UserCache) {

    private val context: Context

    init {
        this.context = context.applicationContext
    }

    /**
     * Create [UserDataStore] from a user id.
     */
    fun create(userId: Int): UserDataStore {
        val userDataStore: UserDataStore

        if (!this.userCache.isExpired() && this.userCache.isCached(userId)) {
            userDataStore = DiskUserDataStore(this.userCache)
        } else {
            userDataStore = createCloudDataStore()
        }

        return userDataStore
    }

    /**
     * Create [UserDataStore] to retrieve data from the Cloud.
     */
    fun createCloudDataStore(): UserDataStore {
        val userEntityJsonMapper = UserEntityJsonMapper()
        val restApi = RestApiImpl(this.context, userEntityJsonMapper)

        return CloudUserDataStore(restApi, this.userCache)
    }
}
