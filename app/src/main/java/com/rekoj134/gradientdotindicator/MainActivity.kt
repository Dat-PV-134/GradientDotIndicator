package com.rekoj134.gradientdotindicator

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.rekoj134.gradientdotindicator.indicator.GradientDotIndicator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        val dotIndicator = findViewById<GradientDotIndicator>(R.id.dotsIndicator)
        val viewPager = findViewById<ViewPager>(R.id.vpTest)
        val sliderAdapter = SliderAdapter(this@MainActivity)

        viewPager.adapter = sliderAdapter
        dotIndicator.attachTo(viewPager)
//        dotIndicator.setListColor(listOf(
//            Color.parseColor("#F31414"),
//            Color.parseColor("#16DC96"),
//            Color.parseColor("#6A12C3")
//        )
//        )
    }
}