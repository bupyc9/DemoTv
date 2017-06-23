package ru.bupyc9.demotv.presenter

import android.graphics.Color
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import ru.bupyc9.demotv.R

class GridItemPresenter: Presenter() {
    private val GRID_ITEM_WIDTH: Int = 300
    private val GRID_ITEM_HEIGHT: Int = 200

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view: TextView = TextView(parent!!.context)
        view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.default_background))
        view.setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        (viewHolder!!.view as TextView).text = item as String
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {

    }
}