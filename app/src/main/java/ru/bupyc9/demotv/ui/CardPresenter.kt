package ru.bupyc9.demotv.ui

import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.model.Movie

class CardPresenter: Presenter() {
    private val TAG = CardPresenter::class.java.simpleName!!

    private val CARD_WIDTH: Int = 313
    private val CARD_HEIGHT: Int = 176

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        val cardView = ImageCardView(parent!!.context)

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        cardView.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.fastlane_background))

        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
        val movie = item as Movie
        val holder = viewHolder as ViewHolder

        holder.cardView.titleText = movie.title
        holder.cardView.contentText = movie.studio
        holder.cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        holder.cardView.mainImage = holder.defaultCardImage
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {
        Log.d(TAG, "onUnbindViewHolder")
    }

    class ViewHolder(view: View) : Presenter.ViewHolder(view) {
        val cardView: ImageCardView = view as ImageCardView
        val defaultCardImage: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.movie)
    }
}