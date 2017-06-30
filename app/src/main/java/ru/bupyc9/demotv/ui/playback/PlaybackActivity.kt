package ru.bupyc9.demotv.ui.playback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.getDuration


class PlaybackActivity : Activity() {
    companion object {
        @JvmStatic private val TAG = PlaybackActivity::class.java.simpleName
        @JvmStatic private val MOVIE = "movie"

        @JvmStatic fun newIntent(context: Context, movie: Movie): Intent {
            val intent = Intent(context, PlaybackActivity::class.java)

            intent.putExtra(MOVIE, movie)

            return intent
        }

        @JvmStatic fun getMovie(intent: Intent): Movie {
            return intent.getParcelableExtra(MOVIE)
        }
    }

    private lateinit var mVideoView: VideoView

    var playbackState = LeanbackPlaybackState.IDLE

    var position = 0

    private var mStartTimeMillis = 0L
    private var mDuration = -1L

    enum class LeanbackPlaybackState {
        PLAYING, PAUSED, IDLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_playback)

        loadViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
        mVideoView.suspend()
        mVideoView.setVideoURI(null)
    }

    fun loadViews() {
        mVideoView = findViewById(R.id.videoView) as VideoView
        mVideoView.isFocusable = false
        mVideoView.isFocusableInTouchMode = false

        val movie = getMovie(intent)
        setVideoPath(movie.videoUrl)
    }

    fun setVideoPath(videoUrl: String) {
        position = 0
        mVideoView.setVideoPath(videoUrl)
        mStartTimeMillis = 0
        mDuration = getDuration(videoUrl)
    }

    fun stopPlayback() {
        mVideoView.stopPlayback()
    }

    fun playPause(doPlay: Boolean) {
        if (playbackState == LeanbackPlaybackState.IDLE) {
            setupCallbacks()
        }

        if (doPlay && playbackState != LeanbackPlaybackState.PLAYING) {
            playbackState = LeanbackPlaybackState.PLAYING

            if (position > 0) {
                mVideoView.seekTo(position)
            }

            mVideoView.start()
            mStartTimeMillis = System.currentTimeMillis()
        } else {
            playbackState = LeanbackPlaybackState.PAUSED
            val timeElapsedSinceStart: Int = (System.currentTimeMillis() - mStartTimeMillis).toInt()
            position += timeElapsedSinceStart
            mVideoView.pause()
        }
    }

    fun setupCallbacks() {
        mVideoView.setOnErrorListener { mp, what, extra ->
            mVideoView.stopPlayback()
            playbackState = LeanbackPlaybackState.IDLE

            false
        }

        mVideoView.setOnPreparedListener { mp ->
            if (playbackState == LeanbackPlaybackState.PLAYING) {
                mVideoView.start()
            }
        }

        mVideoView.setOnCompletionListener { mp ->
            playbackState = LeanbackPlaybackState.IDLE
        }
    }

    fun fastForward() {
        if (mDuration != -1L) {
            position = mVideoView.currentPosition + 10 * 1000
            mVideoView.seekTo(position)
        }
    }

    fun rewind() {
        position = mVideoView.currentPosition - 10 * 1000
        mVideoView.seekTo(position)
    }
}