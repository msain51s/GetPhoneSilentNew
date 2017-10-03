package com.get_phone_silent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.get_phone_silent.adapter.RegisterPlaceListAdatper;
import com.get_phone_silent.db.DB_Handler;
import com.get_phone_silent.model.LocationDataModel;
import com.get_phone_silent.service.MyAlarmReceiver;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private final static int LIST_REFRESH_REQUEST_CODE=121;
    Toolbar toolbar;
    TextView titleText;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RegisterPlaceListAdatper adatper;
    ArrayList<LocationDataModel> list;
    DB_Handler db_handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initToolBar();
        db_handler=new DB_Handler(this);
        titleText= (TextView) findViewById(R.id.toolbar_title_text);
        titleText.setText("Get Phone Silent");
        recyclerView= (RecyclerView) findViewById(R.id.register_place_recyclerView);
        initializeList();
        scheduleAlarm();
    }

    /*Method to in initialize toolbar*/
    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    public void initializeList(){
        list=db_handler.getRegisteredLocationDataList();
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adatper=new RegisterPlaceListAdatper(list,this);
        recyclerView.setAdapter(adatper);
    }

    public void refreshList(){
        list.clear();
        list.addAll(db_handler.getRegisteredLocationDataList());
        adatper.notifyDataSetChanged();
    }

    public void performRegisterPlaceClick(View view){
        Intent intent=new Intent(HomeScreen.this, LocationRegistration.class);
        startActivityForResult(intent,LIST_REFRESH_REQUEST_CODE);
    }

    public void updateLocationStatus(final String status, final int id){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db_handler.updateLocationDataStatus(status,id);
                list.clear();
                list.addAll(db_handler.getRegisteredLocationDataList());
                adatper.notifyDataSetChanged();
            }
        });

    }

    public void deleteLocation(final int id, final int position){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db_handler.deleteLocationData(id);
                list.clear();
                list.addAll(db_handler.getRegisteredLocationDataList());
                adatper.notifyItemRemoved(position);
                adatper.notifyItemRangeChanged(position,list.size());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == LIST_REFRESH_REQUEST_CODE) {
            refreshList();
        }
    }

    // Setup a recurring alarm every 15 seconds
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                15000, pIntent);
    }
}
