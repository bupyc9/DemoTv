package ru.bupyc9.demotv.presenter

import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter
import android.support.v17.leanback.widget.Presenter
import android.support.v17.leanback.widget.RowPresenter
import android.util.Log

class CustomFullWidthDetailsOverviewRowPresenter(val presenter: Presenter): FullWidthDetailsOverviewRowPresenter(presenter) {
    private val TAG = CustomFullWidthDetailsOverviewRowPresenter::class.java.simpleName

    override fun onRowViewAttachedToWindow(vh: RowPresenter.ViewHolder?) {
        super.onRowViewAttachedToWindow(vh)
        Log.v(TAG, "onRowViewAttachedToWindow")
    }

    override fun onBindRowViewHolder(holder: RowPresenter.ViewHolder?, item: Any?) {
        super.onBindRowViewHolder(holder, item)
        Log.v(TAG, "onBindRowViewHolder")
    }

    override fun onLayoutOverviewFrame(viewHolder: ViewHolder?, oldState: Int, logoChanged: Boolean) {
        super.onLayoutOverviewFrame(viewHolder, oldState, logoChanged)
        Log.v(TAG, "onLayoutOverviewFrame")

        setState(viewHolder, FullWidthDetailsOverviewRowPresenter.STATE_HALF)
    }
}