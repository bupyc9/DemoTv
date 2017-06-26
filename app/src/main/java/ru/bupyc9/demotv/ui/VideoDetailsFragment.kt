package ru.bupyc9.demotv.ui

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v17.leanback.app.DetailsFragment
import android.util.Log
import ru.bupyc9.demotv.model.Movie
import com.squareup.picasso.Picasso
import android.support.v17.leanback.widget.*
import ru.bupyc9.demotv.DetailsActivity
import ru.bupyc9.demotv.PicassoBackgroundManager
import ru.bupyc9.demotv.convertDpToPixel
import java.io.IOException
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.presenter.CustomFullWidthDetailsOverviewRowPresenter
import ru.bupyc9.demotv.presenter.DetailsDescriptionPresenter


class VideoDetailsFragment: DetailsFragment() {
    companion object {
        @JvmStatic private val DETAIL_THUMB_WIDTH = 274
        @JvmStatic private val DETAIL_THUMB_HEIGHT = 274
        @JvmStatic private val TAG = VideoDetailsFragment::class.java.simpleName
        @JvmStatic private val MOVIE = "movie"

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
            for (i in 0..2) {
                sparseArrayObjectAdapter.set(i, Action(i.toLong(), "label1", "label2"))
            }
            row.actionsAdapter = sparseArrayObjectAdapter

            /* 2nd row: ListRow */
            val listRowAdapter = ArrayObjectAdapter(CardPresenter())
            for (i in 0..9) {
                val movie = Movie(
                        i,
                        "title $i",
                        "studio $i",
                        "http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce enim felis, accumsan eget eros auctor, condimentum commodo arcu. Etiam vel quam ac urna ullamcorper molestie sit amet eget ex. Maecenas tincidunt scelerisque ultrices. Vestibulum odio sem, euismod eu lectus nec, congue mollis mi. Fusce vitae gravida lectus, eget vehicula est. Vivamus tempus vitae dui ac dapibus. Nulla bibendum eleifend ultrices. Morbi efficitur tellus nisi, a congue magna molestie quis. Mauris ornare justo sapien, vitae sodales sem pulvinar ut. Aliquam ut pharetra justo. Quisque sit amet tortor vel massa mollis euismod et et sem. Cras facilisis nibh libero, non eleifend justo venenatis vel. Etiam tempus sem lacus, a sodales ante congue ac. Mauris ut dolor sit amet nibh aliquet dapibus. Praesent et tellus at libero commodo efficitur."
                )

                listRowAdapter.add(movie)
            }
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