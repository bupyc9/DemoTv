package ru.bupyc9.demotv

import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.v17.leanback.app.BackgroundManager
import android.util.DisplayMetrics

class SimpleBackgroundManager(private val mActivity: Activity) {
    private val TAG = SimpleBackgroundManager::class.java.simpleName

    companion object {
        const val DEFAULT_BACKGROUND_RES_ID = R.drawable.default_background
    }

    private val mDefaultBackground: Drawable
    private val mBackgroundManager: BackgroundManager

    init {
        mDefaultBackground = mActivity.getDrawable(DEFAULT_BACKGROUND_RES_ID)
        mBackgroundManager = BackgroundManager.getInstance(mActivity)
        mBackgroundManager.attach(mActivity.window)
        mActivity.windowManager.defaultDisplay.getMetrics(DisplayMetrics())
    }

    fun updateBackground(drawable: Drawable) {
        mBackgroundManager.drawable = drawable
    }

    fun clearBackground() {
        mBackgroundManager.drawable = mDefaultBackground
    }
}