package com.smartloopLearn.learning

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.smartloopLearn.learning.databinding.ActivityHomeBinding
import com.smartloopLearn.learning.fragments.CourseFragment
import com.smartloopLearn.learning.fragments.HomeFragment
import com.smartloopLearn.learning.fragments.ProfileFragment
import com.smartloopLearn.learning.fragments.SearchFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Smart"
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)


//        binding.bottomNavigation.background = null

        binding.bottomNavigation.setOnItemSelectedListener {
                item -> when(item.itemId){
            R.id.nav_bottom_home -> openFragment(HomeFragment())
            R.id.nav_bottom_course -> openFragment(CourseFragment())
            R.id.nav_bottom_search -> openFragment(SearchFragment())
            R.id.nav_bottom_profile -> openFragment(ProfileFragment())
        }
            true
        }

        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())

    }


    // ------ My  Side  Bar --------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_side_home -> openFragment(HomeFragment())
            R.id.nav_side_course -> openFragment(CourseFragment())
            R.id.nav_side_search -> openFragment(SearchFragment())
            R.id.nav_side_profile -> openFragment(ProfileFragment())
            R.id.about_us -> {
                val i = Intent(this, AboutUs::class.java)
                startActivity(i)
            }
            R.id.privacy_policy -> {
                val i = Intent(this, PrivacyPolicy::class.java)
                startActivity(i)
            }
            R.id.feedback -> {
                val i = Intent(this,Feedback::class.java)
                startActivity(i)
            }
            R.id.update_password -> {
                val i = Intent(this, CreateNewPassword::class.java)
                startActivity(i)
            }
            // ---- Delete User ----
            R.id.delete_account -> {
                val user = auth.currentUser
                user?.delete()?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Account delete successfully", Toast.LENGTH_SHORT).show()
                        val i = Intent(this, LoginSignUp::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(this, "Account don't delete successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // Contact Us
            R.id.contact_us -> {
                val number = "+923464298524"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.setData(Uri.parse("tel:$number"))
                startActivity(intent)
            }
            // Log Out Logic
            R.id.log_out ->  {
                auth.signOut()

                val i = Intent(this,
                    LoginSignUp::class.java)
                startActivity(i)

                Toast.makeText(this, "Log Out Successfully",
                    Toast.LENGTH_SHORT).show()

                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
        super.onBackPressed()
    }


    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


}