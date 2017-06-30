package ru.bupyc9.demotv.ui.playback

import android.media.session.PlaybackState
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v17.leanback.app.PlaybackFragment
import android.support.v17.leanback.widget.*
import android.util.Log
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.presenter.CardPresenter
import ru.bupyc9.demotv.presenter.DetailsDescriptionPresenter
import android.support.v17.leanback.widget.ArrayObjectAdapter
import ru.bupyc9.demotv.getDuration
import android.support.v17.leanback.widget.PlaybackControlsRow

class PlaybackFragment : PlaybackFragment() {
    companion object {
        @JvmStatic private val TAG = PlaybackFragment::class.java.simpleName
        @JvmStatic private val SIMULATED_BUFFERED_TIME = 10000
        @JvmStatic private val DEFAULT_UPDATE_PERIOD = 1000
        @JvmStatic private val UPDATE_PERIOD = 16
    }

    private lateinit var mSelectedMovie: Movie
    private lateinit var mPlaybackControlsRow: PlaybackControlsRow
    private lateinit var mPrimaryActionsAdapter: ArrayObjectAdapter
    private lateinit var mSecondaryActionsAdapter: ArrayObjectAdapter
    private var mCurrentPlaybackState: Int = 0
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null

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

    private var mCurrentItem = 0
    private var mItems = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        mSelectedMovie = PlaybackActivity.getMovie(activity.intent)
        backgroundType = PlaybackFragment.BG_LIGHT
        isFadingEnabled = true

        mHandler = Handler(Looper.getMainLooper())

        mCurrentItem = mSelectedMovie.id - 1

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

        playbackControlsRowPresenter.setOnActionClickedListener { action ->
            if (action.id == mPlayPauseAction.id) {
                togglePlayback(mPlayPauseAction.index == PlaybackControlsRow.PlayPauseAction.PLAY)
            } else if (action.id == mSkipNextAction.id) {
                next(mCurrentPlaybackState == PlaybackState.STATE_PLAYING)
            } else if (action.id == mSkipPreviousAction.id) {
                prev(mCurrentPlaybackState == PlaybackState.STATE_PLAYING)
            } else if (action.id == mFastForwardAction.id) {
                fastForward()
            } else if (action.id == mRewindAction.id) {
                rewind()
            }

            if (action is PlaybackControlsRow.MultiAction) {
                notifyChanged(action)

                if (action is PlaybackControlsRow.ThumbsUpAction ||
                        action is PlaybackControlsRow.ThumbsDownAction ||
                        action is PlaybackControlsRow.RepeatAction ||
                        action is PlaybackControlsRow.ShuffleAction ||
                        action is PlaybackControlsRow.HighQualityAction ||
                        action is PlaybackControlsRow.ClosedCaptioningAction) {
                    action.nextIndex()
                }
            }
        }

