package uk.com.a1ms.ui.uiutil;

import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

/**
 * Created by priju.jacobpaul on 26/07/16.
 */
public class BaseBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    private OnActionCallback actionCallback;

    public void setActionCallback(OnActionCallback onActionCallback) {
        this.actionCallback = onActionCallback;
    }

    @Override
    public void onClick(View view) {
        if (actionCallback != null) {
            actionCallback.onAction(view.getId());
        }
    }

    @Override
    public void onDetach() {
        actionCallback = null;
        super.onDetach();
    }
}
