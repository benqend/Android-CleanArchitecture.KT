package com.fernandocejas.android10.sample.presentation.view.activity

import android.os.Bundle
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fernandocejas.android10.sample.presentation.R

/**
 * Main application screen. This is the app entry point.
 */
class MainActivity : BaseActivity() {

    @BindView(R.id.btn_LoadData)
    lateinit var btn_LoadData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
    }

    /**
     * Goes to the user list screen.
     */
    @OnClick(R.id.btn_LoadData)
    internal fun navigateToUserList() {
        this.navigator.navigateToUserList(this)
    }
}
