package com.get_phone_silent.model;

/**
 * Created by Administrator on 9/26/2017.
 */

public class LocationDataModel {
    public int Id;
    public String address;
    public String latitude;
    public String longitude;
    public String status;
    public String areaRadius;

    public String getAreaRadius() {
        return areaRadius;
    }

    public void setAreaRadius(String areaRadius) {
        this.areaRadius = areaRadius;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
