package com.smartloopLearn.learning.student.view.activities

import android.content.Context
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
import com.smartloopLearn.learning.student.view.fragments.CourseFragment
import com.smartloopLearn.learning.student.view.fragments.HomeFragment
import com.smartloopLearn.learning.student.view.fragments.ProfileFragment
import com.smartloopLearn.learning.student.view.fragments.SearchFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.smartloopLearn.learning.AboutUs
import com.smartloopLearn.learning.auth.CreateNewPassword
import com.smartloopLearn.learning.R
import com.smartloopLearn.learning.admin.DashboardUserActivity
import com.smartloopLearn.learning.auth.LoginSignUp

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityHomeBinding

    private val sharedPrefFile = "smartloopLearnPrefs"
    private val sharedPrefNameKey = "user_name"
    private val sharedPrefEmailKey = "user_email"
    private val sharedPrefPhoneKey = "user_phone"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        setSupportActionBar(binding.toolbarHomeActivity)
        binding.toolbarHomeActivity.title = "Smart"
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbarHomeActivity,
            R.string.nav_open, R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        // Load user data and store in shared preferences
        loadUserData()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
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

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val phone = document.getString("phone")

                        // Store user data in shared preferences
                        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString(sharedPrefNameKey, name)
                        editor.putString(sharedPrefEmailKey, email)
                        editor.putString(sharedPrefPhoneKey, phone)
                        editor.apply()

                    } else {
                        Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to fetch user data: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

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
                val i = Intent(this, Feedback::class.java)
                startActivity(i)
            }
            R.id.update_password -> {
                val i = Intent(this, CreateNewPassword::class.java)
                startActivity(i)
            }
            R.id.news ->{
                val i = Intent(this, DashboardUserActivity::class.java)
                startActivity(i)
            }
            R.id.delete_account -> {
                val user = auth.currentUser
                user?.delete()?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                        val i = Intent(this, LoginSignUp::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(this, "Account deletion failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.contact_us -> {
                val number = "+923464298524"
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            }
            R.id.log_out ->  {
                auth.signOut()
                val i = Intent(this, LoginSignUp::class.java)
                startActivity(i)
                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
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
