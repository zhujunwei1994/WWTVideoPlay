package com.cvim.v.play1;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class USBHelper {

    private static String uabs1  = "";

    private static String mntUsbRoot = "mnt/usb";

    private static ArrayList<File> mData = new ArrayList<>();


    public static String readFileDataFile(String path)
    {

        return  readUsbDirtory(path);
    }

    private static String readUsbDirtory(String path)
    {
//        Log.i("play","01 USBHelper readFileDa mntUsbRoot = "+mntUsbRoot+"  path = "+path);

        File file = new File(mntUsbRoot);
//        Log.i("play","02 USBHelper readFileDataFile file.exists() = "+file.exists()+"  mntUsbRoot = "+mntUsbRoot);

        if (file.exists())
        {
            File []files = file.listFiles();
            for (File f:files)
            {

//                Log.i("play","readUsbDirtory  mntUsbRoot =  "+mntUsbRoot+" f = "+f.getName()+"   f.getPath = "+f.getPath());

                File subFile = new File(f.getPath());
//                Log.i("play","--->readUsbDirtory  mntUsbRoot =  "+mntUsbRoot+" subFile = "+subFile.getName()+"   subFile.getPath = "+subFile.getPath()
//                        +"  subFile.exist = "+subFile.exists()+"   subFile = "+subFile);
                if (subFile==null||!subFile.exists()) return mntUsbRoot;


                File []subFileItems = subFile.listFiles();

                if (subFileItems==null) return mntUsbRoot;

                for (File iteml:subFileItems)
                {
//                    Log.i("play","readUsbDirtory  mntUsbRoot "+mntUsbRoot+" iteml = "+iteml.getName()+"   iteml.getPath = "+iteml.getPath());
                    if (path.equals(iteml.getName()))
                    {
//                        Log.i("play","=============readUsbDirtory mntUsbRoot "+mntUsbRoot+" f = "+iteml.getName()+"   f.getPath = "+iteml.getPath());
                        return iteml.getPath();
                    }
                }
            }
        }
        return mntUsbRoot;
    }

}
