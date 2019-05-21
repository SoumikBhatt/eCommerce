package com.soumik.e_commerce.activities

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.database.*
import com.soumik.e_commerce.R
import com.soumik.e_commerce.utils.showToast
import kotlinx.android.synthetic.main.activity_signup.*
import kotlin.collections.HashMap

class SignupActivity : AppCompatActivity() {

    lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        progressDialog = ProgressDialog(this)

        btn_do_sign_up.setOnClickListener{
            validateFields()
        }
    }

    private fun validateFields() {

        var userName : String = et_user_name_register.text.toString()
        var userPhoneNumber : String = et_phone_number_register.text.toString()
        var userPassword : String = et_password_register.text.toString()

        when {
            TextUtils.isEmpty(userName) -> showToast(applicationContext,"User Name can't be empty")
            TextUtils.isEmpty(userPhoneNumber) -> showToast(applicationContext,"Phone Number can't be empty")
            TextUtils.isEmpty(userPassword) -> showToast(applicationContext,"Password can't be empty")
            else -> {
                progressDialog.setTitle("Creating Account")
                progressDialog.setMessage("Please wait, while we are matching the credentials")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
//                showProgressBar(applicationContext,"Create Account","Please wait, while we are matching the credentials")
                validatePhoneNumber(userName,userPhoneNumber,userPassword)
            }
        }
    }

    private fun validatePhoneNumber(userName: String, userPhoneNumber: String, userPassword: String) {
        val rootReference : DatabaseReference = FirebaseDatabase.getInstance().reference

        rootReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (!(p0.child("Users").child(userPhoneNumber).exists())){

                    var userMap:HashMap<String,Any> = HashMap()
                    userMap["Phone"] = userPhoneNumber
                    userMap["Name"] = userName
                    userMap["Password"] = userPassword

                    rootReference.child("Users").child(userPhoneNumber).updateChildren(userMap)
                        .addOnCompleteListener { p0 ->
                            if (p0.isSuccessful){
                                showToast(applicationContext,"Congratulations! Your Account is created")
//                                hideProgressBar()
                                progressDialog.dismiss()
                                startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                                finish()
                            } else{
                                showToast(applicationContext,"Something is wrong! Please Try Again")
//                                hideProgressBar()
                                progressDialog.dismiss()
                            }
                        }

                } else {
                    showToast(applicationContext, "This $userPhoneNumber number already exists")
//                    hideProgressBar()
                    progressDialog.dismiss()
                    showToast(applicationContext,"Please try again with another phone number")
                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                }
            }
        })
    }
}
