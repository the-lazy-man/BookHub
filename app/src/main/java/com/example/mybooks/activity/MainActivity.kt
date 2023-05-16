package com.example.mybooks.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.example.mybooks.*

//import com.internshala.bookhub.*
//import com.internshala.bookhub.fragment.AboutAppFragment
//import com.internshala.bookhub.fragment.DashboardFragment
//import com.internshala.bookhub.fragment.FavouritesFragment
//import com.internshala.bookhub.fragment.ProfileFragment
class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinatorLayout : CoordinatorLayout
    lateinit var navigationView : NavigationView
    lateinit var toolbar : Toolbar
    lateinit var frameLayout : FrameLayout
    var previousMenuItem : MenuItem ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout,
            R.string.open_drawer, R.string.close_drawer
        )
            drawerLayout.addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem != null)
                previousMenuItem?.isChecked = false
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it
            when(it.itemId){
                R.id.dashboard ->{
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
                        .addToBackStack("Favourites")
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())
                        .addToBackStack("About app")
                        .commit()
                    supportActionBar?.title="About app"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        val id = item.itemId


        if (id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }


        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction =  supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}