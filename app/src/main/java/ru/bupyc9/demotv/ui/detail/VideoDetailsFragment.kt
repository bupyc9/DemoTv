package ru.bupyc9.demotv.ui.detail

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v17.leanback.app.DetailsFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import com.squareup.picasso.Picasso
import ru.bupyc9.demotv.MovieProvider
import ru.bupyc9.demotv.PicassoBackgroundManager
import ru.bupyc9.demotv.convertDpToPixel
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.presenter.CustomFullWidthDetailsOverviewRowPresenter
import ru.bupyc9.demotv.presenter.DetailsDescriptionPresenter
import ru.bupyc9.demotv.ui.playback.PlaybackActivity
import java.io.IOException


class VideoDetailsFragment: DetailsFragment() {
    companion object {
        @JvmStatic private val DETAIL_THUMB_WIDTH = 274
        @JvmStatic private val DETAIL_THUMB_HEIGHT = 274
        @JvmStatic private val TAG = VideoDetailsFragment::class.java.simpleName
        @JvmStatic private val MOVIE = "movie"
        @JvmStatic private val ACTION_PLAY_VIDEO = 1L

        @JvmStatic fun newIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, DetailsActivity::class.java)

            intent.putExtra(MOVIE, movie)

            return intent
        }
    }

    private lateinit var mFwdorPresenter: CustomFullWidthDetailsOverviewRowPresenter
    private lateinit var mPicassoBackgroundManager: PicassoBackgroundManager

    private lateinit var mSelectedMovie: Movie
    private lateinit var mDetailsRowBuilderTask: DetailsRowBuilderTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")

        mFwdorPresenter = CustomFullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        mSelectedMovie = activity.intent.getParcelableExtra(MOVIE)

        mDetailsRowBuilderTask = DetailsRowBuilderTask().execute(mSelectedMovie) as DetailsRowBuilderTask

        mPicassoBackgroundManager = PicassoBackgroundManager(activity)
        mPicassoBackgroundManager.updateBackgroundWithDelay(mSelectedMovie.getImageURI()!!)
    }

    override fun onStop() {
        super.onStop()
        mDetailsRowBuilderTask.cancel(true)
    }

    private inner class DetailsRowBuilderTask: AsyncTask<Movie, Int, DetailsOverviewRow>() {
        override fun doInBackground(vararg params: Movie?): DetailsOverviewRow {
            val row = DetailsOverviewRow(mSelectedMovie)
            try {
                val poster = Picasso.with(activity)
                        .load(mSelectedMovie.imageUrl)
                        .resize(
                                convertDpToPixel(activity.applicationContext, DETAIL_THUMB_WIDTH),
                                convertDpToPixel(activity.applicationContext, DETAIL_THUMB_HEIGHT)
                        )
                        .centerCrop()
                        .get()
                row.setImageBitmap(activity, poster)
            } catch (e: IOException) {
                Log.w(TAG, "background detail: $e")
            }

            return row
        }

        override fun onPostExecute(row: DetailsOverviewRow) {
            val sparseArrayObjectAdapter = SparseArrayObjectAdapter()
            sparseArrayObjectAdapter.set(0, Action(ACTION_PLAY_VIDEO, "Play video"))
            sparseArrayObjectAdapter.set(1, Action(2, "Action 2", "label"))
            sparseArrayObjectAdapter.set(2, Action(3, "Action 3", "label"))

            row.actionsAdapter = sparseArrayObjectAdapter

            mFwdorPresenter.setOnActionClickedListener { action ->
                if (action.id == ACTION_PLAY_VIDEO) {
                    val intent = PlaybackActivity.newIntent(activity, mSelectedMovie)
                    startActivity(intent)
                }
            }

            /* 2nd row: ListRow */
            val listRowAdapter = ArrayObjectAdapter(CardPresenter())
            MovieProvider.instance.getItems().forEach { listRowAdapter.add(it) }
            val headerItem = HeaderItem(0, "Related Videos")

            val classPresenterSelector = ClassPresenterSelector()
            mFwdorPresenter.initialState = FullWidthDetailsOverviewRowPresenter.STATE_HALF
            Log.e(TAG, "mFwdorPresenter.getInitialState: " + mFwdorPresenter.initialState)

            classPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, mFwdorPresenter)
            classPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())

            val adapter = ArrayObjectAdapter(classPresenterSelector)
            /* 1st row */
            adapter.add(row)
            /* 2nd row */
            adapter.add(ListRow(headerItem, listRowAdapter))
            /* 3rd row */
            //adapter.add(new ListRow(headerItem, listRowAdapter));
            setAdapter(adapter)

        }
    }
}