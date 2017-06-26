package ru.bupyc9.demotv

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity

class DetailsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}
