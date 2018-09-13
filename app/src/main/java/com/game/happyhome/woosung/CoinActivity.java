package com.game.happyhome.woosung;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CoinActivity extends AppCompatActivity {
    int count=2; // 참여자 수
    NumberPicker picker;
    RelativeLayout layout;
    ImageView coinfront;
    ImageView coinback;
    Button button;
    Random random = new Random();
    RelativeLayout.LayoutParams layoutParamsCoin;
    RelativeLayout.LayoutParams layoutParamsButton1;
    RelativeLayout.LayoutParams layoutParamsButton2;
    RelativeLayout.LayoutParams layoutParamsText;
    boolean isCoinFront;
    int coinFrontCount = 0; //동전 앞면이 몇번 선택됐는지
    int coinCount = 0; //현재 몇명이 수행했는지.
    Vibrator vibe;
    TextView textView;
    boolean isStart;
    TimerTask tt;
    Timer timer;
    int time = 5000;
    Button buttonNext;
    Button buttonReset;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "CoinActivity::OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);

        picker = (NumberPicker) findViewById(R.id.picker);
        picker.setMinValue(2); //사람수가 2명부터 시작
        picker.setMaxValue(10);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(new String[] {
                "2명", "3명", "4명", "5명", "6명", "7명", "8명", "9명", "10명"
        });
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                count = newVal;
            }
        });
    }

    public void SetLayout() {
        /* 동전이미지 layout params setting */
        layoutParamsCoin = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsCoin.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        /* 다음버튼 layout params setting */
        layoutParamsButton1 = new RelativeLayout.LayoutParams
                (layout.getWidth()/2, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsButton1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParamsButton1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        /* 뒤집기 button layout params setting */
        layoutParamsButton2 = new RelativeLayout.LayoutParams
                (layout.getWidth()/2, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsButton2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParamsButton2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        /* 감추기 Text layout params setting */
        layoutParamsText = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, layout.getHeight()/2);
        layoutParamsText.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    }

    public void onSelectButtonClicked(View v) {
        layout = findViewById(R.id.coinlayout);
        layout.removeAllViews();

        SetLayout();

        /* coin image loading */
        coinfront = new ImageView(this);
        coinfront.setImageResource(R.drawable.coinfront);
        coinfront.setVisibility(View.INVISIBLE);
        coinfront.setLayoutParams(layoutParamsCoin);
        coinback = new ImageView(this);
        coinback.setImageResource(R.drawable.coinback);
        coinback.setVisibility(View.INVISIBLE);
        coinback.setLayoutParams(layoutParamsCoin);
        layout.addView(coinfront);
        layout.addView(coinback);

        /* 앞뒤면 중 random으로 띄움 */
        LoadCoin();

        /* 버튼 띄우기*/
        // 다음 버튼
        buttonNext = new Button(this);
        buttonNext.setText("다음");
        buttonNext.setTextSize(32);
        buttonNext.setBackgroundColor(0xffffffff);
        buttonNext.setLayoutParams(layoutParamsButton1);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isStart)
                    return;
                vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(200);
                ++coinCount;
                if(isCoinFront)
                    ++coinFrontCount;
                //게임 종료조건
                if(coinCount == count) {
                    textView = new TextView(getApplicationContext());
                    textView.setText("뒤집어주세요!");
                    textView.setTextSize(60);
                    textView.setBackgroundColor(0xff000000);
                    textView.setTextColor(0xffffffff);
                    textView.setLayoutParams(layoutParamsText);
                    textView.setGravity(Gravity.CENTER);
                    layout.addView(textView);
                    //button 감추기
                    buttonNext.setVisibility(View.INVISIBLE);
                    buttonReset.setVisibility(View.INVISIBLE);
                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), CoinSubActivity.class);
                            intent.putExtra("count", coinFrontCount);
                            startActivity(intent);
                        }
                    };
                    timer = new Timer();
                    timer.schedule(tt, (long)time);
                } else {
                    LoadCoin();
                }
            }
        });
        layout.addView(buttonNext);
        // 뒤집기 버튼
        buttonReset = new Button(this);
        buttonReset.setText("뒤집기");
        buttonReset.setTextSize(32);
        buttonReset.setBackgroundColor(0xffffffff);
        buttonReset.setLayoutParams(layoutParamsButton2);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isStart)
                   return;
                if(isCoinFront) {
                    coinback.setVisibility(View.VISIBLE);
                    coinfront.setVisibility(View.INVISIBLE);
                    isCoinFront = false;
                }
                else {
                    coinfront.setVisibility(View.VISIBLE);
                    coinback.setVisibility(View.INVISIBLE);
                    isCoinFront = true;
                }
            }
        });
        layout.addView(buttonReset);
    }

    public void Reset() {
        coinFrontCount = 0;
        coinCount = 0;
    }

    public void LoadCoin() {
        isStart = false;
        textView = new TextView(this);
        textView.setText("Click");
        textView.setTextSize(58);
        textView.setBackgroundColor(0xff000000);
        textView.setTextColor(0xffffffff);
        textView.setLayoutParams(layoutParamsText);
        textView.setGravity(Gravity.CENTER);
        layout.addView(textView);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.removeView(textView);
                isStart=true;
            }
        });

        int ranNum = random.nextInt(2);
        if(ranNum == 0) {
            coinback.setVisibility(View.VISIBLE);
            coinfront.setVisibility(View.INVISIBLE);
            isCoinFront = false;
        }
        else {
            coinfront.setVisibility(View.VISIBLE);
            coinback.setVisibility(View.INVISIBLE);
            isCoinFront = true;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("DEBUG", "BombActivity::OnDestroy()");
        super.onDestroy();
    }
}

