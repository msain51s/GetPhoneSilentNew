package com.get_phone_silent.utility;

/**
 * Created by Administrator on 9/26/2017.
 */
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class GeocodingLocation {
    private static final String TAG = "GeocodingLocation";

    public static void getLocationFromAddress(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append(address.getLatitude()).append(":");
                        sb.append(address.getLongitude());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result =  locationAddress +
                                ":" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "unable to get location"+":"+" "+":"+" ";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

  /*Get Address from latitude/longitude*/
    public static void getAddressFromLocation(final double latitude,final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List addressList = geocoder.getFromLocation(latitude,longitude, 1);
                    StringBuilder str = new StringBuilder();
                    if (addressList != null && addressList.size() > 0) {
                        if (geocoder.isPresent()) {
               /* Toast.makeText(getApplicationContext(),
                        "geocoder present", Toast.LENGTH_SHORT).show();*/
                            Address returnAddress = (Address) addressList.get(0);

                            String localityString = returnAddress.getLocality();
                            String city = returnAddress.getCountryName();
                            String region_code = returnAddress.getCountryCode();
                            String zipcode = returnAddress.getPostalCode();
                            String ss=returnAddress.getAddressLine(0);
                            String ss1=returnAddress.getAddressLine(1);
                            String ss2=returnAddress.getAddressLine(2);

                            /*str.append(localityString + ",");
                            str.append(city + "," + region_code + ",");
                            str.append(zipcode + ",");*/
                            str.append(ss );
                            if(ss1!=null)
                            str.append(","+ss1 + ",");
                            if(ss1!=null)
                            str.append(ss2);

                        } else {
                            Toast.makeText(context,
                                    "geocoder not present", Toast.LENGTH_SHORT).show();
                        }
                        result = str.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result =  result+":"+latitude+":"+longitude;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "unable to get location"+":"+" "+":"+" ";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
