package ru.bupyc9.demotv.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import ru.bupyc9.demotv.R

class SearchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchActivity::class.java))
        return true
    }
}
