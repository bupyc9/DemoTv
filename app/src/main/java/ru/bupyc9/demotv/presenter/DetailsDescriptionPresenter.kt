package ru.bupyc9.demotv.presenter

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter
import android.util.Log
import ru.bupyc9.demotv.model.Movie

class DetailsDescriptionPresenter: AbstractDetailsDescriptionPresenter() {
    private val TAG = DetailsDescriptionPresenter::class.java.simpleName

    override fun onBindDescription(vh: ViewHolder?, item: Any?) {
        val movie = item as Movie

        vh!!.title.text = movie.title
        vh.subtitle.text = movie.studio
        vh.body.text = movie.description

        Log.d(TAG, item.toString())
    }
}