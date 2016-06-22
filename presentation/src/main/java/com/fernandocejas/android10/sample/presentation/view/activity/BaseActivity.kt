package com.fernandocejas.android10.sample.presentation.view.activity

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import com.fernandocejas.android10.sample.presentation.AndroidApplication
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule
import com.fernandocejas.android10.sample.presentation.navigation.Navigator
import javax.inject.Inject

/**
 * Base [android.app.Activity] class for every Activity in this application.
 */
abstract class BaseActivity : Activity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.applicationComponent?.inject(this)
    }

    /**
     * Adds a [Fragment] to this activity's layout.

     * @param containerViewId The container view to where add the fragment.
     * *
     * @param fragment The fragment to be added.
     */
    protected fun addFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = this.fragmentManager.beginTransaction()
        fragmentTransaction.add(containerViewId, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Get the Main Application component for dependency injection.

     * @return [com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent]
     */
    protected val applicationComponent: ApplicationComponent?
        get() = (application as AndroidApplication).applicationComponent

    /**
     * Get an Activity module for dependency injection.

     * @return [com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule]
     */
    protected val activityModule: ActivityModule
        get() = ActivityModule(this)
}
