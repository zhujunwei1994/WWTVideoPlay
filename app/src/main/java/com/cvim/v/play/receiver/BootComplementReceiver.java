package com.cvim.v.play.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cvim.v.play1.VideoPlay;

public class BootComplementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("main","--BootComplementReceiver-----intent.getAction = "+intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {

            Intent intent1 = new Intent(context, VideoPlay.class);
//            context.startActivity(intent1);
        }
    }


}
