package com.cvim.v.play1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cvim.v.play.R;
import com.cvim.v.play.receiver.ReceiverHelper;

public class PlayActivity extends AppCompatActivity {

    PlayHelper mPlayHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mPlayHelper = new PlayHelper(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((event.getRepeatCount() == 9)
                    && event.getAction() == KeyEvent.ACTION_DOWN  )
            {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
