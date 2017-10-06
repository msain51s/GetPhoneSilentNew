package com.get_phone_silent.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.get_phone_silent.db.DB_Handler;
import com.get_phone_silent.model.LocationDataModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 10/3/2017.
 */

public class BackgroundService extends IntentService {

    DB_Handler db_handler;
    ArrayList<LocationDataModel> list;
    CurrentLocationClass loc;
    public BackgroundService() {
        super("BackgroundService");
        db_handler=new DB_Handler(BackgroundService.this);

    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Log.d("service start time","now");
            if (db_handler != null) {
                list = db_handler.getRegisteredLocationDataListWithEnabledStatus();
                loc = new CurrentLocationClass(BackgroundService.this);
                String location = loc.getLocation();
                if (location != null) {
                    String[] locationArr = location.split(",");
                    double currentLat = Double.parseDouble(locationArr[0]);
                    double currentLng = Double.parseDouble(locationArr[1]);
                    boolean isInRadius = false;
                    if (list.size() > 0) {
                        for (LocationDataModel model : list) {
                            if ((calculateDistance(currentLat, currentLng, Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()))) <= Integer.parseInt(model.getAreaRadius())) {
                                isInRadius = true;
                                break;
                            }
                        }

                        Intent intent1 = new Intent("phone_silent");
                        intent.putExtra("isSilent", isInRadius);
                        sendBroadcast(intent1);

                    }

                }
            }
    }

    public int calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        int dist = (int) (earthRadius * c);

        Log.d("distance bw",""+dist);
        return dist;

    }


}
