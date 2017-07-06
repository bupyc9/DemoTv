package ru.bupyc9.demotv.recommendation

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import ru.bupyc9.demotv.R
import com.squareup.picasso.Picasso

class RecommendationBuilder(private val mContext: Context) {
    companion object {
        @JvmStatic private val TAG = RecommendationBuilder::class.java.simpleName
    }

    private var mId: Int? = null
    private var mPriority: Int? = null
    private var mTitle: String? = null
    private var mDescription: String? = null
    private var mSmallIcon: Int? = null
    private var mLargeIcon: String? = null
    private var mBackgroundUri: String? = null
    private var mIntent: PendingIntent? = null

    fun setId(id: Int): RecommendationBuilder {
        mId = id
        return this
    }

    fun setPriority(priority: Int): RecommendationBuilder {
        mPriority = priority
        return this
    }

    fun setTitle(title: String): RecommendationBuilder {
        mTitle = title
        return this
    }

    fun setDescription(description: String): RecommendationBuilder {
        mDescription = description
        return this
    }

    fun setSmallIcon(resourceId: Int): RecommendationBuilder {
        mSmallIcon = resourceId
        return this
    }

    fun setLargeIcon(uri: String): RecommendationBuilder {
        mLargeIcon = uri
        return this
    }

    fun setIntent(intent: PendingIntent): RecommendationBuilder {
        mIntent = intent
        return this
    }

    fun setBackground(uri: String): RecommendationBuilder {
        mBackgroundUri = uri
        return this
    }

    fun build(): Notification {
        val extras = Bundle()
        if (mBackgroundUri != null) {
            extras.putString(Notification.EXTRA_BACKGROUND_IMAGE_URI, mBackgroundUri);
        }

        val image = Picasso.with(mContext).load(mLargeIcon).get()

        val notification = NotificationCompat.BigPictureStyle(
                NotificationCompat.Builder(mContext)
                        .setAutoCancel(true)
                        .setContentTitle(mTitle)
                        .setContentText(mDescription)
                        .setPriority(mPriority!!)
                        .setLocalOnly(true)
                        .setOngoing(true)
                        .setColor(ContextCompat.getColor(mContext, R.color.fastlane_background))
                        .setCategory(Notification.CATEGORY_RECOMMENDATION)
                        .setLargeIcon(image)
                        .setSmallIcon(mSmallIcon!!)
                        .setContentIntent(mIntent)
                        .setExtras(extras)
        )

        Log.d(TAG, "Building notification ${this.toString()}")

        return notification.build()
    }

    override fun toString(): String {
        return "RecommendationBuilder{mId=${mId}, mPriority=${mPriority}, mSmallIcon=${mSmallIcon}"
                .plus(", mTitle=${mTitle}}, mDescription=${mDescription}, mLargeIcon=${mLargeIcon}")
                .plus(", mIntent=${mIntent}}")
    }
}