package helper_tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import interfaces.CustomAlertDialog;

/**
 * class used to show custom alert dialog.
 */
public class TanawarAlertDialog implements CustomAlertDialog {
    @Override
    public AlertDialog showSimpleDialog(String title, String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setTitle(title);
        AlertDialog dialog = builder.create();
        return dialog;
    }
    @Override
    public AlertDialog showSimpleDialogWithOKButton(String title, String message,
                                                    Activity activity,
                                                    DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("Ok",listener);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
