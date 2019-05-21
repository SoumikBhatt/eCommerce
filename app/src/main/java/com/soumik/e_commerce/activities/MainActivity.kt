package com.soumik.e_commerce.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.database.*
import com.soumik.e_commerce.R
import com.soumik.e_commerce.data.DataHandling
import com.soumik.e_commerce.models.Users
import com.soumik.e_commerce.utils.parentDatabase
import com.soumik.e_commerce.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressDialog = ProgressDialog(this)

        DataHandling.getPaperContext(applicationContext)

        btn_login.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        btn_sign_up.setOnClickListener{
            var intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

        var password = DataHandling.getUserPassword()
        var phone = DataHandling.getUserPhoneNumber()

        if (phone!=""&& password!=null){

            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)){
                allowAccess(phone,password)

                progressDialog.setTitle("Already Logging In")
                progressDialog.setMessage("Please wait")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
            }
        }
    }

    private fun allowAccess(phoneNumber: String?, password: String) {

        val rootref: DatabaseReference = FirebaseDatabase.getInstance().reference

        rootref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(parentDatabase).child(phoneNumber!!).exists()) {

                    var userData: Users? = p0.child(parentDatabase).child(phoneNumber).getValue(Users::class.java)

                    if (userData?.Phone?.equals(phoneNumber)!!) {
                        if (userData?.Password?.equals(password)) {
                            showToast(applicationContext, "Logged In Successfully!")
                            progressDialog.dismiss()
                            startActivity(Intent(this@MainActivity,HomeActivity::class.java))
                            finish()
                        } else{
                            showToast(applicationContext,"Incorrect Password")
                            progressDialog.dismiss()
                        }
                    }

                } else {
                    showToast(applicationContext, "Please Create an account first to Login")
                    progressDialog.dismiss()
                    startActivity(Intent(this@MainActivity, SignupActivity::class.java))
                    finish()
                }
            }
        })
    }
}
