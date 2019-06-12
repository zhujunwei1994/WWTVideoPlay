package com.cvim.v.play1;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cvim.v.play.R;

public class PlayHelper implements View.OnClickListener {

    PlayActivity mPlayActivity;

    private Button mBtn_a;
    private Button mBtn_b;
    private Button mBtn_c;
    private Button mBtn_d;
    private Button mBtn_m;


    public PlayHelper(PlayActivity mPlayActivity) {
        this.mPlayActivity = mPlayActivity;
        initView();
    }

    void initView()
    {
        mBtn_a = (Button)mPlayActivity.findViewById(R.id.video_item_a);
        mBtn_b = (Button)mPlayActivity.findViewById(R.id.video_item_b);
        mBtn_c = (Button)mPlayActivity.findViewById(R.id.video_item_c);
        mBtn_d = (Button)mPlayActivity.findViewById(R.id.video_item_d);
        mBtn_m = (Button)mPlayActivity.findViewById(R.id.video_item_m);

        mBtn_a.setOnClickListener(this);
        mBtn_b.setOnClickListener(this);
        mBtn_c.setOnClickListener(this);
        mBtn_d.setOnClickListener(this);
        mBtn_m.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mPlayActivity,VideoPlay.class);

        Log.i("play","02 USBHelper readFileDa mntUsbRoot ReadManager.isUExist() = "+ReadManager.isUExist());

        if (!ReadManager.isUExist()) return;

        Log.i("play","02 USBHelper readFileDa mntUsbRoot ReadManager.isUExist() = "+ReadManager.isUExist());


        switch (v.getId())
        {
            case R.id.video_item_a:
//                ReadVideoFiles.mHandler.sendEmptyMessage(0);
                intent.putExtra("index",0);
                Log.i("play","video_item_a");
                break;
            case R.id.video_item_b:
                intent.putExtra("index",1);
//                ReadVideoFiles.mHandler.sendEmptyMessage(1);
                Log.i("play","video_item_b");
                break;
            case R.id.video_item_c:
                intent.putExtra("index",2);
//                ReadVideoFiles.mHandler.sendEmptyMessage(2);
                Log.i("play","video_item_c");
                break;
            case R.id.video_item_d:
                intent.putExtra("index",3);
//                ReadVideoFiles.mHandler.sendEmptyMessage(3);
                Log.i("play","video_item_d");
                break;
            case R.id.video_item_m:
                intent.putExtra("index",4);
//                ReadVideoFiles.mHandler.sendEmptyMessage(4);
                 Log.i("play","video_item_m");
                break;
        }
        mPlayActivity.startActivity(intent);
    }
}
