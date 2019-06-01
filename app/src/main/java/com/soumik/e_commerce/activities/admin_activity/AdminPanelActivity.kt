package com.soumik.e_commerce.activities.admin_activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.soumik.e_commerce.R
import com.soumik.e_commerce.utils.hideProgressDialog
import com.soumik.e_commerce.utils.showProgressDialog
import com.soumik.e_commerce.utils.showToast
import kotlinx.android.synthetic.main.activity_admin_panel.*
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AdminPanelActivity : AppCompatActivity() {

    companion object{
        val CATEGORY_NAME = "category"
        val GALLERY_PICK = 1
    }

    lateinit var categoryName:String
    lateinit var saveCurrentDate:String
    lateinit var saveCurrentTime:String
    lateinit var randomProductKey:String
    lateinit var productName:String
    lateinit var productDescription:String
    lateinit var productPrice:String
    lateinit var downloadImageURL:String

    lateinit var productImageRef:StorageReference
    lateinit var productReference: DatabaseReference

    lateinit var progressDialog:ProgressDialog

    var imageURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        progressDialog = ProgressDialog(this)

        categoryName = intent.getStringExtra(CATEGORY_NAME).toString()

        productImageRef = FirebaseStorage.getInstance().reference.child("Product Images")

        productReference = FirebaseDatabase.getInstance().reference.child("Products")

        iv_select_product_icon.setOnClickListener{
            openGallery()
        }

        btn_add_new_product.setOnClickListener{
            validateProducts()
        }

    }

    private fun validateProducts() {
        productName = et_product_name.text.toString()
        productDescription = et_product_description.text.toString()
        productPrice = et_product_price.text.toString()

        when {
            imageURI==null -> showToast(applicationContext,"Product Image is Mandatory! Please select a product Image")
            TextUtils.isEmpty(productName) -> showToast(applicationContext,"Please provide the product name")
            TextUtils.isEmpty(productDescription) -> showToast(applicationContext,"Please provide the product description")
            TextUtils.isEmpty(productPrice) -> showToast(applicationContext,"Please provide the product price")

            else->{
                storeProductInformation()
            }
        }
    }

    private fun storeProductInformation() {

        showProgressDialog(progressDialog,"Adding New Product","Please wait, while we are adding your product")

        var calendar : Calendar = Calendar.getInstance()

        var currentDate = SimpleDateFormat("MMM dd,yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        var currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.time)

        randomProductKey = saveCurrentDate + saveCurrentTime

        var filePath:StorageReference = productImageRef.child(imageURI?.lastPathSegment!! + randomProductKey + ".jpg")

        val uploadTask:UploadTask = filePath.putFile(imageURI!!)

        uploadTask.addOnFailureListener{
            hideProgressDialog(progressDialog)
            showToast(applicationContext, "Error: $it")
        }.addOnSuccessListener {
            showToast(applicationContext,"Product Image uploaded successfully")

            var uriTask:Task<Uri> = uploadTask.continueWithTask { p0 ->
                if (!p0.isSuccessful){
                    throw p0.exception!!
//                    hideProgressDialog(progressDialog)
                }

                downloadImageURL = filePath.downloadUrl.toString()
                filePath.downloadUrl
            }.addOnCompleteListener { p0 ->
                if (p0.isSuccessful){

                    downloadImageURL = p0.result.toString()

                    showToast(applicationContext,"Product image URL is found successfully")

                    saveProductInDatabase()
                }
            }
        }
    }

    private fun saveProductInDatabase() {
        var productMap:HashMap<String,Any> = HashMap()

        productMap["pid"] = randomProductKey
        productMap["date"] = saveCurrentDate
        productMap["time"] = saveCurrentTime
        productMap["description"] = productDescription
        productMap["image"] = downloadImageURL
        productMap["category"] = categoryName
        productMap["price"] = productPrice
        productMap["pName"] = productName

        productReference.child(randomProductKey).updateChildren(productMap)
            .addOnCompleteListener{ p0 ->

                if (p0.isSuccessful){

                    startActivity(Intent(this,CatergoryActivity::class.java))
                    hideProgressDialog(progressDialog)
                    showToast(applicationContext,"Product is Added Successfully!")
                } else{
                    hideProgressDialog(progressDialog)
                    showToast(applicationContext,"Error: "+p0.exception.toString())
                }
            }
    }

    private fun openGallery() {
        var galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== GALLERY_PICK && resultCode == Activity.RESULT_OK && data!=null){
            imageURI = data.data
            iv_select_product_icon.setImageURI(imageURI)
        }
    }
}
