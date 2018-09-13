package com.game.happyhome.woosung;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.game.happyhome.woosung.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_MENU = 1001;
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "MainAcitivity: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRandomClicked(View v) {
        Log.d("DEBUG", "MainAcitivity: onRandomClicked()");
        Intent intent = new Intent(MainActivity.this, RandomFingerActivity.class);

        startActivity(intent);
    }
    public void onBombClicked(View v) {
        Log.d("DEBUG", "MainAcitivity: onBombClicked()");
        Intent intent = new Intent(MainActivity.this, BombActivity.class);

        startActivity(intent);
    }

    public void onCoinClicked(View v) {
        Log.d("DEBUG", "MainAcitivity: onCoinClicked()");
        Intent intent = new Intent(MainActivity.this, CoinActivity.class);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //2초이내에 취소 2번클릭이면 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
            finish();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
