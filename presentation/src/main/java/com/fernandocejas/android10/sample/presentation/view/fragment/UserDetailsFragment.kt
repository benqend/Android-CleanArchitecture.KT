/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fernandocejas.android10.sample.presentation.R
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent
import com.fernandocejas.android10.sample.presentation.model.UserModel
import com.fernandocejas.android10.sample.presentation.presenter.UserDetailsPresenter
import com.fernandocejas.android10.sample.presentation.view.UserDetailsView
import com.fernandocejas.android10.sample.presentation.view.component.AutoLoadImageView
import javax.inject.Inject

/**
 * Fragment that shows details of a certain user.
 */
class UserDetailsFragment : BaseFragment(), UserDetailsView {

    @Inject
    lateinit var userDetailsPresenter: UserDetailsPresenter

    @BindView(R.id.iv_cover) lateinit var iv_cover: AutoLoadImageView
    @BindView(R.id.tv_fullname) lateinit var tv_fullname: TextView
    @BindView(R.id.tv_email) lateinit var tv_email: TextView
    @BindView(R.id.tv_followers) lateinit var tv_followers: TextView
    @BindView(R.id.tv_description) lateinit var tv_description: TextView
    @BindView(R.id.rl_progress) lateinit var rl_progress: RelativeLayout
    @BindView(R.id.rl_retry) lateinit var rl_retry: RelativeLayout
    @BindView(R.id.bt_retry) lateinit var bt_retry: Button

    init {
        retainInstance = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getComponent(UserComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false)
        ButterKnife.bind(this, fragmentView)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.userDetailsPresenter.setView(this)
        if (savedInstanceState == null) {
            this.loadUserDetails()
        }
    }

    override fun onResume() {
        super.onResume()
        this.userDetailsPresenter.resume()
    }

    override fun onPause() {
        super.onPause()
        this.userDetailsPresenter.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.userDetailsPresenter.destroy()
    }

    override fun renderUser(user: UserModel) {
        if (user != null) {
            this.iv_cover.setImageUrl(user.coverUrl)
            this.tv_fullname.text = user.fullName
            this.tv_email.text = user.email
            this.tv_followers.text = user.followers.toString()
            this.tv_description.text = user.description
        }
    }

    override fun showLoading() {
        this.rl_progress.visibility = View.VISIBLE
        this.activity.setProgressBarIndeterminateVisibility(true)
    }

    override fun hideLoading() {
        this.rl_progress.visibility = View.GONE
        this.activity.setProgressBarIndeterminateVisibility(false)
    }

    override fun showRetry() {
        this.rl_retry.visibility = View.VISIBLE
    }

    override fun hideRetry() {
        this.rl_retry.visibility = View.GONE
    }

    override fun showError(message: String) {
        this.showToastMessage(message)
    }

    override fun context(): Context {
        return activity.applicationContext
    }

    /**
     * Loads all users.
     */
    private fun loadUserDetails() {
        if (this.userDetailsPresenter != null) {
            this.userDetailsPresenter.initialize()
        }
    }

    @OnClick(R.id.bt_retry)
    internal fun onButtonRetryClick() {
        this@UserDetailsFragment.loadUserDetails()
    }
}
