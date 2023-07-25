package fr.axelc.cefimmeteo.models;

import org.json.JSONException;
import org.json.JSONObject;

public class City {
    private String mName;
    private String mDescription;
    private String mTemperature;
    private int mWeatherIcon;
    private int mCityId;
    private double mLatitude;
    private double mLongitude;
    private int mWeatherResIconWhite;

    public City(String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }

    public City(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        mName = jsonObject.getString("name");
        mDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        mTemperature = jsonObject.getJSONObject("main").getString("temp") + "°C";
//        mWeatherResIconWhite = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTemperature() {
        return mTemperature;
    }

    public int getmWeatherIcon() {
        return mWeatherIcon;
    }
}
