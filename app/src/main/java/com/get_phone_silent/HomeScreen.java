package com.get_phone_silent;

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

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

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

    public void performRegisterPlaceClick(View view){
        Intent intent=new Intent(HomeScreen.this, LocationRegistration.class);
        startActivity(intent);
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

    public void deleteLocation(final int id){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                db_handler.deleteLocationData(id);
                list.clear();
                list.addAll(db_handler.getRegisteredLocationDataList());
                adatper.notifyDataSetChanged();
            }
        });
    }
}
