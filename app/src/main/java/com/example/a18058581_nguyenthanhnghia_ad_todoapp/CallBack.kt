package com.example.a18058581_nguyenthanhnghia_ad_todoapp

import androidx.recyclerview.widget.DiffUtil

open class CallBack(
    private val oldString:ArrayList<User>,
    private val newString:ArrayList<User>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldString.size
    }

    override fun getNewListSize(): Int {
        return newString.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldString[oldItemPosition]==newString[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldString[oldItemPosition]==newString[newItemPosition]
    }
}