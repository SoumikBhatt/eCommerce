package com.soumik.e_commerce.interfaces

import android.view.View

interface ItemClickListener {
    fun onItemClick(view: View,position:Int,isLongClick:Boolean)
}