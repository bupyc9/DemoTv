package ru.bupyc9.demotv.ui.search

import android.Manifest
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.os.Handler
import android.support.v17.leanback.app.SearchFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import ru.bupyc9.demotv.MovieProvider
import ru.bupyc9.demotv.hasPermission
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.ui.detail.VideoDetailsFragment
import java.util.*
import android.text.TextUtils



class SearchFragment: SearchFragment(), SearchFragment.SearchResultProvider {
    companion object {
        @JvmStatic private val TAG = SearchFragment::class.java.simpleName
        @JvmStatic private val REQUEST_SPEECH = 0x00000010
        @JvmStatic private val SEARCH_DELAY_MS = 1000L
    }
    private lateinit var mRowsAdapter: ArrayObjectAdapter
    private val mHandler = Handler()
    private val mDelayedLoad = Runnable { loadRows() }
    private lateinit var mQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        setSearchResultProvider(this)

        setOnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is Movie) {
                val intent = VideoDetailsFragment.newIntent(activity, item)

                activity.startActivity(intent)
            }
        }

        if (!hasPermission(activity, Manifest.permission.RECORD_AUDIO)) {
            setSpeechRecognitionCallback {
                Log.v(TAG, "recognizeSpeech")

                try {
                    startActivityForResult(recognizerIntent, REQUEST_SPEECH)
                } catch (e: ActivityNotFoundException) {
                    Log.e(TAG, "Cannot find activity for speech recognizer", e)
                }
            }
        }
    }

    override fun getResultsAdapter(): ObjectAdapter {
        Log.d(TAG, "getResultsAdapter");

        return mRowsAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i(TAG, "Search Query Text Submit $query");
        loadQueryWithDelay(query!!, 0)

        return true;
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        Log.i(TAG, "Search Query Text Change $newQuery");
        loadQueryWithDelay(newQuery!!, SEARCH_DELAY_MS)

        return true;
    }

    private fun loadRows() {
        Log.d(TAG, "loadRows")

        mRowsAdapter.clear()
        val items = mutableListOf<Movie>()

        MovieProvider.instance.getItems().forEach {
            if (
            it.title.toLowerCase(Locale.ENGLISH).contains(mQuery.toLowerCase(Locale.ENGLISH))
                    || it.description.toLowerCase(Locale.ENGLISH).contains(mQuery.toLowerCase(Locale.ENGLISH))
                    ) {
                items.add(it)
            }
        }

        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        listRowAdapter.addAll(0, items)
        val header = HeaderItem("Search results")
        mRowsAdapter.add(ListRow(header, listRowAdapter))
    }

    private fun loadQueryWithDelay(query: String, delay: Long) {
        mHandler.removeCallbacks(mDelayedLoad)
        if (!TextUtils.isEmpty(query) && query != "nil") {
            mQuery = query
            mHandler.postDelayed(mDelayedLoad, delay)
        }
    }
}
