package com.mobiai.views.beforeafterapp

import android.app.Activity
import android.os.Bundle
import com.mobiai.views.chrismas.CountryFlagView

class CountryFlagActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_country_flag)


        val countryFlagView = findViewById<CountryFlagView>(R.id.countryflag)

        countryFlagView.updateBackground(R.drawable.a)
        countryFlagView.updateObject(R.drawable.b)

    }
}