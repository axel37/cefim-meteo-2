package fr.axelc.cefimmeteo.models;

import fr.axelc.cefimmeteo.R;
import org.json.JSONException;
import org.json.JSONObject;

public class City {
    private static final String ICON_DAY_CLEAR_SKY = "01d";
    private static final String ICON_NIGHT_CLEAR_SKY = "01n";
    private static final String ICON_DAY_FEW_CLOUDS = "02d";
    private static final String ICON_NIGHT_FEW_CLOUDS = "02n";
    private static final String ICON_DAY_SCATTERED_CLOUDS = "03d";
    private static final String ICON_NIGHT_SCATTERED_CLOUDS = "03n";
    private static final String ICON_DAY_BROKEN_CLOUDS = "04d";
    private static final String ICON_NIGHT_BROKEN_CLOUDS = "04n";
    private static final String ICON_DAY_SHOWER_RAIN = "09d";
    private static final String ICON_NIGHT_SHOWER_RAIN = "09n";
    private static final String ICON_DAY_RAIN = "10d";
    private static final String ICON_NIGHT_RAIN = "10n";
    private static final String ICON_DAY_THUNDERSTORM = "11d";
    private static final String ICON_NIGHT_THUNDERSTORM = "11n";
    private static final String ICON_DAY_SNOW = "13d";
    private static final String ICON_NIGHT_SNOW = "13n";
    private static final String ICON_DAY_MIST = "50d";
    private static final String ICON_NIGHT_MIST = "50n";
    private String mName;
    private String mDescription;
    private String mTemperature;
    private int mWeatherIcon;
    private String mJsonString;

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
        String iconId = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
        setIconFromIconId(iconId);
        mJsonString = json;
    }

    private void setIconFromIconId(String iconId) {
        switch (iconId) {
            case ICON_DAY_CLEAR_SKY:
                mWeatherIcon = R.drawable.weather_sunny_white;
                break;
            case ICON_NIGHT_CLEAR_SKY:
                mWeatherIcon = R.drawable.weather_clear_night_white;
                break;
            case ICON_DAY_FEW_CLOUDS:
            case ICON_NIGHT_FEW_CLOUDS:
                mWeatherIcon = R.drawable.weather_cloudy_white;
                break;
            case ICON_DAY_SCATTERED_CLOUDS:
            case ICON_NIGHT_SCATTERED_CLOUDS:
                mWeatherIcon = R.drawable.weather_cloudy_white;
                break;
            case ICON_DAY_BROKEN_CLOUDS:
            case ICON_NIGHT_BROKEN_CLOUDS:
                mWeatherIcon = R.drawable.weather_cloudy_white;
                break;
            case ICON_DAY_SHOWER_RAIN:
            case ICON_NIGHT_SHOWER_RAIN:
                mWeatherIcon = R.drawable.weather_rainy_white;
                break;
            case ICON_DAY_RAIN:
            case ICON_NIGHT_RAIN:
                mWeatherIcon = R.drawable.weather_rainy_white;
                break;
            case ICON_DAY_THUNDERSTORM:
            case ICON_NIGHT_THUNDERSTORM:
                mWeatherIcon = R.drawable.weather_thunder_white;
                break;
            case ICON_DAY_SNOW:
            case ICON_NIGHT_SNOW:
                mWeatherIcon = R.drawable.weather_snowy_white;
                break;
            case ICON_DAY_MIST:
            case ICON_NIGHT_MIST:
                mWeatherIcon = R.drawable.weather_foggy_white;
                break;
        }
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

    public String getmJsonString() {
        return mJsonString;
    }
}
