package com.jjcr11.memorygame.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bnv.selectedItemId = R.id.iPlay

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        val navController = navHostFragment.navController

        var prev: Int

        binding.bnv.setOnItemSelectedListener { item ->
            prev = binding.bnv.selectedItemId
            when (item.itemId) {
                R.id.iScore -> {
                    if (binding.bnv.selectedItemId != R.id.iScore) {
                        navController.navigate(R.id.action_global_scoreFragment)
                    }
                    true
                }
                R.id.iPlay -> {
                    if (prev == R.id.iScore) {
                        navController.navigate(R.id.action_scoreFragment_to_gameFragment)
                    } else if (prev == R.id.iSettings) {
                        navController.navigate(R.id.action_settingsFragment_to_gameFragment)
                    }
                    true
                }
                R.id.iSettings -> {
                    if (binding.bnv.selectedItemId != R.id.iSettings) {
                        navController.navigate(R.id.action_global_settingsFragment)
                    }
                    true
                }
                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                updateBottomNavigation()
            }
        })

    }

    fun updateBottomNavigation() {
        if (binding.bnv.selectedItemId != R.id.iPlay) {
            binding.bnv.selectedItemId = R.id.iPlay
        } else {
            finish()
        }
    }
}