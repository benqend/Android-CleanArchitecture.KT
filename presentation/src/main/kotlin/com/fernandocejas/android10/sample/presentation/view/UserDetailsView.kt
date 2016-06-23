/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view

import com.fernandocejas.android10.sample.presentation.model.UserModel

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a user profile.
 */
interface UserDetailsView : LoadDataView {
    /**
     * Render a user in the UI.

     * @param user The [UserModel] that will be shown.
     */
    fun renderUser(user: UserModel)
}
