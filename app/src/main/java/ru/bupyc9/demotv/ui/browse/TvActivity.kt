package ru.bupyc9.demotv.ui.browse

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.ui.search.SearchActivity

class TvActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchActivity::class.java))
        return true
    }
}
