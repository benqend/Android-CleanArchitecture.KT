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
package com.fernandocejas.android10.sample.presentation.presenter

import com.fernandocejas.android10.sample.domain.User
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle
import com.fernandocejas.android10.sample.domain.interactor.DefaultSubscriber
import com.fernandocejas.android10.sample.domain.interactor.UseCase
import com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity
import com.fernandocejas.android10.sample.presentation.mapper.UserModelDataMapper
import com.fernandocejas.android10.sample.presentation.model.UserModel
import com.fernandocejas.android10.sample.presentation.view.UserListView
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
class UserListPresenter
@Inject
constructor(@Named("userList") private val getUserListUseCase: UseCase,
            private val userModelDataMapper: UserModelDataMapper) : Presenter {

    private var viewListView: UserListView? = null

    fun setView(view: UserListView) {
        this.viewListView = view
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        this.getUserListUseCase.unsubscribe()
        this.viewListView = null
    }

    /**
     * Initializes the presenter by start retrieving the user list.
     */
    fun initialize() {
        this.loadUserList()
    }

    /**
     * Loads all users.
     */
    private fun loadUserList() {
        this.hideViewRetry()
        this.showViewLoading()
        this.getUserList()
    }

    fun onUserClicked(userModel: UserModel) {
        this.viewListView!!.viewUser(userModel)
    }

    private fun showViewLoading() {
        this.viewListView!!.showLoading()
    }

    private fun hideViewLoading() {
        this.viewListView!!.hideLoading()
    }

    private fun showViewRetry() {
        this.viewListView!!.showRetry()
    }

    private fun hideViewRetry() {
        this.viewListView!!.hideRetry()
    }

    private fun showErrorMessage(errorBundle: ErrorBundle) {
        val errorMessage = ErrorMessageFactory.create(this.viewListView!!.context(),
                errorBundle.exception!!)
        this.viewListView!!.showError(errorMessage)
    }

    private fun showUsersCollectionInView(usersCollection: Collection<User>) {
        val userModelsCollection = this.userModelDataMapper.transform(usersCollection)
        this.viewListView!!.renderUserList(userModelsCollection)
    }

    private fun getUserList() {
        this.getUserListUseCase.execute(UserListSubscriber() as Subscriber<Any>)
    }

    private inner class UserListSubscriber : DefaultSubscriber<List<User>>() {

        override fun onCompleted() {
            this@UserListPresenter.hideViewLoading()
        }

        override fun onError(e: Throwable) {
            this@UserListPresenter.hideViewLoading()
            this@UserListPresenter.showErrorMessage(DefaultErrorBundle(e as Exception))
            this@UserListPresenter.showViewRetry()
        }

        override fun onNext(users: List<User>) {
            this@UserListPresenter.showUsersCollectionInView(users)
        }
    }
}
