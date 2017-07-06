package ru.bupyc9.demotv.recommendation

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.bupyc9.demotv.MovieProvider
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.model.Movie
import ru.bupyc9.demotv.ui.detail.DetailsActivity
import ru.bupyc9.demotv.ui.detail.VideoDetailsFragment

class UpdateRecommendationsService: IntentService("RecommendationService") {
    companion object {
        @JvmStatic private val TAG = UpdateRecommendationsService::class.java.simpleName
        @JvmStatic private val MAX_RECOMMENDATIONS = 3
    }

    private lateinit var mNotificationManager: NotificationManager

    override fun onCreate() {
        Log.d(TAG, "onCreate")

        super.onCreate()

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "Updating recommendation cards")

        val builder = RecommendationBuilder(applicationContext).setSmallIcon(R.drawable.videos_by_google_icon)
        var count = 0

        MovieProvider.instance.getItems().forEach {
            Log.d(TAG, "Recommendation - ${it.title}")

            builder
                    .setId(it.id)
                    .setBackground(it.imageUrl)
                    .setLargeIcon(it.imageUrl)
                    .setPriority(MAX_RECOMMENDATIONS - count)
                    .setTitle(it.title)
                    .setDescription(it.description)
                    .setSmallIcon(R.drawable.lb_text_dot_one_small)
                    .setIntent(buildPendingIntent(it))

            mNotificationManager.notify(it.id, builder.build())

            if (++count >= MAX_RECOMMENDATIONS) {
                return
            }
        }
    }

    private fun buildPendingIntent(movie: Movie): PendingIntent {
        val detailsIntent = VideoDetailsFragment.newIntent(this, movie)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(DetailsActivity::class.java)
        stackBuilder.addNextIntent(detailsIntent)
        detailsIntent.action = movie.id.toString()

        val intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        return intent
    }
}