        adapter = mRowsAdapter
    }

    private fun notifyChanged(action: Action) {
        var adapter = mPrimaryActionsAdapter
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1)
            return
        }

        adapter = mSecondaryActionsAdapter
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1)
            return
        }
    }

    private fun togglePlayback(playPause: Boolean) {
        (activity as PlaybackActivity).playPause(playPause)
        playbackStateChanged()
    }

    fun playbackStateChanged() {
        if (mCurrentPlaybackState != PlaybackState.STATE_PLAYING) {
            mCurrentPlaybackState = PlaybackState.STATE_PLAYING
            startProgressAutomation()
            isFadingEnabled = true
            mPlayPauseAction.index = PlaybackControlsRow.PlayPauseAction.PAUSE
            mPlayPauseAction.icon = mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE)
            notifyChanged(mPlayPauseAction)
        } else if (mCurrentPlaybackState != PlaybackState.STATE_PAUSED) {
            mCurrentPlaybackState = PlaybackState.STATE_PAUSED
            stopProgressAutomation()
            mPlayPauseAction.index = PlaybackControlsRow.PlayPauseAction.PLAY
            mPlayPauseAction.icon = mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PLAY)
            notifyChanged(mPlayPauseAction)
        }

        val currentTime = (activity as PlaybackActivity).position
        mPlaybackControlsRow.currentTime = currentTime
        mPlaybackControlsRow.bufferedProgress = currentTime + SIMULATED_BUFFERED_TIME
    }

    private fun startProgressAutomation() {
        if (mRunnable == null) {
            mRunnable = object : Runnable {
                override fun run() {
                    val updatePeriod = getUpdatePeriod()
                    val currentTime = mPlaybackControlsRow.currentTime + updatePeriod
                    val totalTime = mPlaybackControlsRow.totalTime

                    mPlaybackControlsRow.currentTime = currentTime
                    mPlaybackControlsRow.bufferedProgress = currentTime + SIMULATED_BUFFERED_TIME

                    if (totalTime in 1..currentTime) {
                        stopProgressAutomation()
                    } else {
                        mHandler?.postDelayed(this, updatePeriod.toLong())
                    }
                }
            }
            mHandler?.postDelayed(mRunnable, getUpdatePeriod().toLong())
        }
    }

    private fun getUpdatePeriod(): Int {
        if (view == null || mPlaybackControlsRow.totalTime <= 0) {
            return DEFAULT_UPDATE_PERIOD
        }
        return Math.max(UPDATE_PERIOD, mPlaybackControlsRow.totalTime / view!!.width)
    }

    private fun stopProgressAutomation() {
        mHandler!!.removeCallbacks(mRunnable)
        mRunnable = null
    }

    private fun addPlaybackControlsRow() {
        mPlaybackControlsRow = PlaybackControlsRow(mSelectedMovie)
        mRowsAdapter.add(mPlaybackControlsRow)

        val presenterSelector = ControlButtonPresenterSelector()
        mPrimaryActionsAdapter = ArrayObjectAdapter(presenterSelector)
        mSecondaryActionsAdapter = ArrayObjectAdapter(presenterSelector)
        mPlaybackControlsRow.primaryActionsAdapter = mPrimaryActionsAdapter
        mPlaybackControlsRow.secondaryActionsAdapter = mSecondaryActionsAdapter

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

        mPrimaryActionsAdapter.add(mSkipPreviousAction)
        mPrimaryActionsAdapter.add(mRewindAction)
        mPrimaryActionsAdapter.add(mPlayPauseAction)
        mPrimaryActionsAdapter.add(mFastForwardAction)
        mPrimaryActionsAdapter.add(mSkipNextAction)

        mSecondaryActionsAdapter.add(mThumbsUpAction)
        mSecondaryActionsAdapter.add(mThumbsDownAction)
        mSecondaryActionsAdapter.add(mRepeatAction)
        mSecondaryActionsAdapter.add(mShuffleAction)
        mSecondaryActionsAdapter.add(mHighQualityAction)
        mSecondaryActionsAdapter.add(mClosedCaptioningAction)
        mSecondaryActionsAdapter.add(mMoreActions)
    }

    private fun addOtherRows() {
        if (mItems.size == 0) {
            return
        }

        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        mItems.forEach { listRowAdapter.add(it) }

        val header = HeaderItem(0, "OtherRows")
        mRowsAdapter.add(ListRow(header, listRowAdapter))
    }

    private fun fastForward() {
        (activity as PlaybackActivity).fastForward()

        val currentTime = (activity as PlaybackActivity).position
        mPlaybackControlsRow.currentTime = currentTime
        mPlaybackControlsRow.bufferedProgress = currentTime + SIMULATED_BUFFERED_TIME
    }

    private fun rewind() {
        (activity as PlaybackActivity).rewind()

        /* UI part */
        val currentTime = (activity as PlaybackActivity).position
        mPlaybackControlsRow.currentTime = currentTime
        mPlaybackControlsRow.bufferedProgress = currentTime + SIMULATED_BUFFERED_TIME
    }

    private fun next(autoPlay: Boolean) {
        if (++mCurrentItem >= mItems.size) {
            mCurrentItem = 0
        }

        if (autoPlay) {
            mCurrentPlaybackState = PlaybackState.STATE_PAUSED
        }

        val movie = mItems[mCurrentItem]
        if (movie != null) {
            val activity = activity as PlaybackActivity
            activity.setVideoPath(movie.videoUrl)
            activity.playbackState = PlaybackActivity.LeanbackPlaybackState.PAUSED
            activity.playPause(autoPlay)
        }

        /* UI part */
        playbackStateChanged()
        updatePlaybackRow(mCurrentItem)
    }

    private fun prev(autoPlay: Boolean) {
        if (--mCurrentItem < 0) {
            mCurrentItem = mItems.size - 1
        }
        if (autoPlay) {
            mCurrentPlaybackState = PlaybackState.STATE_PAUSED
        }

        val movie = mItems[mCurrentItem]
        if (movie != null) {
            val activity = activity as PlaybackActivity
            activity.setVideoPath(movie.videoUrl)
            activity.playbackState = PlaybackActivity.LeanbackPlaybackState.PAUSED
            activity.playPause(autoPlay)
        }

        playbackStateChanged()
        updatePlaybackRow(mCurrentItem)
    }

    private fun updatePlaybackRow(index: Int) {
        Log.d(TAG, "updatePlaybackRow")

        if (mPlaybackControlsRow.item != null) {
            val item = mPlaybackControlsRow.item as Movie
            item.title = mItems[index].title
            item.studio = mItems[index].studio

            mRowsAdapter.notifyArrayItemRangeChanged(0, 1)
            val duration = getDuration(mItems.get(index).videoUrl)
            Log.i(TAG, "videoUrl: " + mItems.get(index).videoUrl)
            Log.i(TAG, "duration = " + duration)

            mPlaybackControlsRow.totalTime = duration.toInt()
            mPlaybackControlsRow.currentTime = 0
            mPlaybackControlsRow.bufferedProgress = 0
        }
    }
}