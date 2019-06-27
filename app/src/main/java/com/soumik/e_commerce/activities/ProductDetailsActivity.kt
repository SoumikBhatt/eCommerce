package com.soumik.e_commerce.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.soumik.e_commerce.R
import com.soumik.e_commerce.models.Products
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : AppCompatActivity() {

    companion object{
        val PRODUCT_ID: String =  "PID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        var productDetailsID = intent.getStringExtra(PRODUCT_ID)

        getProductDetails(productDetailsID)
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
