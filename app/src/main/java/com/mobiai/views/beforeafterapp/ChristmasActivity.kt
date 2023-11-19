package com.mobiai.views.beforeafterapp

import android.app.Activity
import android.os.Bundle
import com.mobiai.views.chrismas.CountryFlagView

class ChristmasActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_christmas)


        val countryFlagView = findViewById<CountryFlagView>(R.id.christmas)

        countryFlagView.updateBackground(R.drawable.a)
        countryFlagView.updateObject(R.drawable.b)

    }
}