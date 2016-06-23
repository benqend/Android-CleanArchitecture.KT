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

import com.fernandocejas.android10.sample.data.cache.UserCache
import com.fernandocejas.android10.sample.data.entity.UserEntity
import rx.Observable

/**
 * [UserDataStore] implementation based on file system data store.
 */
class DiskUserDataStore
/**
 * Construct a [UserDataStore] based file system data store.

 * @param userCache A [UserCache] to cache data retrieved from the api.
 */
(private val userCache: UserCache) : UserDataStore {

    override fun userEntityList(): Observable<List<UserEntity>> {
        //TODO: implement simple cache for storing/retrieving collections of users.
        throw UnsupportedOperationException("Operation is not available!!!")
    }

    override fun userEntityDetails(userId: Int): Observable<UserEntity> {
        return this.userCache.get(userId)
    }
}
