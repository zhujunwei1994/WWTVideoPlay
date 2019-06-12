package com.cvim.v.play;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class VideoPlay extends AppCompatActivity {


    private VideoView mPlayVideo;

    private ArrayList <File>videoData;

    private int videoIndex = 0;

    private LinearLayout otherArea;
    private LinearLayout videoArea;
    ArrayList<File> tempFiles;


    private int mPaserFileCount = 1;

    // 从activity传递过来的所有文件夹
    private ArrayList <File>mMultixFile;

    //解析出来的所有需要播放的视频文件
    private ArrayList<File> playVideoFiles = new ArrayList<File>();

    // 最大解析文件数量
    private int mMaxParseFilseCount;

    // 用户选择解析文件索引
    private int mUserSelectedIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_play_main);
        initView();

       Bundle bundle = getIntent().getBundleExtra("files");
       if (bundle!=null)
       {
           mMultixFile = (ArrayList<File>) bundle.get("selectedFile");
           if (null !=mMultixFile)
           {
               mMaxParseFilseCount = mMultixFile.size();
               paserAllVideoFile(mMaxParseFilseCount);
               if (!playVideoFiles.isEmpty())
               {
                   Log.i("main","paraseVideFile videoIndex = "+videoIndex+"   playVideoFiles.size = "+playVideoFiles.size()+"playVideoFiles.get(0).getPath() = "+playVideoFiles.get(0).getPath());

                   mPlayVideo.setVideoURI( Uri.parse(playVideoFiles.get(0).getPath()));
                   mPlayVideo.start();
               }else
               {
                   finish();
               }

           }
       }
    }

    void paserAllVideoFile(int count)
    {
        if (count>mMultixFile.size()) count=mMultixFile.size();
        for (int i = 0; i<count;i++)
        {
            paraseVideFile(mMultixFile.get(i));
        }
    }

   void paraseVideFile(File file)
    {
        File[] files = file.listFiles();
        for (File f :files){
            if (f.getName().endsWith(".mp4")
                ||f.getName().endsWith(".wma")
                 ||f.getName().endsWith(".ogg")
                    ||f.getName().endsWith(".pcm")
                    ||f.getName().endsWith(".m4a")
                    ||f.getName().endsWith(".ac3")
                    ||f.getName().endsWith(".ec3")
                    ||f.getName().endsWith(".dtshd")
                    ||f.getName().endsWith(".wav")
                    ||f.getName().endsWith(".rm")
                    ||f.getName().endsWith(".ra")
                    ||f.getName().endsWith(".cd")
                    ||f.getName().endsWith(".amr")
                    ||f.getName().endsWith(".dts")
                    ||f.getName().endsWith(".dts")
                    ||f.getName().endsWith(".midi")
                    ||f.getName().endsWith(".snd")
                    ||f.getName().endsWith(".vqf")
                    ||f.getName().endsWith(".cda")
                    ||f.getName().endsWith(".aiff"))
            {
                playVideoFiles.add(f);
                Log.i("main","paraseVideFile videoIndex = "+videoIndex+"   playVideoFiles.size = "+playVideoFiles.size());

            }
        }
    }

    void initView(){
        mPlayVideo = (VideoView)findViewById(R.id.vv_play);
        mPlayVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoIndex++;
                if (videoIndex > playVideoFiles.size()-1)videoIndex = 0;
                mPlayVideo.setVideoURI( Uri.parse(playVideoFiles.get(videoIndex).getPath()));
                mPlayVideo.start();
            }
        });
        mPlayVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                finish();
//                return false;

                playNext(true);
                return true;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        Log.i("main","getCode = "+event.getKeyCode());

        if (event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_UP)
        {
            if (event.isLongPress())
            {
                Log.i("main","****************************isLongPress");

            }

            if (event.getRepeatCount()%20==0)
            {
                mUserSelectedIndex++;
                if (mUserSelectedIndex >mMaxParseFilseCount) mUserSelectedIndex = 0;

                Log.i("main","getCode = "+event.getKeyCode()+" count = "+event.getRepeatCount());
                return true;
            }
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_DOWN)
        {
            if (event.getRepeatCount()%20==0)
            {

                mUserSelectedIndex--;
                if (mUserSelectedIndex <0) mUserSelectedIndex = mMaxParseFilseCount;

                Log.i("main","getCode = "+event.getKeyCode()+" count = "+event.getRepeatCount());
                return true;
            }
        }

        if (event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            return super.dispatchKeyEvent(event);
        }
        if (event.getRepeatCount() ==0 &&event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_LEFT)
        {
            playNext(false);
            return true;
        }
        if (event.getRepeatCount() == 0&& event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            playNext(true);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    void playNext(Boolean direction)
    {

        if (playVideoFiles.size()==0) return;
        Log.i("main","vvideoIndex = "+videoIndex+"   playVideoFiles.size = "+playVideoFiles.size());


        videoIndex = videoIndex+( direction?1:-1);

        if (videoIndex > playVideoFiles.size()-1)videoIndex = 0;
        else if (videoIndex == -1) videoIndex = playVideoFiles.size()-1;


            mPlayVideo.setVideoURI( Uri.parse(playVideoFiles.get(videoIndex).getPath()));
            mPlayVideo.start();
    }

}
