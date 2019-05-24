package com.soumik.e_commerce.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.soumik.e_commerce.R
import com.soumik.e_commerce.data.DataHandling
import com.soumik.e_commerce.models.Users
import com.soumik.e_commerce.utils.parentDatabase
import com.soumik.e_commerce.utils.showToast
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var progressDialog: ProgressDialog
    lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth
    val TAG = "FB_LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressDialog = ProgressDialog(this)


// ...
// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        DataHandling.getPaperContext(applicationContext)

        btn_do_login.setOnClickListener {
            validateLoginFields()
        }

        /////////

        createKeyHash(this, "com.soumik.e_commerce")

        bt_facebook_login.setOnClickListener{
            bt_facebook_login.isEnabled=false
            doFBLogin()
        }



        /////////
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser!=null){
            updateUI()
        }

    }

    private fun updateUI() {
        showToast(applicationContext,"Logged IN")
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun createKeyHash(activity: Activity, yourPackage: String) {
        val info = activity.packageManager.getPackageInfo(yourPackage, PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    }


    private fun doFBLogin() {
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })
        // ...
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    bt_facebook_login.isEnabled=true
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    bt_facebook_login.isEnabled=true
//                    updateUI()
                }

                // ...
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

        if (cb_remember_me.isChecked) {
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
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            showToast(applicationContext, "Incorrect Password")
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
