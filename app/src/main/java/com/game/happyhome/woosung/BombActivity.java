package com.game.happyhome.woosung;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.game.happyhome.woosung.R;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BombActivity extends AppCompatActivity {
    int minute;
    static boolean alert = false;
    ImageView iv;
    NumberPicker picker;
    TextView tv;
    Button button;
    GlideDrawableImageViewTarget imageViewTarget;
    TimerTask tt;
    Timer timer= null;
    Random ran;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "BombActivity::OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb);

        picker = (NumberPicker) findViewById(R.id.picker);
        picker.setMinValue(1);
        picker.setMaxValue(5);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(new String[] {
                "1분", "2분", "3분", "4분", "5분"
        });
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                minute = newVal;
            }
        });
    }

    public void onButtonClicked(View v) {
        Log.d("DEBUG", "BombActivity::OnButton1Clicked()");
        tt = new TimerTask() {
            @Override
            public void run() {
                Log.d("DEBUG", "BombActivity::OnButton1Clicked() TimerTask.run()");
                Intent intent = new Intent(getApplicationContext(), BombSubActivity.class);
                startActivity(intent);
            }
        };
        timer = new Timer();
        ran = new Random();
        //최소 30초는 나오도록 설정
        if(minute == 0)
            minute = 1;
        minute *= 10;
        int ranNum  = ran.nextInt(10);
        long time = (long)minute - ranNum;
        time = time * 6000;
        if(time < 30000)
            time = 30000;
        Log.d("DEBUG", "BombActivity::OnButton1Clicked() time: " + time);
        timer.schedule(tt, (long)time);
        // Number Picker, button, text 없애기
        button = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);
        button.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        picker.setVisibility(View.INVISIBLE);

        // 폭탄 이미지 띄우기
        iv = (ImageView) findViewById(R.id.bomb);
        imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this).load(R.raw.bomb).into(imageViewTarget);

    }

    @Override
    protected void onDestroy() {
        Log.d("DEBUG", "BombActivity::OnDestroy()");
        if (timer != null)
            timer.cancel();
        super.onDestroy();
    }
}

