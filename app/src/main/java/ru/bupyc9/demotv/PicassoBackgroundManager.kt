package ru.bupyc9.demotv

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.support.v17.leanback.app.BackgroundManager
import android.util.DisplayMetrics
import android.util.Log

import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import java.net.URI
import java.net.URISyntaxException
import java.util.Timer
import java.util.TimerTask


class PicassoBackgroundManager(private val mActivity: Activity) {
    companion object {
        @JvmStatic private val TAG = PicassoBackgroundManager::class.java.simpleName
        @JvmStatic private val BACKGROUND_UPDATE_DELAY = 500
        @JvmStatic private lateinit var mDefaultBackground: Drawable
    }

    private val DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background
    // Handler attached with main thread
    private val mHandler = Handler(Looper.getMainLooper())
    private val mBackgroundManager = BackgroundManager.getInstance(mActivity)
    private val mMetrics: DisplayMetrics
    private var mBackgroundURI: URI? = null
    private val mBackgroundTarget: PicassoBackgroundManagerTarget

    internal var mBackgroundTimer: Timer? = null // null when no UpdateBackgroundTask is running.

    init {
        mDefaultBackground = mActivity.getDrawable(DEFAULT_BACKGROUND_RES_ID)
        mBackgroundManager.attach(mActivity.window)
        mBackgroundTarget = PicassoBackgroundManagerTarget(mBackgroundManager)
        mMetrics = DisplayMetrics()
        mActivity.windowManager.defaultDisplay.getMetrics(mMetrics)

    }

    /**
     * if UpdateBackgroundTask is already running, cancel this task and start new task.
     */
    private fun startBackgroundTimer() {
        if (mBackgroundTimer != null) {
            mBackgroundTimer!!.cancel()
        }
        mBackgroundTimer = Timer()
        /* set delay time to reduce too much background image loading process */
        mBackgroundTimer!!.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
    }


    private inner class UpdateBackgroundTask : TimerTask() {
        override fun run() {
            /* Here is TimerTask thread, not UI thread */
            mHandler.post {
                /* Here is main (UI) thread */
                updateBackground(mBackgroundURI!!)
            }
        }
    }

    fun updateBackgroundWithDelay(url: String) {
        try {
            val uri = URI(url)
            updateBackgroundWithDelay(uri)
        } catch (e: URISyntaxException) {
            /* skip updating background */
            Log.e(TAG, e.toString())
        }

    }

    /**
     * updateBackground with delay
     * delay time is measured in other Timer task thread.
     * @param uri
     */
    fun updateBackgroundWithDelay(uri: URI) {
        mBackgroundURI = uri
        startBackgroundTimer()
    }

    private fun updateBackground(uri: URI) {
        try {
            Picasso.with(mActivity)
                    .load(uri.toString())
                    .resize(mMetrics.widthPixels, mMetrics.heightPixels)
                    .centerCrop()
                    .error(mDefaultBackground)
                    .into(mBackgroundTarget)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

    /**
     * Copied from AOSP sample code.
     * Inner class
     * Picasso target for updating default_background images
     */
    inner class PicassoBackgroundManagerTarget(internal var mBackgroundManager: BackgroundManager) : Target {

        override fun onBitmapLoaded(bitmap: Bitmap, loadedFrom: Picasso.LoadedFrom) {
            this.mBackgroundManager.setBitmap(bitmap)
        }

        override fun onBitmapFailed(drawable: Drawable) {
            this.mBackgroundManager.drawable = drawable
        }

        override fun onPrepareLoad(drawable: Drawable) {
            // Do nothing, default_background manager has its own transitions
        }

        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (other == null || javaClass != other.javaClass)
                return false

            val that = other as PicassoBackgroundManagerTarget?

            if (mBackgroundManager != that!!.mBackgroundManager)
                return false

            return true
        }

        override fun hashCode(): Int {
            return mBackgroundManager.hashCode()
        }
    }
}