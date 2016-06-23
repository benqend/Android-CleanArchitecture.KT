/**
 * Copyright (C) 2014 android10.org. All rights reserved.

 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fernandocejas.android10.sample.presentation.R
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent
import com.fernandocejas.android10.sample.presentation.model.UserModel
import com.fernandocejas.android10.sample.presentation.presenter.UserListPresenter
import com.fernandocejas.android10.sample.presentation.view.UserListView
import com.fernandocejas.android10.sample.presentation.view.adapter.UsersAdapter
import com.fernandocejas.android10.sample.presentation.view.adapter.UsersLayoutManager
import javax.inject.Inject

/**
 * Fragment that shows a list of Users.
 */
class UserListFragment : BaseFragment(), UserListView {

    /**
     * Interface for listening user list events.
     */
    interface UserListListener {
        fun onUserClicked(userModel: UserModel)
    }

    @Inject lateinit var userListPresenter: UserListPresenter
    @Inject lateinit var usersAdapter: UsersAdapter

    @BindView(R.id.rv_users) lateinit var rv_users: RecyclerView
    @BindView(R.id.rl_progress) lateinit var rl_progress: RelativeLayout
    @BindView(R.id.rl_retry) lateinit var rl_retry: RelativeLayout
    @BindView(R.id.bt_retry) lateinit var bt_retry: Button

    private var userListListener: UserListListener? = null

    init {
        retainInstance = true
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is UserListListener) {
            this.userListListener = activity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getComponent(UserComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_user_list, container, false)
        ButterKnife.bind(this, fragmentView)
        setupRecyclerView()
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.userListPresenter!!.setView(this)
        if (savedInstanceState == null) {
            this.loadUserList()
        }
    }

    override fun onResume() {
        super.onResume()
        this.userListPresenter!!.resume()
    }

    override fun onPause() {
        super.onPause()
        this.userListPresenter!!.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rv_users!!.adapter = null
    }

    override fun onDestroy() {
        super.onDestroy()
        this.userListPresenter!!.destroy()
    }

    override fun onDetach() {
        super.onDetach()
        this.userListListener = null
    }

    override fun showLoading() {
        this.rl_progress!!.visibility = View.VISIBLE
        this.activity.setProgressBarIndeterminateVisibility(true)
    }

    override fun hideLoading() {
        this.rl_progress!!.visibility = View.GONE
        this.activity.setProgressBarIndeterminateVisibility(false)
    }

    override fun showRetry() {
        this.rl_retry!!.visibility = View.VISIBLE
    }

    override fun hideRetry() {
        this.rl_retry!!.visibility = View.GONE
    }

    override fun renderUserList(userModelCollection: Collection<UserModel>) {
        if (userModelCollection != null) {
            this.usersAdapter!!.setUsersCollection(userModelCollection)
        }
    }

    override fun viewUser(userModel: UserModel) {
        if (this.userListListener != null) {
            this.userListListener!!.onUserClicked(userModel)
        }
    }

    override fun showError(message: String) {
        this.showToastMessage(message)
    }

    override fun context(): Context {
        return this.activity.applicationContext
    }

    private fun setupRecyclerView() {
        this.usersAdapter!!.setOnItemClickListener(onItemClickListener)
        this.rv_users!!.layoutManager = UsersLayoutManager(context())
        this.rv_users!!.adapter = usersAdapter
    }

    /**
     * Loads all users.
     */
    private fun loadUserList() {
        this.userListPresenter!!.initialize()
    }

    @OnClick(R.id.bt_retry) internal fun onButtonRetryClick() {
        this@UserListFragment.loadUserList()
    }

    private val onItemClickListener = object : UsersAdapter.OnItemClickListener {
        override fun onUserItemClicked(userModel: UserModel) {
            if (this@UserListFragment.userListPresenter != null && userModel != null) {
                this@UserListFragment.userListPresenter!!.onUserClicked(userModel)
            }
        }
    }
}
