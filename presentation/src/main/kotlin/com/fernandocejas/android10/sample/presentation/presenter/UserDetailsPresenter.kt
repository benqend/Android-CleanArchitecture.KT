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
import com.fernandocejas.android10.sample.presentation.view.UserDetailsView
import com.fernandocejas.frodo.annotation.RxLogSubscriber
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * [Presenter] that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
class UserDetailsPresenter
@Inject
constructor(@Named("userDetails") private val getUserDetailsUseCase: UseCase,
            private val userModelDataMapper: UserModelDataMapper) : Presenter {

    private var viewDetailsView: UserDetailsView? = null

    fun setView(view: UserDetailsView) {
        this.viewDetailsView = view
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        this.getUserDetailsUseCase.unsubscribe()
        this.viewDetailsView = null
    }

    /**
     * Initializes the presenter by start retrieving user details.
     */
    fun initialize() {
        this.loadUserDetails()
    }

    /**
     * Loads user details.
     */
    private fun loadUserDetails() {
        this.hideViewRetry()
        this.showViewLoading()
        this.getUserDetails()
    }

    private fun showViewLoading() {
        this.viewDetailsView!!.showLoading()
    }

    private fun hideViewLoading() {
        this.viewDetailsView!!.hideLoading()
    }

    private fun showViewRetry() {
        this.viewDetailsView!!.showRetry()
    }

    private fun hideViewRetry() {
        this.viewDetailsView!!.hideRetry()
    }

    private fun showErrorMessage(errorBundle: ErrorBundle) {
        val errorMessage = ErrorMessageFactory.create(this.viewDetailsView!!.context(),
                errorBundle.exception!!)
        this.viewDetailsView!!.showError(errorMessage)
    }

    private fun showUserDetailsInView(user: User) {
        val userModel = this.userModelDataMapper.transform(user)
        this.viewDetailsView!!.renderUser(userModel)
    }

    private fun getUserDetails() {
        this.getUserDetailsUseCase.execute(UserDetailsSubscriber() as Subscriber<Any>)
    }

    @RxLogSubscriber
    private inner class UserDetailsSubscriber : DefaultSubscriber<User>() {

        override fun onCompleted() {
            this@UserDetailsPresenter.hideViewLoading()
        }

        override fun onError(e: Throwable) {
            this@UserDetailsPresenter.hideViewLoading()
            this@UserDetailsPresenter.showErrorMessage(DefaultErrorBundle(e as Exception))
            this@UserDetailsPresenter.showViewRetry()
        }

        override fun onNext(user: User) {
            this@UserDetailsPresenter.showUserDetailsInView(user)
        }
    }
}
