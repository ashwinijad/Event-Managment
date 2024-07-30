package com.example.eventmanagementapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventmanagementapp.databinding.ActivitySplashBinding
import com.example.eventmanagementapp.ui.home.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EventLauncherScreen: AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(1000)
            val i = Intent(
                this@EventLauncherScreen,
                MainActivity::class.java
            )
            startActivity(i)
            finish()
        }
    }
}