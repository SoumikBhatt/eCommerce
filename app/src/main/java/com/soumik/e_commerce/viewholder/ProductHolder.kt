package com.soumik.e_commerce.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.soumik.e_commerce.interfaces.ItemClickListener
import kotlinx.android.synthetic.main.item_products.view.*

class ProductHolder(itemView:View): RecyclerView.ViewHolder(itemView),View.OnClickListener {

    private lateinit var itemClickListener:ItemClickListener

    val productName = itemView.tv_product_name
    val productIcon = itemView.iv_product_icon
    val productCategory = itemView.tv_product_category
    val productPrice = itemView.tv_product_price
    val productDescription = itemView.tv_product_description

    fun setItemClickListener(listener: ItemClickListener){
        itemClickListener = listener
    }

    override fun onClick(v: View?) {

        itemClickListener.onItemClick(v!!,adapterPosition,false)
    }
}