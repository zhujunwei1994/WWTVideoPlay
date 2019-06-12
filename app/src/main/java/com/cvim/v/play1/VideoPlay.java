package com.cvim.v.play1;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.cvim.v.play.R;
import com.cvim.v.play.ReadVideoFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class VideoPlay extends AppCompatActivity {

    private VideoView mPlayVideo;

    private LinearLayout  mWelcome_tv;

    private LinearLayout mVideo_area;

    //解析出来的所有需要播放的视频文件
    private ArrayList<File> playVideoList = new ArrayList<>();


    public void setPlayVideoList(ArrayList<File> playVideoList) {
        this.playVideoList = playVideoList;
    }

    private int videoIndex = 0;

    private LinearLayout otherArea;
    private LinearLayout videoArea;
    ArrayList<File> tempFiles;

    int UPDATE_VIDEO_LIST = 8;
    int ClEAN_ALL_VIDEO_FILE_DATA = 9;


    int keyDownCount = 0;


    public  Handler mHanlderUpdateVideoList = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int msgWhat = msg.what;
            videoIndex = 0;

            Log.i("play", "视频数据已经更新同步 请同步..... msgWhat = "+msgWhat);

                if (msgWhat == ClEAN_ALL_VIDEO_FILE_DATA)
                {
                    mPlayVideo.pause();
                    playVideoList.clear();
                    mVideo_area.setVisibility(View.GONE);
                    mWelcome_tv.setVisibility(View.VISIBLE);
                }else   if (msgWhat == UPDATE_VIDEO_LIST)
                {
                    if (null!=ReadVideoFiles.getmVideoMP4File()&&ReadVideoFiles.getmVideoMP4File().size()>0)
                    {
                        setPlayVideoList(ReadVideoFiles.getmVideoMP4File());
//                        Collections.sort(playVideoList);
                        updateVideoList();
                    }
            }
        }
    };

    void updateVideoList(){
        Log.i("play", "数据已经更新同步 即将播放.....playVideoList = "+playVideoList.size());
        if (playVideoList.size()==0)
        {
            Log.i("play", "01 -视频数据为空 .....playVideoList = "+playVideoList.size());
            mPlayVideo.pause();
            mVideo_area.setVisibility(View.GONE);
            mWelcome_tv.setVisibility(View.VISIBLE);
            return;
        }else {
            Log.i("play", "02 - 视频数据更新已同步 播放.....playVideoList = "+playVideoList.size());
            mVideo_area.setVisibility(View.VISIBLE);
            mWelcome_tv.setVisibility(View.GONE);

            mPlayVideo.setVideoURI(Uri.parse(playVideoList.get(0).getPath()));
            mPlayVideo.start();
//            for (File f:playVideoList)
//            {
//                Log.i("play", "mHanlderUpdateVideoList f.name = " + f.getName() + "    f.path = " + f.getPath());
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        Log.i("play","VideoPlay   onCreate");
        initView();

        initData();

    }
    void initData()
    {
        int index = getIntent().getIntExtra("index",0);
        Log.i("play","oncreate  index = "+index);
        ReadVideoFiles.setmVideoPlay(this);
        ReadManager.init(this,index);

    }

    void initView(){

        mPlayVideo = (VideoView)findViewById(R.id.vv_play);

        mWelcome_tv = (LinearLayout)findViewById(R.id.welcome_tv);

        mVideo_area = (LinearLayout)findViewById(R.id.video_area);

        mWelcome_tv.setVisibility(View.GONE);

        MediaController mc = new MediaController(this);
        mc.setVisibility(View.GONE);
        mPlayVideo.setMediaController(mc);

        mPlayVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext(true);
            }
        });
        mPlayVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                playNext(true);
                return true;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.i("play","getDownTime = "+event.getDownTime()+"   getEventTime = "+event.getEventTime()+"   continueTime  = "+(event.getEventTime()-event.getDownTime())+"    event.getScanCode() = "+ event.getScanCode());

        if (playVideoList.size()==0)
        {
            mPlayVideo.pause();
            mVideo_area.setVisibility(View.GONE);
            mWelcome_tv.setVisibility(View.VISIBLE);
        }else {
            mVideo_area.setVisibility(View.VISIBLE);
            mWelcome_tv.setVisibility(View.GONE);
        }


        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            Log.i("play","keycode = "+event.getKeyCode()+"    event.getAction = "+event.getAction()+"    event.getRepeatCount = "+event.getRepeatCount());

            if ( event.getRepeatCount() == 11 &&event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                finish();
                return true;
            }

            if (event.getRepeatCount() == 11 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT ) {
                playNext(false);
                return true;
            }

            if (event.getRepeatCount() ==11 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                playNext(true);
                return true;
            }
        }

        if (handlerKeys(event)) return true;      // 组合键 处理方式

        return super.dispatchKeyEvent(event);
    }

    void playNext(Boolean direction)
    {
        Log.i("play","*********************************************************************************");
        if (playVideoList.size()==0)  return;
        videoIndex = videoIndex+( direction?1:-1);
        if (videoIndex >= playVideoList.size())videoIndex = 0;
        else if (videoIndex == -1) videoIndex = playVideoList.size()-1;

        Log.i("play","   playVideoFiles.size = "+playVideoList.size()+"  direction = "+direction);
        Log.i("play","*************************************************************************************************************");

        for (File f:playVideoList)
        {
            Log.i("play", "videoPlay playNext f.name = " + f.getName() + "    f.path = " + f.getPath());
        }
        Log.i("play","**************************************************************************************************************");


        Log.i("play","playNext videoIndex = "+videoIndex+"  getName = "+playVideoList.get(videoIndex).getName()+"    playVideoList.get(videoIndex).getPath() ="+playVideoList.get(videoIndex).getPath());

        mPlayVideo.setVideoURI( Uri.parse(playVideoList.get(videoIndex).getPath()));
            mPlayVideo.start();
    }

    /**
     *  0：a ： 左右上   1: b ： 左右下
     *  2: b :  上下左   3: c ： 上下右  4: d :  左左右右
     *     public static final int KEYCODE_DPAD_UP         = 19;
     *     public static final int KEYCODE_DPAD_DOWN       = 20;
     *     public static final int KEYCODE_DPAD_LEFT       = 21;
     *     public static final int KEYCODE_DPAD_RIGHT      = 22;
     */

    long[] mHits = new long[3];
    int[] mkeys = new int[3];
    int[] keys          = {KeyEvent.KEYCODE_DPAD_UP  , KeyEvent.KEYCODE_DPAD_DOWN , KeyEvent.KEYCODE_DPAD_LEFT};
    int[] keysUP        = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_UP};
    int[] keysDown      = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_DOWN};
    int[] keysLeft      = {KeyEvent.KEYCODE_DPAD_UP  , KeyEvent.KEYCODE_DPAD_DOWN , KeyEvent.KEYCODE_DPAD_LEFT};
    int[] keysRight     = {KeyEvent.KEYCODE_DPAD_UP  , KeyEvent.KEYCODE_DPAD_DOWN , KeyEvent.KEYCODE_DPAD_RIGHT};

    int[] keysA        = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_UP};
    int[] keysB        = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_DOWN};
    int[] keysC        = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_LEFT};
    int[] keysD        = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_RIGHT};
    int[] keysABC        = {KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_CENTER};
    int[] keysABC1        = {KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_LEFT};

    int[] keysMoreFounction   = {KeyEvent.KEYCODE_DPAD_LEFT  , KeyEvent.KEYCODE_DPAD_UP , KeyEvent.KEYCODE_DPAD_RIGHT};
    boolean handlerKeys(KeyEvent event)
    {
        int keyCode = event.getKeyCode();

        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_LEFT||keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            System.arraycopy(mkeys, 1, mkeys, 0, mkeys.length - 1);
            mkeys[mkeys.length - 1] = event.getKeyCode();
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 10000)) {
                if (Arrays.equals(mkeys, keysA)) {    // left right up 0->A
                    ReadVideoFiles.mHandler.sendEmptyMessage(0);
                    return true;
                }else  if (Arrays.equals(mkeys,keysB )) { // left right down B->1
                    ReadVideoFiles.mHandler.sendEmptyMessage(1);
                    return true;
                }else  if (Arrays.equals(mkeys, keysC)) { // up down left c-> 2
                    ReadVideoFiles.mHandler.sendEmptyMessage(2);
                    return true;
                }
                else  if (Arrays.equals(mkeys, keysD)) { //up down right d->3
                    ReadVideoFiles.mHandler.sendEmptyMessage(3);
                    return true;
                }
                else  if (Arrays.equals(mkeys, keysABC1)) { //  left up right 4
                    ReadVideoFiles.mHandler.sendEmptyMessage(4);
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }



    boolean mutliKeys(KeyEvent event)
    {
        if (Arrays.equals(mkeys, keysUP)) {    // left right up 0
            ReadVideoFiles.mHandler.sendEmptyMessage(0);
            return true;
        }else  if (Arrays.equals(mkeys,keysDown )) { // left right down 1
            ReadVideoFiles.mHandler.sendEmptyMessage(1);
            return true;
        }else  if (Arrays.equals(mkeys, keysLeft)) { // up down left 2
            ReadVideoFiles.mHandler.sendEmptyMessage(2);
            return true;
        }
        else  if (Arrays.equals(mkeys, keysRight)) { //up down right 3
            ReadVideoFiles.mHandler.sendEmptyMessage(3);
            return true;
        }
        else  if (Arrays.equals(mkeys, keysMoreFounction)) { //  left up right 4
            ReadVideoFiles.mHandler.sendEmptyMessage(4);
            return true;
        }
        return  super.dispatchKeyEvent(event);
    }

    boolean longPressKey(KeyEvent event)
    {
        if ((event.getRepeatCount() == 10 ||event.getRepeatCount() == 11)&& event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_LEFT)
        {
            Log.i("play","你已经长按了 返回键----12 KEYCODE_DPAD_LEFT event.getRepeatCount() = "+ event.getRepeatCount());
            ReadVideoFiles.mHandler.sendEmptyMessage(0);
            return true;
        }
        if ((event.getRepeatCount() == 10 ||event.getRepeatCount() == 11)&& event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_UP)
        {
            Log.i("play","你已经长按了 返回键----12 KEYCODE_DPAD_UP event.getRepeatCount() = "+ event.getRepeatCount());
            ReadVideoFiles.mHandler.sendEmptyMessage(1);
            return true;
        }
        if ((event.getRepeatCount() == 10 ||event.getRepeatCount() == 11)&& event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            Log.i("play","你已经长按了 返回键----12 KEYCODE_DPAD_RIGHT event.getRepeatCount() = "+ event.getRepeatCount());
            ReadVideoFiles.mHandler.sendEmptyMessage(2);
            return true;
        }

        if ((event.getRepeatCount() == 10 ||event.getRepeatCount() == 11)&& event.getAction() == KeyEvent.ACTION_DOWN &&event.getKeyCode()==KeyEvent.KEYCODE_DPAD_DOWN)
        {
            Log.i("play","你已经长按了 返回键----12 KEYCODE_DPAD_DOWN event.getRepeatCount() = "+ event.getRepeatCount());
            ReadVideoFiles.mHandler.sendEmptyMessage(3);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onStart() {
        Log.i("play","VideoPlay   onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("play","VideoPlay   onResume");
        ReadManager.initRegisterUSB();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("play","VideoPlay   onPause");
        ReadManager.unRegisterReceiver();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("play","VideoPlay   onDestroy");
//        ReadManager.unRegisterReceiver();
        super.onDestroy();
    }
}
