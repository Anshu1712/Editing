package com.example.editing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.editing.Fragments.addFragment
import com.example.editing.Fragments.cameraFragment
import com.example.editing.Fragments.homeFragment
import com.example.editing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Binding object to access views in the layout
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()
        // Inflate the layout and set the content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust padding to accommodate system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the bottom navigation item selected listener
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                // When home navigation item is selected, replace with homeFragment
                R.id.home_nav -> setFragment(homeFragment())
                // When add navigation item is selected, replace with addFragment
                R.id.add -> setFragment(addFragment())
                // When camera navigation item is selected, replace with cameraFragment
                R.id.camera -> setFragment(cameraFragment())
                else -> return@setOnItemSelectedListener false
            }
            return@setOnItemSelectedListener true
        }
    }

    // Method to set the fragment in the main frame layout
    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        // Replace the current fragment with the new fragment
        transaction.replace(R.id.main_Frame, fragment)
        // Do not add the transaction to the back stack
        transaction.disallowAddToBackStack()
        // Commit the transaction
        transaction.commit()
    }
}