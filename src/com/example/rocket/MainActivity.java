package com.example.rocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        service = new Intent(this, RocketService.class);
    }

    public void start(View v) {
        //开启服务
        startService(service);
    }
    
    public void stop(View v) {
        //关闭服务
        stopService(service);
    }
}
