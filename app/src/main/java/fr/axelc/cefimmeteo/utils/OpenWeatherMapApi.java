package fr.axelc.cefimmeteo.utils;

import android.util.Log;
import fr.axelc.cefimmeteo.models.City;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OpenWeatherMapApi {
    private static final String API_URL_BASE = "https://api.openweathermap.org";
    private static final String API_URL_WEATHER = API_URL_BASE + "/data/2.5/weather";
    private static final String API_WEATHER_OPTIONS = "&units=metric&lang=fr";
    private static final String API_AUTH = "&appid=8db629c9dd7b6e807faf04d2135f6d89";
    private final OkHttpClient httpClient = new OkHttpClient();

    public void requestCityByCoordinates(String lon, String lat, OnResponseInterface onResponse) {
        String urlTemplate = API_URL_WEATHER + "?lon=%s&lat=%s" + API_WEATHER_OPTIONS + API_AUTH;
        Request request = new Request.Builder().url(String.format(urlTemplate, lon, lat)).build();

        Log.d("APP", "Requesting weather API for coordinates [lon=" + lon + ", lat=" + lat + "]");
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("APP", "requestCityByCoordinates Failure", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        City city = new City(response.body().string());
                        onResponse.onResponse(city);

                    } catch (Exception e) {
                        Log.e("APP", "onResponse: API request failed", e);
                    }
                }
            }
        });
    }

    public interface OnResponseInterface {
        void onResponse(City city);
    }
}

