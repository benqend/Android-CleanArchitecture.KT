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
package com.fernandocejas.android10.sample.data.entity.mapper

import com.fernandocejas.android10.sample.data.entity.UserEntity
import com.fernandocejas.android10.sample.domain.User
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mapper class used to transform [UserEntity] (in the data layer) to [User] in the
 * domain layer.
 */
@Singleton
class UserEntityDataMapper
@Inject
constructor() {

    /**
     * Transform a [UserEntity] into an [User].

     * @param userEntity Object to be transformed.
     * *
     * @return [User] if valid [UserEntity] otherwise null.
     */
    fun transform(userEntity: UserEntity?): User? {
        var user: User? = null
        if (userEntity != null) {
            user = User(userEntity.userId)
            user.coverUrl = userEntity.coverUrl
            user.fullName = userEntity.fullname
            user.description = userEntity.description
            user.followers = userEntity.followers
            user.email = userEntity.email
        }

        return user
    }

    /**
     * Transform a List of [UserEntity] into a Collection of [User].

     * @param userEntityCollection Object Collection to be transformed.
     * *
     * @return [User] if valid [UserEntity] otherwise null.
     */
    fun transform(userEntityCollection: Collection<UserEntity>): List<User> {
        val userList = ArrayList<User>(20)
        var user: User?
        for (userEntity in userEntityCollection) {
            user = transform(userEntity)
            if (user != null) {
                userList.add(user)
            }
        }

        return userList
    }
}
