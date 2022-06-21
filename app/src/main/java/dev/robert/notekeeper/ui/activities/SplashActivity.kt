package dev.robert.notekeeper.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.notekeeper.R

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Handler().postDelayed({
            if (onBoardingCompleted() || user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, ViewPagerActivity::class.java))
                finish()
            }
        }, 2000)
//
    }

    private fun onBoardingCompleted(): Boolean {
        val prefs = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return prefs?.getBoolean("onBoardingFinished", false) ?: false
    }
}