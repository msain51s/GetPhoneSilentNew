package com.get_phone_silent.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.get_phone_silent.HomeScreen;
import com.get_phone_silent.R;
import com.get_phone_silent.model.LocationDataModel;

import java.util.List;

/**
 * Created by Administrator on 9/29/2017.
 */

public class RegisterPlaceListAdatper extends RecyclerView.Adapter<RegisterPlaceListAdatper.MyViewHolder> {

    /**
     * Created by Administrator on 12/13/2016.
     */

        private List<LocationDataModel> list;
        private Activity mContext;


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView address_text;
            public SwitchCompat switchCompat;
            ImageView deleteIcon;

            public MyViewHolder(View view) {
                super(view);
          /*  Typeface roboto_light_typeface= Utils.getCustomFont(Application.mContext, FontType.ROBOTO_LIGHT)*/;

                address_text = (TextView) view.findViewById(R.id.location_address_text);
                switchCompat = (SwitchCompat) view.findViewById(R.id.switch_btn);
                deleteIcon= (ImageView) view.findViewById(R.id.delete_btn);

                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
            //    ((HomeScreen)mContext).deleteLocation();
            }
        }
          public RegisterPlaceListAdatper(List<LocationDataModel> list, Activity mContext) {
              this.list = list;
              this.mContext=mContext;
    }
        @Override
        public RegisterPlaceListAdatper.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.register_place_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RegisterPlaceListAdatper.MyViewHolder holder, int position) {
        final LocationDataModel model = list.get(position);
        holder.address_text.setText(model.getAddress());
        if(model.getStatus().equalsIgnoreCase("Enable"))
            holder.switchCompat.setChecked(true);
        else
            holder.switchCompat.setChecked(false);

            holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        ((HomeScreen)mContext).updateLocationStatus("Enable",model.getId());
                    }
                    else {
                        ((HomeScreen)mContext).updateLocationStatus("Disable",model.getId());
                    }
                }
            });

            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeScreen)mContext).deleteLocation(model.getId());
                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

