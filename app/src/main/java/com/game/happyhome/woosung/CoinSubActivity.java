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
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class CoinSubActivity extends AppCompatActivity {
    TextView textView;
    int FrontCount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_sub);


        Intent intent = getIntent();
        FrontCount = intent.getExtras().getInt("count");

        textView = findViewById(R.id.textfinal);
        String numStr = String.valueOf(FrontCount);
        textView.setText(numStr);



    }
}

