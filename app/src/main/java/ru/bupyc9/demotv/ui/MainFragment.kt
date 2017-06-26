package ru.bupyc9.demotv.ui

import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import ru.bupyc9.demotv.DetailsActivity
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.SimpleBackgroundManager
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.presenter.GridItemPresenter


class MainFragment: BrowseFragment() {
    private val TAG: String = MainFragment::class.java.simpleName

    private lateinit var mRowsAdapter: ArrayObjectAdapter
    private lateinit var mSimpleBackgroundManager: SimpleBackgroundManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.i(TAG, "onActivityCreated")

        setUIElements()
        loadRows()
        setupEventListeners()

        mSimpleBackgroundManager = SimpleBackgroundManager(activity)
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

        for (i in 1..10) {
            val movie = Movie(
                    i,
                    "title $i",
                    "studio $i",
                    "http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce enim felis, accumsan eget eros auctor, condimentum commodo arcu. Etiam vel quam ac urna ullamcorper molestie sit amet eget ex. Maecenas tincidunt scelerisque ultrices. Vestibulum odio sem, euismod eu lectus nec, congue mollis mi. Fusce vitae gravida lectus, eget vehicula est. Vivamus tempus vitae dui ac dapibus. Nulla bibendum eleifend ultrices. Morbi efficitur tellus nisi, a congue magna molestie quis. Mauris ornare justo sapien, vitae sodales sem pulvinar ut. Aliquam ut pharetra justo. Quisque sit amet tortor vel massa mollis euismod et et sem. Cras facilisis nibh libero, non eleifend justo venenatis vel. Etiam tempus sem lacus, a sodales ante congue ac. Mauris ut dolor sit amet nibh aliquet dapibus. Praesent et tellus at libero commodo efficitur."

            )
            cardRowAdapter.add(movie)
        }

        mRowsAdapter.add(ListRow(cardPresenterHeader, cardRowAdapter))
    }

    private fun setupEventListeners() {
        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewSelectedListener: OnItemViewSelectedListener {
        override fun onItemSelected(itemViewHolder: Presenter.ViewHolder?, item: Any?,
                                    rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
            if (item is String) {
                mSimpleBackgroundManager.clearBackground()
            } else if (item is Movie) {
                mSimpleBackgroundManager.updateBackground(activity.getDrawable(R.drawable.movie_background))
            }
        }
    }

    private inner class ItemViewClickedListener: OnItemViewClickedListener {
        override fun onItemClicked(itemViewHolder: Presenter.ViewHolder?, item: Any?,
                                   rowViewHolder: RowPresenter.ViewHolder?, row: Row?) {
            if (item is Movie) {
                Log.d(TAG, "Item $item")

                val intent = VideoDetailsFragment.newIntent(activity, item)

                activity.startActivity(intent)
            }
        }
    }
}
