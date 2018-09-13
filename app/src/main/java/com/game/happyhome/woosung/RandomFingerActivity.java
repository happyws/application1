package com.game.happyhome.woosung;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.game.happyhome.woosung.R;

import java.util.Random;

public class RandomFingerActivity extends AppCompatActivity {
    static boolean run;
    RelativeLayout layout;
    int maxCount = 10;  //maximum participants
    int delay = 4000;   //(당첨자 발생시간)milliseconds
    ImageView iv[] = new ImageView[maxCount];
    int id[] = new int[maxCount];
    int x[] = new int[maxCount];
    int y[] = new int[maxCount];
    static boolean is_finish = false;
    static int count = 0;
    static int ranNum = -1;
    static long startTime = System.currentTimeMillis()+10000;
    Vibrator vibe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "RandomFingerAcitivity::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranfin);

        layout = (RelativeLayout) findViewById(R.id.layout1);
        Intent intent = getIntent();
    }

    @Override
    protected void onStart() {
        Log.d("DEBUG", "RandomFingerAcitivity::onStart()");
        super.onStart();

        Init();
        CheckThread myThread = new CheckThread();
        myThread.setDaemon(true);
        myThread.start();
    }

    public void Init() {
        Log.d("DEBUG", "RandomFingerAcitivity::Init()");
        is_finish = false;
        count = 0;
        ranNum = -1;
        startTime = System.currentTimeMillis()+10000;
    }

    @Override
    protected void onStop() {
        Log.d("DEBUG", "RandomFingerAcitivity::onStop()");
        super.onStop();
        run=false;
    }

    public void makeImage(MotionEvent event, int index) {
        Log.d("DEBUG", "RandomFingerAcitivity::makeImage() index[" + index + "]");
       /* layout에 box image 추가 */
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        iv[index] = new ImageView(getBaseContext());
        iv[index].setLayoutParams(layoutParams);
        iv[index].setImageResource(R.drawable.blue);
        /* event로부터 image 정보 추가 */
        id[index] = event.getPointerId(index); //터치한 순간부터 부여되는 포인트 고유번호.
        x[index] = (int)event.getX(index);
        y[index] = (int)event.getY(index) - 250;

        layoutParams.setMargins(x[index], y[index], 0, 0);
        layout.addView(iv[index]);
    }
    public void makeImageForEnd(MotionEvent event, int index) {
        Log.d("DEBUG", "RandomFingerAcitivity::makeImageForEnd() index[" + index + "]");
        /* layout에 box image 추가 */
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(400, 250);

        iv[index] = new ImageView(getBaseContext());
        iv[index].setLayoutParams(layoutParams);
        iv[index].setImageResource(R.drawable.red);
        /* event로부터 image 정보 추가 */
        id[index] = event.getPointerId(index); //터치한 순간부터 부여되는 포인트 고유번호.
        x[index] = (int)event.getX(index);
        y[index] = (int)event.getY(index) - 250;

        layoutParams.setMargins(x[index], y[index], 0, 0);

        layout.addView(iv[index]);
        /* 뒤로가기 버튼 추가 */
        final Button btn = new Button(this);
        // setId 버튼에 대한 키값
        btn.setText("뒤로가기");
        btn.setBackgroundColor(0xff00ddff);
        btn.setTextSize(18);
        btn.setTextColor(0xffffffff);


        btn.setLayoutParams(layoutParams2);
        Log.d("DEBUG", "RandomFingerAcitivity::makeImageForEnd() setOnClickListener");
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        layout.addView(btn);


        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("DEBUG", "RandomFingerAcitivity::onTouchEvent()");
        if(is_finish) {
            Log.d("DEBUG", "RandomFingerAcitivity::onTouchEvent() is_finish");
            return true;
        }
        int pointer_count = event.getPointerCount(); //현재 터치 발생한 포인트 수를 얻는다.
        for(int i=0; i<count; ++i) {
            layout.removeView(iv[i]);
        }
        count = pointer_count;
        if(pointer_count > maxCount) pointer_count = maxCount;
        Log.d("DEBUG", "RandomFingerAcitivity::onTouchEvent() pointer_count: " + pointer_count);
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //한 개 포인트에 대한 DOWN을 얻을 때.
                makeImage(event, 0);
                vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(200);
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //두 개 이상의 포인트에 대한 DOWN을 얻을 때.
                for(int i = 0; i < pointer_count; i++) {
                    makeImage(event, i);
                }
                vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(200);
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if(ranNum != -1) {
                    for(int i = 0; i < pointer_count; i++) {
                        if (i == ranNum) {
                            makeImageForEnd(event, i);
                            is_finish = true;
                            return true;
                        }
                    }
                }
                for(int i = 0; i < pointer_count; i++) {
                    makeImage(event, i);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: //두 개 이상의 포인트에 대한 DOWN을 얻을 때.
                for(int i = 0; i < pointer_count; i++) {
                    makeImage(event, i);
                }
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                for(int i = 0; i < pointer_count; i++) {
                    iv[i].setVisibility(View.GONE);
                }
                startTime = System.currentTimeMillis();
                break;
        }

        return super.onTouchEvent(event);
    }

    public class CheckThread extends Thread {
            Random random = new Random();
            long time;
            @Override
            public void run() {
                Log.d("DEBUG", "CheckThread::run()");
                try {
                    run=true;
                    while (run) {
                        time = System.currentTimeMillis() - startTime;
                        if(time >= delay)
                            run = false;
                    }
                    ranNum = random.nextInt(count);
                    Log.d("DEBUG", "CheckThread::run() ranNum: " + ranNum);
                }catch(Exception e) {
                   // System.out.println(e.getMessage());
                }
                finally {
                   // System.out.println("시스템 종료");
                }
            }
    }


}

