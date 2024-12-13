package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umi.app.vantech.databinding.ActivityOnBoardingBinding
import umi.app.vantech.adapter.OnAdapter

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var onboardingAdapter: OnAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onboardingAdapter = OnAdapter(::navigateNext)
        binding.viewPager.adapter = onboardingAdapter

        setupIndicator()
    }

    private fun setupIndicator() {
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun navigateNext() {
        val currentItem = binding.viewPager.currentItem
        if (currentItem < onboardingAdapter.itemCount - 1) {
            binding.viewPager.currentItem = currentItem + 1
        } else {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }
    }

}