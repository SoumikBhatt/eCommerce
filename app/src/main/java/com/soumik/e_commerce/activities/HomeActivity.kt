package com.soumik.e_commerce.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.soumik.e_commerce.R
import com.soumik.e_commerce.data.DataHandling
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        DataHandling.getPaperContext(applicationContext)

        btn_logout.setOnClickListener{

            DataHandling.clearUserData()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}
