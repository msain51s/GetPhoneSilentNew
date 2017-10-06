package com.get_phone_silent.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Administrator on 10/4/2017.
 */

public class PhoneSilentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String action=intent.getAction();
      //  boolean isSilent = intent.getExtras().getBoolean("isSilent");
           getSilent(context,true);
    }

    public void getSilent(Context context,boolean isInRadius){
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if(isInRadius) {
            //For Silent mode
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }else{
            //For Normal mode
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

        Log.d("Phone Silent","executed");
    }
}
