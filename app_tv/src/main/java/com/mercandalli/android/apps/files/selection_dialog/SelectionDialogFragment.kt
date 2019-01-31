@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.apps.files.selection_dialog

import android.os.Bundle
import android.widget.Toast
import androidx.leanback.app.GuidedStepFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction

import com.mercandalli.android.apps.files.R

class SelectionDialogFragment : GuidedStepFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        return GuidanceStylist.Guidance(getString(R.string.app_name),
                getString(R.string.app_name),
                "", null)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        var action = GuidedAction.Builder()
                .id(ACTION_ID_POSITIVE.toLong())
                .title(getString(R.string.app_name)).build()
        actions.add(action)
        action = GuidedAction.Builder()
                .id(ACTION_ID_NEGATIVE.toLong())
                .title(getString(R.string.app_name)).build()
        actions.add(action)
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        if (ACTION_ID_POSITIVE.toLong() == action!!.id) {
            Toast.makeText(activity, R.string.app_name,
                    Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, R.string.app_name,
                    Toast.LENGTH_SHORT).show()
        }
        activity.finish()
    }

    companion object {

        private const val ACTION_ID_POSITIVE = 1
        private const val ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1
    }
}
