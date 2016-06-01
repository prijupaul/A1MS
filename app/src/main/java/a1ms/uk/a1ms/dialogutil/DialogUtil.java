package a1ms.uk.a1ms.dialogutil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jialian.sun on 12/22/2014.
 */
public class DialogUtil {

    static IDialogCreator dialogCreator = new DefaultDialogCreator();

    public static void showOKDialog(Context context, String title, String message, String okLabel, DialogCallBackListener dialogCallBackListener, boolean isCancellable) {
        Dialog dialog = dialogCreator.createOKDialog(context, title, message, okLabel, dialogCallBackListener, isCancellable);
        dialog.show();
    }

    public static void showYESNODialog(Context context, String title, String message, String yesLabel, String noLabel, DialogCallBackListener yesCallBackListener, DialogCallBackListener noCallBackListener, boolean isCancellable) {
        Dialog dialog = dialogCreator.createYesNoDialog(context, title, message, yesLabel, noLabel, yesCallBackListener, noCallBackListener, isCancellable);
        dialog.show();
    }

    public static void showOptionDialog(Context context, String title, CharSequence[] items, DialogInterface.OnClickListener callBackListener, DialogInterface.OnCancelListener cancelListener) {
        Dialog dialog = dialogCreator.createOptionDialog(context, title, items, callBackListener, cancelListener);
        dialog.show();
    }

    public static void dismissDialog() {
        dialogCreator.dismissDialog();
    }

    public static void cancelDialog() {
        dialogCreator.cancelDialog();
    }

    public static boolean isDialogShowing() {
        return dialogCreator.isDialogShowing();
    }

}
