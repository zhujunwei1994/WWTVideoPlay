package com.cvim.v.play.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;



public class ReceiverHelper {

    private  Context mContext;
    private  IntentFilter mFilter;
    private  MyReceiver mMyReceiver;

    class MyReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("main","--MyReceiver-----intent.getAction = "+intent.getAction());

            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            {

               // Intent intent1 = new Intent(mContext, VideoPlay.class);
                //mContext.startActivity(intent1);
            }
        }
    }
    public ReceiverHelper(Context mConttext) {
        this.mContext = mConttext;
    }

    public  void register()
    {
        mMyReceiver = new MyReceiver();

        mFilter = new IntentFilter();

        mFilter.addAction(Intent.ACTION_BOOT_COMPLETED);

        mContext.registerReceiver(mMyReceiver,mFilter);
    }
    public  void unRegister()
    {
        mContext.unregisterReceiver(mMyReceiver);
    }



}
