package uk.com.a1ms.ui.uiutil;

import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by priju.jacobpaul on 26/07/16.
 */
public class BottomSheetHelper {

    private final static String BOTTOMSHEET_TAG = "bottomsheet";

    public static void showBottomSheet(FragmentManager fragmentManager, BottomSheetDialogFragment bottomsheet) {
        bottomsheet.show(fragmentManager, BOTTOMSHEET_TAG);
    }

    public static void dismiss(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(BOTTOMSHEET_TAG);
        if (fragment instanceof BottomSheetDialogFragment)
            ((BottomSheetDialogFragment) fragment).dismiss();
    }
}
