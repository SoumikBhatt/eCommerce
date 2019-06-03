package com.soumik.e_commerce.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.soumik.e_commerce.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tv_close.setOnClickListener{
            onBackPressed()
        }
    }
}
