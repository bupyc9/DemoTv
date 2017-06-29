package ru.bupyc9.demotv.ui.error

import android.app.Activity
import android.os.Bundle
import ru.bupyc9.demotv.R

class ErrorActivity: Activity() {
    companion object {
        @JvmStatic private val TAG = ErrorActivity::class.java.simpleName
    }

    private lateinit var mErrorFragment: ErrorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testError()
    }

    private fun testError() {
        mErrorFragment = ErrorFragment()
        fragmentManager.beginTransaction().add(R.id.main_browse_fragment, mErrorFragment).commit()
    }
}