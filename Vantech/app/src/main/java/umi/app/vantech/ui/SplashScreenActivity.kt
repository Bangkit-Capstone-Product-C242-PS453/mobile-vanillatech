package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import umi.app.vantech.Preferences
import umi.app.vantech.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding
    private val DELAY = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = Preferences(this)
        val statusLogin = pref.getValues("STATUS_LOGIN")

        val intent = when{
            statusLogin == "true" -> Intent(this@SplashScreenActivity, MainActivity::class.java)
            else -> Intent(this@SplashScreenActivity, OnBoardingActivity::class.java)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        },DELAY)




    }
}