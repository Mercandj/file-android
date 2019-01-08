package com.mercandalli.android.apps.files.selection_dialog;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.mercandalli.android.apps.files.R;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class SelectionDialogFragment extends GuidedStepFragment {

    private static final int ACTION_ID_POSITIVE = 1;
    private static final int ACTION_ID_NEGATIVE = ACTION_ID_POSITIVE + 1;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        GuidanceStylist.Guidance guidance = new GuidanceStylist.Guidance(getString(R.string.app_name),
                getString(R.string.app_name),
                "", null);
        return guidance;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder()
                .id(ACTION_ID_POSITIVE)
                .title(getString(R.string.app_name)).build();
        actions.add(action);
        action = new GuidedAction.Builder()
                .id(ACTION_ID_NEGATIVE)
                .title(getString(R.string.app_name)).build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (ACTION_ID_POSITIVE == action.getId()) {
            Toast.makeText(getActivity(), R.string.app_name,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.app_name,
                    Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }
}
