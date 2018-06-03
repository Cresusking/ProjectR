package com.example.cresu.projectr.utils;

import android.content.ContentResolver;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.cresu.projectr.R;

/**
 * Created by cresu on 24/04/2018.
 */

public class SoundsUtils {

    public SoundsUtils(){

    }

    public void playASound(String name) {

        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + VariablesGlobales.getInstance().getApplicationContext().getPackageName() + "/raw/"+name);
            Ringtone r = RingtoneManager.getRingtone(VariablesGlobales.getInstance().getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
