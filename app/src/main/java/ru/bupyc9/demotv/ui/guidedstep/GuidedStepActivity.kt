package ru.bupyc9.demotv.ui.guidedstep

import android.app.Activity
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.util.Log
import ru.bupyc9.demotv.R

class GuidedStepActivity: Activity() {
    companion object {
        @JvmStatic private val TAG = GuidedStepActivity::class.java.simpleName

        @JvmStatic private val ACTION_CONTINUE = 0L
        @JvmStatic private val ACTION_BACK = 1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")

        if (null == savedInstanceState) {
            GuidedStepFragment.add(fragmentManager, FirstStepFragment())
        }
    }

    class FirstStepFragment: GuidedStepFragment() {
        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = "Title"
            val breadcrumb = "Breadcrumb"
            val description = "Description"
            val icon = activity.getDrawable(R.drawable.ic_main_icon)

            return GuidanceStylist.Guidance(title, description, breadcrumb, icon)
        }

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            actions.add(GuidedAction.Builder(activity)
                    .id(ACTION_CONTINUE)
                    .title("Continue")
                    .description("Go to SecondStepFragment")
                    .build())

            actions.add(GuidedAction.Builder(activity)
                    .id(ACTION_BACK)
                    .title("Cancel")
                    .description("Go back")
                    .build())
        }

        override fun onGuidedActionClicked(action: GuidedAction?) {
            if (action?.id == ACTION_CONTINUE) {

            } else if (action?.id == ACTION_BACK) {
                activity.finish()
            } else {
                Log.w(TAG, "Action is not defined")
            }
        }
    }
}