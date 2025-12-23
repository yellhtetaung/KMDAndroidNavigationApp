package com.kmd.navigationappapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kmd.navigationappapplication.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var navController: NavController

    // This function will be run when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val id = bundle?.getInt("id")
        val firstName = bundle?.getString("firstName")
        val lastName = bundle?.getString("lastName")
        val username = bundle?.getString("username")

        Log.d("User", "***$id, $username, $firstName, $lastName")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.userFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

//        setupActionBarWithNavController(navController)

        binding.bottomNavView.setupWithNavController(navController)
        navController.navigate(R.id.welcomeUserFragment, bundle)
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemWelcome -> {
                    navController.navigate(R.id.welcomeUserFragment, bundle)
                    true
                }

                R.id.itemEntry -> {
                    navController.navigate(R.id.entryFragment, bundle)
                    true
                }

                R.id.itemList -> {
                    navController.navigate(R.id.listVeiwFragment, bundle)
                    true
                }

                else -> false
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }

    // This function use to load menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)

        return true
    }

    // This function use to handle menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val result = when (item.itemId) {
            R.id.itemLogout -> {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> false
        }
        return result
    }
}
