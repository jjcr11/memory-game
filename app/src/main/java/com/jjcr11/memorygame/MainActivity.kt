package com.jjcr11.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import com.jjcr11.memorygame.databinding.ActivityMainBinding
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bnv.selectedItemId = R.id.iPlay

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.iScore -> {
                    if(binding.bnv.selectedItemId != R.id.iScore) {
                        navController.navigate(R.id.action_global_scoreFragment)
                    }
                    true
                }
                R.id.iPlay -> {
                    if(binding.bnv.selectedItemId != R.id.iPlay) {
                        navController.popBackStack()
                    }
                    true
                }
                R.id.iSettings -> {
                    if(binding.bnv.selectedItemId != R.id.iSettings) {
                        navController.navigate(R.id.action_global_settingsFragment)
                    }
                    true
                }
                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.bnv.selectedItemId != R.id.iPlay) {
                    binding.bnv.selectedItemId = R.id.iPlay
                } else {
                    finish()
                }
            }
        })

    }
}