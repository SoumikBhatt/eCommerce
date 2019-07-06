package com.soumik.e_commerce.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.soumik.e_commerce.R
import com.soumik.e_commerce.models.Products
import com.soumik.e_commerce.utils.showToast
import kotlinx.android.synthetic.main.activity_product_details.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProductDetailsActivity : AppCompatActivity() {

    companion object{
        val PRODUCT_ID: String =  "PID"
    }

    var productDetailsID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productDetailsID = intent.getStringExtra(PRODUCT_ID)

        getProductDetails(productDetailsID)

        fbtn_add_cart.setOnClickListener{
            addingToCart()
        }
    }

    private fun addingToCart() {
        var saveCurrentTime = ""
        var saveCurrentDate= ""

        var calendar:Calendar = Calendar.getInstance()

        var currentDate:SimpleDateFormat = SimpleDateFormat("MMM dd,yyyy")
        saveCurrentDate = currentDate.format(calendar.time)


        var currentTime:SimpleDateFormat = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentDate.format(calendar.time)

        val cartListReference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Cart List")

        val cartMap :HashMap<String,Any> = HashMap()

        cartMap["pId"] = productDetailsID
        cartMap["pName"] = tv_product_det_name.text.toString()
        cartMap["price"] = tv_product_det_price.text.toString()
        cartMap["date"] = saveCurrentDate
        cartMap["time"] = saveCurrentTime
        cartMap["quantity"] = ebtn_product_number.number
        cartMap["discount"] = ""

        cartListReference.child("User Cart View").child(LoginActivity.onlineUserPhone).child("Products").child(productDetailsID)
            .updateChildren(cartMap).addOnCompleteListener { p0 ->
                if (p0.isSuccessful){
                    cartListReference.child("Admin Cart View").child(LoginActivity.onlineUserPhone).child("Products").child(productDetailsID)
                        .updateChildren(cartMap).addOnCompleteListener { p0 ->
                            if (p0.isSuccessful){
                                showToast(applicationContext,"Added to CartList")
                                startActivity(Intent(this@ProductDetailsActivity,HomeActivity::class.java))
                                finish()
                            }
                        }
                }
            }
    }

    private fun getProductDetails(productDetailsID: String?) {

        var productReference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Products")

        productReference.child(productDetailsID!!).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    var products:Products = p0.getValue(Products::class.java)!!
                    tv_product_det_name.text="Name: "+products.pName
                    tv_product_det_category.text="Categoty: "+products.category
                    tv_product_det_price.text="Price: "+products.price
                    tv_product_det_description.text="Description: "+products.description

                    Glide.with(applicationContext)
                        .load(products.image)
                        .into(iv_product_det_icon)
                }
            }
        })

    }
}
