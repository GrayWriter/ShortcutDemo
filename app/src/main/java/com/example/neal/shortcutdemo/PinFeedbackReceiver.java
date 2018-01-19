package com.example.neal.shortcutdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by neal on 12/8/17.
 */

public class PinFeedbackReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PinFeedback","action:"+intent.getAction());
        Toast.makeText(context,"received pin feedback",Toast.LENGTH_SHORT).show();
    }
}
