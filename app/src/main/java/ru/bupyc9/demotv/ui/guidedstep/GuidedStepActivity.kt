package ru.bupyc9.demotv.ui.guidedstep

import android.app.Activity
import android.os.Bundle
import android.support.v17.leanback.app.GuidedStepFragment
import android.support.v17.leanback.widget.GuidanceStylist
import android.support.v17.leanback.widget.GuidedAction
import android.util.Log
import ru.bupyc9.demotv.R
import ru.bupyc9.demotv.showToast

class GuidedStepActivity : Activity() {
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

    class FirstStepFragment : GuidedStepFragment() {
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
                GuidedStepFragment.add(fragmentManager, SecondStepFragment())
            } else if (action?.id == ACTION_BACK) {
                activity.finish()
            } else {
                Log.w(TAG, "Action is not defined")
            }
        }
    }

    class SecondStepFragment : GuidedStepFragment() {
        companion object {
            @JvmStatic private val OPTION_CHECK_SET_ID = 10

            @JvmStatic private val OPTION_NAMES: Array<String> = arrayOf("Option A", "Option B", "Option C")
            @JvmStatic private val OPTION_DESCRIPTIONS: Array<String> = arrayOf(
                    "Here's one thing you can do",
                    "Here's another thing you can do",
                    "Here's one more thing you can do"
            )
            @JvmStatic private val OPTION_CHECKED: Array<Boolean> = arrayOf(true, false, false)
        }

        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = "SecondStepFragment"
            val breadcrumb = "Guided Steps: 2"
            val description = "Showcasing different action configurations"
            val icon = activity.getDrawable(R.drawable.ic_main_icon)

            return GuidanceStylist.Guidance(title, description, breadcrumb, icon)
        }

        override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
            val title = "infoOnly action"
            val desc = "infoOnly indicates whether this action is for information purposes only and cannot be clicked.\n "
                    .plus("The description can be long, by set multilineDescription to true")

            actions.add(
                    GuidedAction.Builder(activity)
                            .title(title)
                            .description(desc)
                            .multilineDescription(true)
                            .infoOnly(true)
                            .enabled(false)
                            .build()
            )

            for (i in 0..OPTION_NAMES.size - 1) {
                val action = GuidedAction.Builder(activity)
                        .title(OPTION_NAMES[i])
                        .description(OPTION_DESCRIPTIONS[i])
                        .checkSetId(OPTION_CHECK_SET_ID)
                        .build()

                action.isChecked = OPTION_CHECKED[i]

                actions.add(action)
            }
        }

        override fun onGuidedActionClicked(action: GuidedAction?) {
            val message = OPTION_NAMES[selectedActionPosition - 1].plus(" clicked")
            showToast(activity, message)
        }

        override fun onCreateGuidanceStylist(): GuidanceStylist {
            return object: GuidanceStylist() {
                override fun onProvideLayoutId(): Int {
                    return R.layout.guidedstep_second_guidance
                }
            }
        }
    }
}