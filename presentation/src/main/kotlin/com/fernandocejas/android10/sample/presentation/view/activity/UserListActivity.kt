/**
 * Copyright (C) 2014 android10.org. All rights reserved.

 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.fernandocejas.android10.sample.presentation.R
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerUserComponent
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent
import com.fernandocejas.android10.sample.presentation.model.UserModel
import com.fernandocejas.android10.sample.presentation.view.fragment.UserListFragment

/**
 * Activity that shows a list of Users.
 */
class UserListActivity : BaseActivity(), HasComponent<UserComponent>, UserListFragment.UserListListener {

    override lateinit var component: UserComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
        setContentView(R.layout.activity_layout)

        this.initializeInjector()
        if (savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, UserListFragment())
        }
    }

    private fun initializeInjector() {
        this.component = DaggerUserComponent.builder().applicationComponent(applicationComponent).activityModule(activityModule).build()
    }

    override fun onUserClicked(userModel: UserModel) {
        this.navigator.navigateToUserDetails(this, userModel.userId)
    }

    companion object {
        @JvmStatic
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, UserListActivity::class.java)
        }
    }
}
