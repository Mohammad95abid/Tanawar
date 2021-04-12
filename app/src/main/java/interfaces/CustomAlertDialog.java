package interfaces;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public interface CustomAlertDialog {
    AlertDialog showSimpleDialog(String title, String message, Activity activity);
    AlertDialog showSimpleDialogWithOKButton(String title, String message, Activity activity,
                                             DialogInterface.OnClickListener listener);
}
