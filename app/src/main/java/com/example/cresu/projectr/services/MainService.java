package com.example.cresu.projectr.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.utils.MyMqtt;

/**
 * Created by cresu on 06/05/2018.
 */

public class MainService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MyMqtt myMqtt = new MyMqtt(getApplicationContext(), HttpRequestConstantes.TOPIC);
        myMqtt.connect(null);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showNotificationReceived(){

    }
}
