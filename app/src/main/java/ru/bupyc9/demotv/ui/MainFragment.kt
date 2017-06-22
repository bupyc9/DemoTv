package ru.bupyc9.demotv.ui

import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.ListRowPresenter
import android.util.Log
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.model.Movie


class MainFragment: BrowseFragment() {
    private val TAG: String = MainFragment::class.java.simpleName

    private lateinit var mRowsAdapter: ArrayObjectAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.i(TAG, "onActivityCreated")

        setUIElements()
        loadRows()
    }

    private fun setUIElements() {
        title = getString(R.string.browse_title)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.fastlane_background)
        searchAffordanceColor = resources.getColor(R.color.search_opaque)
    }

    private fun loadRows() {
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        initGridItemPresenter()
        initCardPresenter()

        adapter = mRowsAdapter
    }

    private fun initGridItemPresenter() {
        val gridItemPresenterHeader = HeaderItem(0, "GridItemPresenter")
        val gridPresenter = GridItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(gridPresenter)

        gridRowAdapter.add("ITEM 1")
        gridRowAdapter.add("ITEM 2")
        gridRowAdapter.add("ITEM 3")

        mRowsAdapter.add(ListRow(gridItemPresenterHeader, gridRowAdapter))
    }

    private fun initCardPresenter() {
        val cardPresenterHeader = HeaderItem(1, "CardPresenter")
        val cardPresenter = CardPresenter()
        val cardRowAdapter = ArrayObjectAdapter(cardPresenter)

        for (i in 0..9) {
            val movie = Movie(i, "title $i", "studio $i")
            cardRowAdapter.add(movie)
        }

        mRowsAdapter.add(ListRow(cardPresenterHeader, cardRowAdapter))
    }
}
