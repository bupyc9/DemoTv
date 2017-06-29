package ru.bupyc9.demotv.ui.error

import android.os.Bundle
import android.support.v17.leanback.app.ErrorFragment
import android.util.Log
import ru.bupyc9.demotv.R

class ErrorFragment: ErrorFragment() {
    companion object {
        @JvmStatic private val TAG = ErrorFragment::class.java.simpleName
        @JvmStatic private val TRANSLUCENT = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")
        title = resources.getString(R.string.app_name)
        setErrorContent()
    }

    private fun setErrorContent() {
        imageDrawable = activity.getDrawable(R.drawable.lb_ic_sad_cloud)
        message = resources.getString(R.string.error_fragment_message)
        setDefaultBackground(TRANSLUCENT)

        buttonText = resources.getString(R.string.dismiss_error)
        setButtonClickListener { fragmentManager.beginTransaction().remove(this).commit() }
    }
}