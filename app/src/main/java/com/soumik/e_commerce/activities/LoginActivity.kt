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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressDialog = ProgressDialog(this)

        DataHandling.getPaperContext(applicationContext)

        btn_do_login.setOnClickListener {
            validateLoginFields()
        }
    }

    private fun validateLoginFields() {
        var phoneNumber: String = et_phone_number.text.toString()
        var password: String = et_password.text.toString()

        when {
            TextUtils.isEmpty(phoneNumber) -> showToast(applicationContext, "Phone number field can't be empty")
            TextUtils.isEmpty(password) -> showToast(applicationContext, "Password field can't be empty")

            else -> {
                progressDialog.setTitle("Logging In")
                progressDialog.setMessage("Please wait, while we are matching the credentials")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                allowAcess(phoneNumber, password)
            }
        }
    }

    private fun allowAcess(phoneNumber: String, password: String) {

        if (cb_remember_me.isChecked){
            DataHandling.setUserPhoneNumber(phoneNumber)
            DataHandling.setUserPassword(password)
        }

        val rootref: DatabaseReference = FirebaseDatabase.getInstance().reference

        rootref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child(parentDatabase).child(phoneNumber).exists()) {

                    var userData: Users? = p0.child(parentDatabase).child(phoneNumber).getValue(Users::class.java)

                    if (userData?.Phone?.equals(phoneNumber)!!) {
                        if (userData?.Password?.equals(password)) {
                            showToast(applicationContext, "Logged In Successfully!")
                            progressDialog.dismiss()
                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                            finish()
                        } else{
                            showToast(applicationContext,"Incorrect Password")
                            progressDialog.dismiss()
                        }
                    }

                } else {
                    showToast(applicationContext, "Please Create an account first to Login")
                    progressDialog.dismiss()
                    startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
                    finish()
                }
            }
        })
    }
}
