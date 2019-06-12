package com.cvim.v.play1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ReadVideoFiles {

    public static int indexFile = 0;
    public static int parseCount = 0;

    public static VideoPlay mVideoPlay;

    public static void setmVideoPlay(VideoPlay mVideoPlay) {
        ReadVideoFiles.mVideoPlay = mVideoPlay;
    }

    private static ArrayList<File> mMultixDirtory = new ArrayList<>();

    private static ArrayList<File> mVideoMP4File = new ArrayList<>();

    private static Activity mContext;

    public static ArrayList<File> getmVideoMP4File() {
        return mVideoMP4File;
    }

    public static void setIndex(int index) {
        ReadVideoFiles.indexFile = index;
    }


    public static  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int msgIndex = msg.what;
            indexFile= 0;
            Log.i("play","readVdeioFile mHandler msgIndex = "+msgIndex+"   indexFile = "+indexFile+"  mMultixDirtory.size = "+mMultixDirtory.size());
            mVideoMP4File.clear(); // 次解析之前清除上一次留下的数据
            if(msgIndex==0){ //A
                parseCount = 1;
                if (mMultixDirtory.size()>0) {
                    parseAllVideoFileItems(mMultixDirtory.get(0).listFiles());
                }else
                {
                    mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.ClEAN_ALL_VIDEO_FILE_DATA);
                }
            }else if(msgIndex==1){
                parseCount = 1;
                if (mMultixDirtory.size()>=2){
                    parseAllVideoFileItems(mMultixDirtory.get(1).listFiles()); ////B
                }else
                {
                    mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.ClEAN_ALL_VIDEO_FILE_DATA);
                }
            }else if(msgIndex==2){
                parseCount = 1;
                if (mMultixDirtory.size()>=3){
                    parseAllVideoFileItems(mMultixDirtory.get(2).listFiles());  //C
                }else
                {
                    mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.ClEAN_ALL_VIDEO_FILE_DATA);
                }
            }else if(msgIndex==3){
                parseCount = 1;
                if (mMultixDirtory.size()>=4){
                    parseAllVideoFileItems(mMultixDirtory.get(3).listFiles()); // D
                }
            }else if(msgIndex==4){
                    parseCount = 3;
                if (mMultixDirtory.size()>0) {
                    paserAllVideoFile(3); // //ABC 目录循环
                }else
                {
                    mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.ClEAN_ALL_VIDEO_FILE_DATA);
                }
            }
            Log.i("play","readVdeioFile mHandler msgIndex = "+msgIndex+"   indexFile = "+indexFile+"  parseCount = "+parseCount);
        }
    };

    public static void reset(){
        if (mMultixDirtory!=null) mMultixDirtory.clear();
        if (mVideoMP4File!=null) mVideoMP4File.clear();
        //if (null!=mVideoPlay) mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.ClEAN_ALL_VIDEO_FILE_DATA);
    }

    public static  void initPermission(Activity context)
    {
        mContext= context;
        if (!ReadManager.isMntUsb)
        {
           // readInternalStorage(context);
        }
    }

    public static void readInternalStorage(Activity context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
        {
            if (isGrantExternalRW(context)){
                readFile();
            }
        }else
        {
            readFile();
        }
    }
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            return false;
        }
        return true;
    }

    public static void setmMultixDirtory(ArrayList<File> mMultixDirtory) {
        ReadVideoFiles.mMultixDirtory = mMultixDirtory;
    }


    public static void readFile() {
        String videosPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "video/";

       // Log.i("play", "readFile videosPath = "+videosPath);

        File videosFile = new File(videosPath);

      //  Log.i("play", "readFile videosFile = "+videosFile.exists());
        if (!videosFile.exists()) return;

        File[] subVideoFiles = videosFile.listFiles();
        if (subVideoFiles == null || subVideoFiles.length == 0) return;

        parseAllFiles(subVideoFiles);
    }

    public static void parseAllFiles(File[] subVideoFiles)
    {
        for (File f : subVideoFiles) {
            if (f.isDirectory()) {
//                Log.i("play", "readFile f.name = " + f.getName() + "    f.path = " + f.getPath() + "   f.getABSPath = " + f.getAbsolutePath());
                mMultixDirtory.add(f);
            }
        }
    }

    public static void paserAllVideoFile(int count) {
        mVideoMP4File.clear(); // 次解析之前清除上一次留下的数据
        if (count >=mMultixDirtory.size())
        {
            count = mMultixDirtory.size();
            parseCount = count;
        }
        for (int i = 0; i < count; i++) {
            if (mMultixDirtory.get(i).listFiles() != null || mMultixDirtory.get(i).listFiles().length != 0)
            {
                parseAllVideoFileItems(mMultixDirtory.get(i).listFiles());
            }
        }
    }

    public static void parseAllVideoFileItems(File[] files) {
//        Log.i("play", " parseAllVideoFileItems indexFile = "+indexFile);

        for (File f : files) {
            if (f.getName().endsWith(".mp4")
                    || f.getName().endsWith(".wma")
                    || f.getName().endsWith(".ogg")
                    || f.getName().endsWith(".pcm")
                    || f.getName().endsWith(".m4a")
                    || f.getName().endsWith(".mov")
                    || f.getName().endsWith(".ac3")
                    || f.getName().endsWith(".ec3")
                    || f.getName().endsWith(".dtshd")
                    || f.getName().endsWith(".wav")
                    || f.getName().endsWith(".rm")
                    || f.getName().endsWith(".ra")
                    || f.getName().endsWith(".cd")
                    || f.getName().endsWith(".amr")
                    || f.getName().endsWith(".dts")
                    || f.getName().endsWith(".dts")
                    || f.getName().endsWith(".midi")
                    || f.getName().endsWith(".snd")
                    || f.getName().endsWith(".vqf")
                    || f.getName().endsWith(".cda")
                    || f.getName().endsWith(".aiff")) {
                  mVideoMP4File.add(f);
//                Log.i("play", "parseFileList f.name = " + f.getName() + "    f.path = " + f.getPath() + "   f.getABSPath = " + f.getAbsolutePath()+"  f = "+f.lastModified());

            }

        }
        if (null!=mVideoPlay)
        {
            indexFile++;
            if (parseCount == indexFile)
            {
//                Collections.sort(mVideoMP4File);
                Collections.sort(mVideoMP4File,new CompareName());
                Log.i("play", "数据解析完成 刷新播放列表 mVideoMP4File.size = "+mVideoMP4File.size());

                if (mVideoMP4File.size() == 0)
                {

                    mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.ClEAN_ALL_VIDEO_FILE_DATA);
                }else
                {
                    mVideoPlay.mHanlderUpdateVideoList.sendEmptyMessage(mVideoPlay.UPDATE_VIDEO_LIST);
                }
            }
            Log.i("play", "parseFileList parseCount = " +parseCount + "   indexFile = "+indexFile);

        }
    }

}
