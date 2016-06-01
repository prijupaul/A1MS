package a1ms.uk.a1ms.dialogutil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jialian.sun on 12/22/2014.
 */
public interface IDialogCreator {
    Dialog createOKDialog(Context context, String title, String message, String okLabel, DialogCallBackListener dialogCallBackListener, boolean isCancellable);

    Dialog createYesNoDialog(Context context, String title, String message, String yesLabel, String noLabel, DialogCallBackListener yesCallBackListener, DialogCallBackListener noCallBackListener, boolean isCancellable);

    Dialog createOptionDialog(Context context, String title, CharSequence[] items, DialogInterface.OnClickListener callBackListener, DialogInterface.OnCancelListener cancelListener);

    void dismissDialog();

    void cancelDialog();

    boolean isDialogShowing();
}
