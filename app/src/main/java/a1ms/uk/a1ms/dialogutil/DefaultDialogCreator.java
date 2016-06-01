package a1ms.uk.a1ms.dialogutil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.TextView;

public class DefaultDialogCreator implements IDialogCreator {

    private static AlertDialog mAlertDialog;

    @Override
    public void dismissDialog() {
        if ((mAlertDialog != null) && (mAlertDialog.isShowing())) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    public void cancelDialog() {
        if ((mAlertDialog != null) && (mAlertDialog.isShowing())) {
            mAlertDialog.cancel();
            mAlertDialog = null;
        }
    }

    @Override
    public boolean isDialogShowing() {
        return (mAlertDialog != null && mAlertDialog.isShowing());
    }

    @Override
    public Dialog createOKDialog(Context context, String title, String message, String okLabel, final DialogCallBackListener dialogCallBackListener, boolean isCancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(okLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialogCallBackListener != null) {
                    dialogCallBackListener.run();
                }
            }
        });

        if (isDialogShowing()) {
            dismissDialog();
        }

        mAlertDialog = builder.show();
        mAlertDialog.setCancelable(isCancellable);

        TextView textViewMessage = (TextView) mAlertDialog.findViewById(android.R.id.message);
        textViewMessage.setGravity(Gravity.CENTER);

        return mAlertDialog;
    }

    @Override
    public Dialog createYesNoDialog(Context context, String title, String message, String yesLabel, String noLabel, final DialogCallBackListener yesCallBackListener, final DialogCallBackListener noCallBackListener, boolean isCancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(yesLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (yesCallBackListener != null) {
                    yesCallBackListener.run();
                }
            }
        });

        builder.setNegativeButton(noLabel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (noCallBackListener != null) {
                    noCallBackListener.run();
                }
                dialog.dismiss();
            }
        });


        if (isDialogShowing()) {
            dismissDialog();
        }

        mAlertDialog = builder.show();
        mAlertDialog.setCancelable(isCancellable);

        TextView textViewMessage = (TextView) mAlertDialog.findViewById(android.R.id.message);
        textViewMessage.setGravity(Gravity.CENTER);

        return mAlertDialog;
    }

    public Dialog createOptionDialog(Context context, String title, CharSequence[] items, DialogInterface.OnClickListener callBackListener, DialogInterface.OnCancelListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, callBackListener);

        mAlertDialog = builder.show();
        mAlertDialog.setOnCancelListener(cancelListener);
        return mAlertDialog;
    }
}
