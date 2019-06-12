package com.cvim.v.play1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cvim.v.play.ReadVideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ReadManager {


    private static final String readURootDir = "projectorad";


    private static final String mntUsbRoot = "mnt/usb/projectorad";

    public static boolean isMntUsb = true;


    public static ReadVideoFiles mReadVideoFile;

    private static VideoPlay mContext;

    private static int mDirIndex;


    public static void init(VideoPlay context,int index)
    {
        mContext = context;
        mDirIndex = index;
        ReadVideoFiles.initPermission(context);
        readUSB(context);
    }
    private static void readUSB(Activity context)
    {
        /**
         * U盘不存在的情况下 读取内部存储 video目录
         */
        ReadVideoFiles.reset();

        readUContent(USBHelper.readFileDataFile(readURootDir));
    }

    public static boolean isUExist()
    {
        if ( USBHelper.readFileDataFile(readURootDir).contains(readURootDir))return true;
        return false;
    }

    private static void readUContent(String usbPath)
    {
        // 读取U盘
        if (usbPath.contains(readURootDir))
        {
            File file  = new File(usbPath);
            if (!file.exists()) return;
            ArrayList<File> tempArr = new ArrayList<>();

            if (file == null|| file.listFiles().length==0) {
                if (null!=mContext) mContext.mHanlderUpdateVideoList.sendEmptyMessage(mContext.ClEAN_ALL_VIDEO_FILE_DATA);
                return;
            }

            for (File item:file.listFiles())
            {
                if (item.isDirectory()) tempArr.add(item);
                Log.i("play","023 item.getName = "+item.getName()+"   item.Path = "+item.getPath());
            }
            Collections.sort(tempArr,new CompareName());
            for (int i =0; i<tempArr.size();i++)
            {
                File item = tempArr.get(i);
                Log.i("play","023 Collections.sort item.getName = "+item.getName()+"   item.Path = "+item.getPath());
            }
            ReadVideoFiles.setmMultixDirtory(tempArr);
//            Log.i("play","023 USBHelper readFileDataFile file.exists() = "+file.exists()+"  mntUsbRoot = "+mntUsbRoot+"   mDirIndex = "+mDirIndex );
            ReadVideoFiles.mHandler.sendEmptyMessage(mDirIndex);
        }else // 读取内存存储video目录
        {
            isMntUsb = false;
            // ReadVideoFiles.paserAllVideoFile(0); // 默认第一个目录
        }
    }


    public static void initRegisterUSB()
    {
        Log.i("play", "ReadManager initRegisterUSB");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme("file");
        intentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        mContext.registerReceiver(usbReceiver,intentFilter);
    }

    public  static void unRegisterReceiver()
    {
        mContext.unregisterReceiver(usbReceiver);
    }

    private static  BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("main","intent.getAction = "+intent.getAction());

            if (intent.getAction()== Intent.ACTION_MEDIA_EJECT)
            {
                ReadVideoFiles.reset();
                isMntUsb = false;

                if (null!=mContext) mContext.mHanlderUpdateVideoList.sendEmptyMessage(mContext.ClEAN_ALL_VIDEO_FILE_DATA);

            }
            if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED))
            {

                if (intent.getData()!=null)
                {
//                    String usbPath = intent.getData().getPath();
//                    Log.i("play","usbPath = "+usbPath);
//                    Log.i("play","intent.getScheme = "+intent.getScheme());
//
//                    File file = new File(usbPath);
//                    Log.i("play","file = "+file.exists());
//                    Log.i("play","file.getName = "+file.getName());
//                    Log.i("play","file.getAbsolutePath = "+file.getAbsolutePath());
//                    File []files =  file.listFiles();
//                    for (File f :files)
//                    {
//                          if (f.getPath().contains(readURootDir))
//                        {
////                            Log.i("play","f.name = "+f.getName()+"  f.getAbsolutePath = "+f.getAbsolutePath()+"  f.paht = "+f.getPath());
//                            readUContent(f.getPath());
//                        }
//                    }
                    isMntUsb = true;
                    if (ReadManager.isUExist())
                    {
                        readUSB(mContext);
                    }
                }
            }
        }
    };

}
