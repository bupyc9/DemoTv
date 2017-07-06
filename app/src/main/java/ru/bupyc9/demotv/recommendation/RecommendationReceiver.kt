package ru.bupyc9.demotv.recommendation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RecommendationReceiver : BroadcastReceiver() {
    companion object {
        @JvmStatic private val TAG = RecommendationReceiver::class.java.simpleName
        @JvmStatic private val INITIAL_DELAY: Long = 5000
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "RecommendationReceiver initiated")

        if (intent!!.action.endsWith(Intent.ACTION_BOOT_COMPLETED)) {
            scheduleRecommendationUpdate(context)
        }
    }

    private fun scheduleRecommendationUpdate(context: Context?) {
        Log.d(TAG, "Scheduling recommendations update")

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val recommendationIntent = Intent(context, UpdateRecommendationsService::class.java)
        val alarmIntent = PendingIntent.getService(context, 0, recommendationIntent, 0)

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                INITIAL_DELAY,
                AlarmManager.INTERVAL_HALF_HOUR,
                alarmIntent
        )
    }
}