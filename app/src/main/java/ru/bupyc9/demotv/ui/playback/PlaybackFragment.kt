package ru.bupyc9.demotv.ui.playback

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.PlaybackFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.presenter.DetailsDescriptionPresenter

class PlaybackFragment : PlaybackFragment() {
    companion object {
        @JvmStatic private val TAG = PlaybackFragment::class.java.simpleName
        @JvmStatic private val MOVIE = "movie"

        @JvmStatic fun newIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, PlaybackActivity::class.java)

            intent.putExtra(MOVIE, movie)

            return intent
        }
    }

    private lateinit var mSelectedMovie: Movie
    private lateinit var mPlaybackControlsRow: PlaybackControlsRow
    private lateinit var mPrimaryActionAdapter: ArrayObjectAdapter
    private lateinit var mSecondaryActionAdapter: ArrayObjectAdapter

    private lateinit var mPlayPauseAction: PlaybackControlsRow.PlayPauseAction
    private lateinit var mRepeatAction: PlaybackControlsRow.RepeatAction
    private lateinit var mThumbsUpAction: PlaybackControlsRow.ThumbsUpAction
    private lateinit var mThumbsDownAction: PlaybackControlsRow.ThumbsDownAction
    private lateinit var mShuffleAction: PlaybackControlsRow.ShuffleAction
    private lateinit var mSkipNextAction: PlaybackControlsRow.SkipNextAction
    private lateinit var mSkipPreviousAction: PlaybackControlsRow.SkipPreviousAction
    private lateinit var mFastForwardAction: PlaybackControlsRow.FastForwardAction
    private lateinit var mRewindAction: PlaybackControlsRow.RewindAction
    private lateinit var mHighQualityAction: PlaybackControlsRow.HighQualityAction
    private lateinit var mClosedCaptioningAction: PlaybackControlsRow.ClosedCaptioningAction
    private lateinit var mMoreActions: PlaybackControlsRow.MoreActions

    private lateinit var mRowsAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        mSelectedMovie = activity.intent.getParcelableExtra(MOVIE)
        backgroundType = PlaybackFragment.BG_LIGHT
        isFadingEnabled = true

        setUpRows()
    }

    private fun setUpRows() {
        val presenterSelector = ClassPresenterSelector()
        val playbackControlsRowPresenter = PlaybackControlsRowPresenter(DetailsDescriptionPresenter())

        presenterSelector.addClassPresenter(PlaybackControlsRow::class.java, playbackControlsRowPresenter)
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        mRowsAdapter = ArrayObjectAdapter(presenterSelector)

        addPlaybackControlsRow()
        addOtherRows()

        adapter = mRowsAdapter
    }

    private fun addPlaybackControlsRow() {
        mPlaybackControlsRow = PlaybackControlsRow(mSelectedMovie)
        mRowsAdapter.add(mPlaybackControlsRow)

        val presenterSelector = ControlButtonPresenterSelector()
        mPrimaryActionAdapter = ArrayObjectAdapter(presenterSelector)
        mSecondaryActionAdapter = ArrayObjectAdapter(presenterSelector)
        mPlaybackControlsRow.primaryActionsAdapter = mPrimaryActionAdapter
        mPlaybackControlsRow.secondaryActionsAdapter = mSecondaryActionAdapter

        mPlayPauseAction = PlaybackControlsRow.PlayPauseAction(activity)
        mRepeatAction = PlaybackControlsRow.RepeatAction(activity)
        mThumbsUpAction = PlaybackControlsRow.ThumbsUpAction(activity)
        mThumbsDownAction = PlaybackControlsRow.ThumbsDownAction(activity)
        mShuffleAction = PlaybackControlsRow.ShuffleAction(activity)
        mSkipNextAction = PlaybackControlsRow.SkipNextAction(activity)
        mSkipPreviousAction = PlaybackControlsRow.SkipPreviousAction(activity)
        mFastForwardAction = PlaybackControlsRow.FastForwardAction(activity)
        mRewindAction = PlaybackControlsRow.RewindAction(activity)
        mHighQualityAction = PlaybackControlsRow.HighQualityAction(activity)
        mClosedCaptioningAction = PlaybackControlsRow.ClosedCaptioningAction(activity)
        mMoreActions = PlaybackControlsRow.MoreActions(activity)

        mPrimaryActionAdapter.add(mSkipPreviousAction)
        mPrimaryActionAdapter.add(mRewindAction)
        mPrimaryActionAdapter.add(mPlayPauseAction)
        mPrimaryActionAdapter.add(mFastForwardAction)
        mPrimaryActionAdapter.add(mSkipNextAction)

        mSecondaryActionAdapter.add(mThumbsUpAction)
        mSecondaryActionAdapter.add(mThumbsDownAction)
        mSecondaryActionAdapter.add(mRepeatAction)
        mSecondaryActionAdapter.add(mShuffleAction)
        mSecondaryActionAdapter.add(mHighQualityAction)
        mSecondaryActionAdapter.add(mClosedCaptioningAction)
        mSecondaryActionAdapter.add(mMoreActions)
    }

    private fun addOtherRows() {
        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        val movie = Movie(
                1,
                "Title",
                "Studio",
                "http://heimkehrend.raindrop.jp/kl-hacker/wp-content/uploads/2014/08/DSC02580.jpg",
                "Description"
        )

        listRowAdapter.add(movie)
        listRowAdapter.add(movie)

        val header = HeaderItem(0, "OtherRows")
        mRowsAdapter.add(ListRow(header, listRowAdapter))
    }
}