package com.cvim.v.play;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class ReadVideoFile {

    private Context mContext;
    private MainActivity mainActivity;

    public ArrayList<File> filesData = new ArrayList<File>();

    public ReadVideoFile(Context mContext,MainActivity mainActivity) {
        this.mContext = mContext;
        this.mainActivity = mainActivity;
    }

    public  boolean isGrantExternalRW(Activity activity) {
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

    void readFile()
    {
        String filepath = Environment.getExternalStorageDirectory().getPath();

        File[] files = Environment.getExternalStorageDirectory().getAbsoluteFile().listFiles();//new File(filepath);

        filesData = parseFileList(files);
        if (filesData.size()>0)
        {
            mainActivity.mHandler.sendEmptyMessage(500);
        }
    }

    ArrayList<File> parseFileList(File[] files)
    {
        ArrayList<File> tempFiles = new ArrayList<File>();

        if (null== files)  return null;

        for (File f: files)
        {
            Log.i("main","readFile f.name = "+f.getName()+"    f.path = "+f.getPath()+"   f.getABSPath = "+f.getAbsolutePath() );
//            if (f.isDirectory())
            {
                tempFiles.add(f);
            }
        }
        return tempFiles;
    }
}
