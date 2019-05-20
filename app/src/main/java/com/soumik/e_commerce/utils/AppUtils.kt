package com.soumik.e_commerce.utils

import android.content.Context
import android.widget.Toast
import com.kaopiz.kprogresshud.KProgressHUD

fun showToast(context: Context,text:String){

    Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
}

private var kProgressHUD: KProgressHUD? = null

fun showProgressBar(context: Context,label:String,detailsLable:String) {
    kProgressHUD = KProgressHUD.create(context)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel(label)
        .setDetailsLabel(detailsLable)
        .setCancellable(false)
        .setAnimationSpeed(1)
        .setDimAmount(0.5f)

    kProgressHUD?.show()
}

fun hideProgressBar() {
    if (kProgressHUD != null)
        kProgressHUD?.dismiss()
}