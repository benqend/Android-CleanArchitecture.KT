/**
 * Copyright (C) 2014 android10.org. All rights reserved.

 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment

import android.app.Fragment
import android.widget.Toast
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent

/**
 * Base [android.app.Fragment] class for every fragment in this application.
 */
abstract class BaseFragment : Fragment() {
    /**
     * Shows a [android.widget.Toast] message.

     * @param message An string representing a message to be shown.
     */
    protected fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).component)
    }
}
