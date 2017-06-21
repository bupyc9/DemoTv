package ru.bupyc9.demotv.ui

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.util.Log
import ru.bupyc9.demotv.R


class MainFragment: BrowseFragment() {
    private val TAG: String = MainFragment::class.java.simpleName

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.i(TAG, "onActivityCreated")

        setUIElements()
    }

    private fun setUIElements() {
        title = getString(R.string.browse_title)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.fastlane_background)
        searchAffordanceColor = resources.getColor(R.color.search_opaque)
    }
}
