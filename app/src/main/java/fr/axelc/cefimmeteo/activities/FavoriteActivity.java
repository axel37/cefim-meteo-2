package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.adapters.FavoriteAdapter;
import fr.axelc.cefimmeteo.databinding.ActivityFavoriteBinding;
import fr.axelc.cefimmeteo.models.City;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private static final String API_URL_BASE = "https://api.openweathermap.org";
    private static final String API_URL_GEO = API_URL_BASE + "/geo/1.0/";
    private static final String API_URL_WEATHER = API_URL_BASE + "/data/2.5/weather";
    private static final String API_WEATHER_OPTIONS = "&units=metric&lang=fr";
    private static final String API_AUTH = "&appid=8db629c9dd7b6e807faf04d2135f6d89";
    private ArrayList<City> mCities;
    private ActivityFavoriteBinding binding;
    private FavoriteAdapter mAdapter;
    private Context mContext;
    private OkHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());
        mContext = this;
        httpClient = new OkHttpClient();

        binding.fab.setOnClickListener(view ->
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.favorite_add);
                    builder.setMessage(R.string.favorite_add_description);
                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
                    final EditText editTextCity = v.findViewById(R.id.favorite_add_edit_text_city);
                    builder.setView(v);
                    builder.setPositiveButton(getString(R.string.favorite_add_confirm), (dialogInterface, i) ->
                            doWeatherRequests(editTextCity.getText().toString())
                    );
                    builder.create().show();
                }
        );

        initCityList();
    }

    /**
     * Create list of cities and bind it RecyclerViewCities
     */
    private void initCityList() {
        mCities = new ArrayList<>();

        binding.included.recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteAdapter(mContext, mCities);
        binding.included.recyclerViewCities.setAdapter(mAdapter);
    }

    /**
     * Request API for weather data by city name then display results.
     */
    private void doWeatherRequests(String cityName) {
        String urlTemplate = API_URL_GEO + "direct?q=%s&limit=1" + API_AUTH;
        Request geocoderRequest = new Request.Builder().url(String.format(urlTemplate, cityName)).build();
        httpClient.newCall(geocoderRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showErrorToastAndLog(
                        R.string.favorite_error_cant_find_city,
                        "Error while requesting Geocoding data ",
                        e
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    doWeatherRequestFromGeocodingResponse(response);
                } else {
                    showErrorToastAndLog(
                            R.string.favorite_error_cant_find_city,
                            "Geocoding API returned error response = [" + response + "]"
                    );
                }
            }
        });
    }

    private void doWeatherRequestFromGeocodingResponse(@NotNull Response response) {
        try {
            String responseString = response.body().string();
            JSONArray data = new JSONArray(responseString);
            String lon = data.getJSONObject(0).getString("lon");
            String lat = data.getJSONObject(0).getString("lat");

            doWeatherRequestOnCoordinates(lon, lat);

        } catch (Exception e) {
            showErrorToastAndLog(
                    R.string.favorite_error_cant_find_city,
                    "Error while parsing geocoding data", e
            );
        }
    }


    private void doWeatherRequestOnCoordinates(String longitude, String latitude) {
        String urlTemplate = API_URL_WEATHER + "?lat=%s&lon=%s" + API_WEATHER_OPTIONS + API_AUTH;
        Request weatherRequest = new Request.Builder().url(String.format(urlTemplate, latitude, longitude)).build();
        httpClient.newCall(weatherRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showErrorToastAndLog(
                        R.string.favorite_error_cant_find_city,
                        "onWeatherRequestFailure: Error while getting geocoding data",
                        e
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                onWeatherRequestResponse(response);
            }
        });
    }

    private void onWeatherRequestResponse(@NotNull Response response) {
        if (response.isSuccessful()) {
            onWeatherRequestResponseSuccess(response);
        } else {
            showErrorToastAndLog(
                    R.string.favorite_error_couldnt_get_weather,
                    "Weather API returned error response : " + response
            );
        }
    }

    private void onWeatherRequestResponseSuccess(@NotNull Response response) {
        try {
            String responseString = response.body().string();
            runOnUiThread(() -> {
                addCityToListFromJson(responseString);
            });
        } catch (Exception e) {
            showErrorToastAndLog(
                    R.string.favorite_error_couldnt_get_weather,
                    "Error while getting weather data",
                    e
            );
        }
    }

    private void addCityToListFromJson(String responseString) {
        try {
            City city = new City(responseString);
            mCities.add(0, city);
            mAdapter.notifyItemInserted(0);
        } catch (JSONException e) {
            showErrorToastAndLog(
                    R.string.favorite_error_cant_add_favorite,
                    "addCityToListFromJson: Error while adding city to list",
                    e
            );
        }
    }

    private void showErrorToastAndLog(int toastMessageId, String logMessage) {
        displayToast(getString(toastMessageId));
        Log.d("APP", "notifyError: " + logMessage);
    }

    private void showErrorToastAndLog(int toastMessageId, String logMessage, Exception exception) {
        displayToast(getString(toastMessageId));
        Log.e("APP", "notifyError: " + logMessage, exception);
    }

    /**
     * Display a Toast on UI thread
     */
    private void displayToast(String message) {
        runOnUiThread(() -> Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show());
    }
}