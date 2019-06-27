package com.soumik.e_commerce.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.soumik.e_commerce.R
import com.soumik.e_commerce.prevalent.Prevalent
import com.soumik.e_commerce.utils.hideProgressDialog
import com.soumik.e_commerce.utils.showProgressDialog
import com.soumik.e_commerce.utils.showToast
import com.theartofdev.edmodo.cropper.CropImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_settings.*
import java.net.URI
import kotlin.coroutines.Continuation

class SettingsActivity : AppCompatActivity() {

    var imageURI: Uri? = null
    private lateinit var storageReference: StorageReference
    lateinit var storageTask:UploadTask

    lateinit var progressDialog: ProgressDialog

    var myURL = ""
    var checker = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        storageReference = FirebaseStorage.getInstance().reference.child("Profile Pics")

        tv_close.setOnClickListener{
            onBackPressed()
        }

        progressDialog = ProgressDialog(this)

        tv_setting_change_icon.setOnClickListener {
            checker = "Clicked"

            CropImage.activity(imageURI)
                .setAspectRatio(1,1)
                .start(this)
        }

        tv_update.setOnClickListener {
            if (checker == "Clicked"){
                userInfoSaved()
            } else{
                userInfoUpdated()
            }
        }

        var upProfileImage = iv_setting_profile_icon
        var upProfileName = et_set_full_name
        var upPhoneNumber = et_set_phone_num
        var upAddress = et_set_address
        var changeImage = tv_setting_change_icon

        Log.i("222",""+LoginActivity.onlineUserPhone)
        Log.i("222",""+LoginActivity.onlineUserName)

        userInfoDisplay(upProfileImage,upProfileName,upPhoneNumber,upAddress)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== Activity.RESULT_OK && data!=null){
            var result:CropImage.ActivityResult = CropImage.getActivityResult(data)

            imageURI = result.uri

            iv_setting_profile_icon.setImageURI(imageURI)
        } else{

            showToast(applicationContext,"Error! Try Again")
            startActivity(Intent(this,SettingsActivity::class.java))
            finish()
        }
    }

    private fun userInfoUpdated() {

        var reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        var updateMap:HashMap<String,Any> = HashMap()

        updateMap["Name"] = et_set_full_name.text.toString()
        updateMap["Address"] = et_set_address.text.toString()
        updateMap["PhoneUp"] = et_set_phone_num.text.toString()

        reference.child(LoginActivity.onlineUserPhone).updateChildren(updateMap)

        startActivity(Intent(this,HomeActivity::class.java))
        showToast(applicationContext,"Account Info updated Successfully")
        finish()
    }

    private fun userInfoSaved() {

        when {
            TextUtils.isEmpty(et_set_full_name.text.toString()) -> showToast(applicationContext,"Name is Mandatory")
            TextUtils.isEmpty(et_set_address.text.toString()) -> showToast(applicationContext,"Address is Mandatory")
            TextUtils.isEmpty(et_set_phone_num.text.toString()) -> showToast(applicationContext,"Phone Number is Mandatory")
            checker == "Clicked" -> uploadImage()
        }
    }

    private fun uploadImage() {

        showProgressDialog(progressDialog,"Profile Updating","Please wait while we are updating your account information's")

        if (imageURI!=null){

            val fileReference:StorageReference = storageReference.child(LoginActivity.onlineUserPhone + ".jpg")

            storageTask = fileReference.putFile(imageURI!!)

            storageTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> {

                if (!it.isSuccessful){
                    throw it.exception!!
                }

                return@Continuation fileReference.downloadUrl
            }).addOnCompleteListener(OnCompleteListener<Uri>{

                if (it.isSuccessful){
                    var downloadurl = it.result

                    myURL = downloadurl.toString()

                    var reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

                    var updateMap:HashMap<String,Any> = HashMap()

                    updateMap["Name"] = et_set_full_name.text.toString()
                    updateMap["Address"] = et_set_address.text.toString()
                    updateMap["PhoneUp"] = et_set_phone_num.text.toString()
                    updateMap["images"] = myURL

                    reference.child(LoginActivity.onlineUserPhone).updateChildren(updateMap)

                    hideProgressDialog(progressDialog)
                    startActivity(Intent(this,HomeActivity::class.java))
                    showToast(applicationContext,"Account Info updated Successfully")
                    finish()
                } else{
                    hideProgressDialog(progressDialog)
                    showToast(applicationContext,"Error "+it.exception)
                }
            })

        } else{
            showToast(applicationContext,"No Image Selected")
        }
    }

    private fun userInfoDisplay(upProfileImage: CircleImageView?, upProfileName: EditText?, upPhoneNumber: EditText?, upAddress: EditText?) {

        var userRef:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(LoginActivity.onlineUserPhone)

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    if (p0.child("images").exists()){

                        var image:String = p0.child("images").value.toString()
                        var name:String = p0.child("Name").value.toString()
                        var phone:String = p0.child("Phone").value.toString()
                        var address:String = p0.child("address").value.toString()

                        Glide.with(applicationContext)
                            .load(image)
                            .into(upProfileImage!!)
                        upProfileName?.setText(name)
                        upPhoneNumber?.setText(phone)
                        upAddress?.setText(address)
                    }
                }
            }
        })
    }
}
