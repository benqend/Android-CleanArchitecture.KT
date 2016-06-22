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
import com.fernandocejas.android10.sample.presentation.internal.di.modules.UserModule
import com.fernandocejas.android10.sample.presentation.view.fragment.UserDetailsFragment

/**
 * Activity that shows details of a certain user.
 */
class UserDetailsActivity : BaseActivity(), HasComponent<UserComponent> {

    private var userId: Int = 0
    override lateinit var component: UserComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
        setContentView(R.layout.activity_layout)

        this.initializeActivity(savedInstanceState)
        this.initializeInjector()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(INSTANCE_STATE_PARAM_USER_ID, this.userId)
        super.onSaveInstanceState(outState)
    }

    /**
     * Initializes this activity.
     */
    private fun initializeActivity(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            this.userId = intent.getIntExtra(INTENT_EXTRA_PARAM_USER_ID, -1)
            addFragment(R.id.fragmentContainer, UserDetailsFragment())
        } else {
            this.userId = savedInstanceState.getInt(INSTANCE_STATE_PARAM_USER_ID)
        }
    }

    private fun initializeInjector() {
        this.component = DaggerUserComponent.builder().applicationComponent(applicationComponent).activityModule(activityModule).userModule(UserModule(this.userId)).build()
    }

    companion object {

        private val INTENT_EXTRA_PARAM_USER_ID = "org.android10.INTENT_PARAM_USER_ID"
        private val INSTANCE_STATE_PARAM_USER_ID = "org.android10.STATE_PARAM_USER_ID"
        @JvmStatic
        fun getCallingIntent(context: Context, userId: Int): Intent {
            val callingIntent = Intent(context, UserDetailsActivity::class.java)
            callingIntent.putExtra(INTENT_EXTRA_PARAM_USER_ID, userId)
            return callingIntent
        }
    }
}
