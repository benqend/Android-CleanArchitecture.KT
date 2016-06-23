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
package com.fernandocejas.android10.sample.data.repository

import com.fernandocejas.android10.sample.data.entity.UserEntity
import com.fernandocejas.android10.sample.data.entity.mapper.UserEntityDataMapper
import com.fernandocejas.android10.sample.data.repository.datasource.UserDataStore
import com.fernandocejas.android10.sample.data.repository.datasource.UserDataStoreFactory
import com.fernandocejas.android10.sample.domain.User
import com.fernandocejas.android10.sample.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton
import rx.Observable
import rx.functions.Func1

/**
 * [UserRepository] for retrieving user data.
 */
@Singleton
class UserDataRepository
/**
 * Constructs a [UserRepository].

 * @param dataStoreFactory A factory to construct different data source implementations.
 * *
 * @param userEntityDataMapper [UserEntityDataMapper].
 */
@Inject
constructor(private val userDataStoreFactory: UserDataStoreFactory,
            private val userEntityDataMapper: UserEntityDataMapper) : UserRepository {

    @SuppressWarnings("Convert2MethodRef")
    override fun users(): Observable<List<User>> {
        //we always get all users from the cloud
        val userDataStore = this.userDataStoreFactory.createCloudDataStore()
        return userDataStore.userEntityList()
                .map<List<User>>(Func1 {userEntities -> this.userEntityDataMapper.transform(userEntities)   })
    }

    @SuppressWarnings("Convert2MethodRef")
    override fun user(userId: Int): Observable<User> {
        val userDataStore = this.userDataStoreFactory.create(userId)
        return userDataStore.userEntityDetails(userId)
                .map<User>(Func1{ userEntity -> this.userEntityDataMapper.transform(userEntity) })
    }
}
