package fr.axelc.cefimmeteo.utils;

import android.util.Log;
import fr.axelc.cefimmeteo.models.City;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;

public class OpenWeatherMapApi {
    private static final String API_URL_BASE = "https://api.openweathermap.org";
    private static final String API_URL_GEO = API_URL_BASE + "/geo/1.0/direct";
    private static final String API_URL_WEATHER = API_URL_BASE + "/data/2.5/weather";
    private static final String API_WEATHER_OPTIONS = "&units=metric&lang=fr";
    private static final String API_GEO_OPTIONS = "&limit=";
    private static final String API_AUTH = "&appid=8db629c9dd7b6e807faf04d2135f6d89";
    private final OkHttpClient httpClient = new OkHttpClient();

    public void requestCityByCoordinates(String lon, String lat, OnResponseInterface then) {
        String urlTemplate = API_URL_WEATHER + "?lon=%s&lat=%s" + API_WEATHER_OPTIONS + API_AUTH;
        Request request = new Request.Builder().url(String.format(urlTemplate, lon, lat)).build();

        Log.d("APP", "Requesting weather API for coordinates [lon=" + lon + ", lat=" + lat + "]");
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("APP", "requestCityByCoordinates Failure", e);
                then.onError();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        City city = new City(response.body().string());
                        then.onSuccess(city);

                    } catch (Exception e) {
                        then.onError();
                        Log.e("APP", "onResponse: API request failed", e);
                    }
                }
            }
        });
    }

    public void requestCityByName(String cityName, OnResponseInterface then) {
        String urlTemplate = API_URL_GEO + "?q=%s" + API_GEO_OPTIONS + API_AUTH;
        Request geocoderRequest = new Request.Builder().url(String.format(urlTemplate, cityName)).build();
        Log.d("APP", "Requesting weather data for city [name=" + cityName + "]");

        httpClient.newCall(geocoderRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                then.onError();
                Log.e("APP", "onFailure: API request failed", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONArray data = new JSONArray(responseString);
                        String lon = data.getJSONObject(0).getString("lon");
                        String lat = data.getJSONObject(0).getString("lat");

                        requestCityByCoordinates(lon, lat, then);
                    } catch (Exception e) {
                        then.onError();
                        Log.e("APP", "Error while parsing geocoding data.", e);
                    }
                } else {
                    then.onError();
                }
            }
        });
    }

    public interface OnResponseInterface {
        void onSuccess(City city);

        void onError();
    }
}

