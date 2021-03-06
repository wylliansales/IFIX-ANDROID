package tools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class Notification {

    public static void notify(final Context ctx, final String msg, int SPLASH_TIME_OUT){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        }, SPLASH_TIME_OUT);
    }
}
