package com.soumik.e_commerce.activities.admin_activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.soumik.e_commerce.R
import kotlinx.android.synthetic.main.activity_catergory.*

class CatergoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catergory)

        iv_cat_t_shirts.setOnClickListener {
            startActivity(Intent(this, AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME, "T-Shirts"))
        }
        iv_cat_sports_shirts.setOnClickListener {
            startActivity(
                Intent(this, AdminPanelActivity::class.java).putExtra(
                    AdminPanelActivity.CATEGORY_NAME,
                    "Sport-Shirts"
                )
            )
        }
        iv_cat_female_shirts.setOnClickListener {
            startActivity(
                Intent(this, AdminPanelActivity::class.java).putExtra(
                    AdminPanelActivity.CATEGORY_NAME,
                    "Female Dresses"
                )
            )

        }
        iv_cat_sweathers.setOnClickListener {
            startActivity(
                Intent(this, AdminPanelActivity::class.java).putExtra(
                    AdminPanelActivity.CATEGORY_NAME,
                    "Sweathers"
                )
            )

        }

        iv_cat_glass.setOnClickListener {
            startActivity(Intent(this, AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME, "Glasses"))
        }
        iv_cat_bag.setOnClickListener {
            startActivity(Intent(this, AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME, "Bags"))

        }
        iv_cat_hat.setOnClickListener{
            startActivity(Intent(this,AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME,"Hats"))

        }
        iv_cat_shoes.setOnClickListener{
            startActivity(Intent(this,AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME,"Shoes"))

        }

        iv_cat_headphones.setOnClickListener{
            startActivity(Intent(this,AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME,"Headphones"))
        }
        iv_cat_laptops.setOnClickListener{
            startActivity(Intent(this,AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME,"Laptops"))}
        iv_cat_watches.setOnClickListener{
            startActivity(Intent(this,AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME,"Watches"))
        }
        iv_cat_mobiles.setOnClickListener{
            startActivity(Intent(this,AdminPanelActivity::class.java).putExtra(AdminPanelActivity.CATEGORY_NAME,"Mobiles"))
        }
    }
}
