package com.job.ebursary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.job.ebursary.commoners.Tools
import com.job.ebursary.fragments.BursaryFragment
import com.job.ebursary.fragments.HomeFragment
import com.job.ebursary.fragments.MyApplicationFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java)
    }

    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initNavigationMenu()

        loadFragment(HomeFragment())

        nav_view.getHeaderView(0).findViewById<TextView>(R.id.email).text = auth.currentUser?.email
        nav_view.getHeaderView(0).findViewById<TextView>(R.id.name).text = auth.currentUser?.displayName

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        actionBar.title = "eBursary"
        Tools.setSystemBarColor(this)
    }

    private fun initNavigationMenu() {

        val toggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener { item ->

            val id = item.itemId

            when(id){
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                }

                R.id.nav_myapplication -> {
                    loadFragment(MyApplicationFragment())
                }

                R.id.nav_bursary -> {
                    loadFragment(BursaryFragment())
                }

                R.id.nav_applybursary -> {
                    startActivity(EnterDataActivity.newIntent(this))
                }

                R.id.nav_signout -> {
                    Toast.makeText(applicationContext, "${item.title} Signed out", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(LoginActivity.newIntent(this))
                }
            }

            actionBar?.title = item.title
            drawer_layout.closeDrawers()
            true
        }

        // open drawer at start
        //drawer_layout.openDrawer(GravityCompat.START)

        nav_view.menu.getItem(0).isChecked = true
    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

}
