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
package com.fernandocejas.android10.sample.presentation.mapper

import com.fernandocejas.android10.sample.domain.User
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity
import com.fernandocejas.android10.sample.presentation.model.UserModel
import java.util.ArrayList
import javax.inject.Inject

/**
 * Mapper class used to transform [User] (in the domain layer) to [UserModel] in the
 * presentation layer.
 */
@PerActivity
class UserModelDataMapper
@Inject
constructor() {

    /**
     * Transform a [User] into an [UserModel].

     * @param user Object to be transformed.
     * *
     * @return [UserModel].
     */
    fun transform(user: User?): UserModel {
        if (user == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }
        val userModel = UserModel(user.userId)
        userModel.coverUrl = user.coverUrl
        userModel.fullName = user.fullName
        userModel.email = user.email
        userModel.description = user.description
        userModel.followers = user.followers

        return userModel
    }

    /**
     * Transform a Collection of [User] into a Collection of [UserModel].

     * @param usersCollection Objects to be transformed.
     * *
     * @return List of [UserModel].
     */
    fun transform(usersCollection: Collection<User>?): Collection<UserModel> {
        val userModelsCollection: MutableCollection<UserModel>
        userModelsCollection = ArrayList<UserModel>()
        if (usersCollection != null && !usersCollection.isEmpty()) {
            for (user in usersCollection) {
                userModelsCollection.add(transform(user))
            }
        }

        return userModelsCollection
    }
}
