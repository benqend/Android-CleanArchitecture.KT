/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.fernandocejas.android10.sample.presentation.R
import com.fernandocejas.android10.sample.presentation.model.UserModel
import javax.inject.Inject

/**
 * Adaptar that manages a collection of [UserModel].
 */
class UsersAdapter
@Inject
constructor(context: Context) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    interface OnItemClickListener {
        fun onUserItemClicked(userModel: UserModel)
    }

    private var usersCollection: List<UserModel>? = null
    private val layoutInflater: LayoutInflater

    private var onItemClickListener: OnItemClickListener? = null

    init {
        this.layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.usersCollection = emptyList<UserModel>()
    }

    override fun getItemCount(): Int {
        return if (this.usersCollection != null) this.usersCollection!!.size else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = this.layoutInflater.inflate(R.layout.row_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userModel = this.usersCollection!![position]
        holder.textViewTitle!!.text = userModel.fullName
        holder.itemView.setOnClickListener {
            if (this@UsersAdapter.onItemClickListener != null) {
                this@UsersAdapter.onItemClickListener!!.onUserItemClicked(userModel)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setUsersCollection(usersCollection: Collection<UserModel>) {
        this.validateUsersCollection(usersCollection)
        this.usersCollection = usersCollection as List<UserModel>
        this.notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    private fun validateUsersCollection(usersCollection: Collection<UserModel>?) {
        if (usersCollection == null) {
            throw IllegalArgumentException("The list cannot be null")
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.title)
        lateinit var textViewTitle: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

    }
}
