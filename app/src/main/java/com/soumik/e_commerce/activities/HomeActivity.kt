package com.soumik.e_commerce.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.soumik.e_commerce.R
import com.soumik.e_commerce.data.DataHandling
import com.soumik.e_commerce.utils.showToast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        DataHandling.getPaperContext(applicationContext)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        btn_logout.setOnClickListener{

            DataHandling.clearUserData()
            auth.signOut()
            LoginManager.getInstance().logOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()        }
    }


//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//
//        if (currentUser==null){
//            updateUI()
//        }
//
//    }

    private fun updateUI() {
        showToast(applicationContext,"Logged Out")
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
