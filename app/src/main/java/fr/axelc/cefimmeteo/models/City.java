package fr.axelc.cefimmeteo.models;

public class City {
    private String mName;
    private String mDescription;
    private String mTemperature;
    private int mWeatherIcon;

    public City(String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }
}