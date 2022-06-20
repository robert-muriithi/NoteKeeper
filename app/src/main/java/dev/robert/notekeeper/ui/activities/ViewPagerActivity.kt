package dev.robert.notekeeper.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.robert.notekeeper.adapter.ViewPagerAdapter
import dev.robert.notekeeper.databinding.ActivityViewPagerBinding
import dev.robert.notekeeper.onboarding.OnBoardingScreenOne
import dev.robert.notekeeper.onboarding.OnBoardingScreenThree
import dev.robert.notekeeper.onboarding.OnBoardingScreenTwo

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = arrayListOf(
            OnBoardingScreenOne(),
            OnBoardingScreenTwo(),
            OnBoardingScreenThree()
        )

        val adapter = ViewPagerAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.indicator.setViewPager(binding.viewPager)
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)

        binding.buttonGetStarted.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            onBoardingFinished()
        }
    }

    private fun onBoardingFinished() {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("onBoardingFinished", true)
        editor.apply()
    }
}