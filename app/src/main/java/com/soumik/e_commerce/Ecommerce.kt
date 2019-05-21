package com.soumik.e_commerce

import android.app.Application
import io.paperdb.Paper

class Ecommerce : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(applicationContext)
    }
}