package com.soumik.e_commerce.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.soumik.e_commerce.R
import com.soumik.e_commerce.data.DataHandling
import com.soumik.e_commerce.models.Users
import com.soumik.e_commerce.prevalent.Prevalent
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        DataHandling.getPaperContext(applicationContext)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        toolbar.title="Home"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var headerView = nav_view.getHeaderView(0)

        var userNameTV = headerView.tv_user_profile_name
        var userProfileIcon= headerView.iv_user_profile_image


        var name = LoginActivity.onlineUserName
        Log.i("1121",""+name)

        userNameTV?.text = name
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_cart -> {

            }
            R.id.nav_orders -> {

            }
            R.id.nav_category -> {

            }
            R.id.nav_setting -> {

            }
            R.id.nav_logout -> {

                DataHandling.clearUserData()
                auth.signOut()
                LoginManager.getInstance().logOut()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
