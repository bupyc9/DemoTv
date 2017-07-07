package ru.bupyc9.demotv.ui.search

import android.Manifest
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.support.v17.leanback.app.SearchFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import ru.bupyc9.demotv.MovieProvider
import ru.bupyc9.demotv.hasPermission
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.ui.detail.VideoDetailsFragment

class SearchFragment: SearchFragment(), SearchFragment.SearchResultProvider {
    companion object {

        @JvmStatic private val TAG = SearchFragment::class.java.simpleName
        @JvmStatic private val REQUEST_SPEECH = 0x00000010
    }
    private lateinit var mRowsAdapter: ArrayObjectAdapter

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
        Log.d(TAG, mRowsAdapter.toString());

        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        listRowAdapter.addAll(0, MovieProvider.instance.getItems())
        val header = HeaderItem("Search results")
        mRowsAdapter.add(ListRow(header, listRowAdapter))

        return mRowsAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i(TAG, "Search Query Text Change $query");
        return true;
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        Log.i(TAG, "Search Query Text Submit $newQuery");
        return true;
    }
}
