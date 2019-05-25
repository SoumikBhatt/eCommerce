package com.soumik.e_commerce.activities.admin_activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.soumik.e_commerce.R
import com.soumik.e_commerce.utils.showToast

class AdminPanelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        showToast(applicationContext,"This is Admin Panel")
    }
}
