package com.cvim.v.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class USBHelper {

    private Context mContext;
    private MainActivity mMainActivity;

    public ArrayList<File> fileData = new <File>ArrayList();

    public USBHelper(Context context,MainActivity mainActivity){
        this.mContext = context;
        this.mMainActivity = mainActivity;
    }
    void init()
    {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme("file");
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        mContext.registerReceiver(usbReceiver,intentFilter);

    }

    void unRegisterReceiver()
    {
        mContext.unregisterReceiver(usbReceiver);
    }
    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("main","intent.getAction = "+intent.getAction());

            if (intent.getAction()== Intent.ACTION_MEDIA_EJECT)
            {
                mMainActivity.mHandler.sendEmptyMessage(400);
            }


            if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED))
            {
                Log.i("main","intent.getdata = "+intent.getData());

                if (intent.getData()!=null)
                {
                    String usbPath = intent.getData().getPath();
                    Log.i("main","usbPath = "+usbPath);
                    Log.i("main","intent.getScheme = "+intent.getScheme());

                    File file = new File(usbPath);
                    Log.i("main","file = "+file.exists());
                    Log.i("main","file.getName = "+file.getName());
                    Log.i("main","file.getAbsolutePath = "+file.getAbsolutePath());
                    File []files =  file.listFiles();
                    for (File f :files)
                    {
                     //  if (f.getName().endsWith(".mp4"))
                       {
                           fileData.add(f);
                           Log.i("main","f.name = "+f.getName()+"  f.getAbsolutePath = "+f.getAbsolutePath()+"  f.paht = "+f.getPath());
                       }
                    }
                }
//                Message data = new Message();
//                data.arg1 = 200;
                //MainActivity.mHandler.sendEmptyMessage(200);
                if (fileData.size()>0)
                 mMainActivity.mHandler.sendEmptyMessage(200);
            }

        }
    };


}
