package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import fr.axelc.cefimmeteo.databinding.ActivityMainBinding;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.uistate.MainViewModel;
import fr.axelc.cefimmeteo.utils.Util;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String REQUEST_URL = "https://api.openweathermap.org/data/2.5/weather?lat=47.390026&lon=0.688891&appid=01897e497239c8aff78d9b8538fb24ea&units=metric&lang=fr";
    private Context mContext;
    private OkHttpClient mHttpClient;
    private ActivityMainBinding mBinding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mContext = this;
        mHttpClient = new OkHttpClient();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mBinding.floatingActionButtonFavorite.setOnClickListener(view -> goToFavoriteActivity());

        if (viewModel.getCity() != null) {
            Log.d("APP", "Displaying cached weather data.");
            updateUiWithViewModelCity();
        } else if (Util.isActiveNetwork(mContext)) {
            Log.d("APP", "Requesting weather data from OpenWeatherMap API.");
            doWeatherRequest();
        } else {
            Log.d("APP", "No internet connection. Can't display weather data.");
            updateViewNoConnection();
        }
    }

    private void doWeatherRequest() {
        Request request = new Request.Builder().url(REQUEST_URL).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    onWeatherRequestSuccess(response);
                }
            }
        });
    }

    private void onWeatherRequestSuccess(@NotNull Response response) {
        runOnUiThread(() -> {
            try {
                if (response.body() != null) {
                    updateViewModelCityFromJson(response.body().string());
                }
            } catch (JSONException | IOException e) {
                Log.e("APP", "run: updateUi", e);
            }
        });
    }

    private void goToFavoriteActivity() {
        Intent intent = new Intent(mContext, FavoriteActivity.class);
        intent.putExtra(Util.KEY_MESSAGE, mBinding.editTextMessage.getText().toString());
        startActivity(intent);
    }


    private void updateViewModelCityFromJson(String json) throws JSONException {
        viewModel.setCity(new City(json));
        updateUiWithViewModelCity();
    }

    private void updateUiWithViewModelCity() {
        mBinding.textViewCityName.setText(viewModel.getCity().getmName());
        mBinding.textViewCityTemp.setText(viewModel.getCity().getmTemperature());
        mBinding.textViewCityDesc.setText(viewModel.getCity().getmDescription());
        mBinding.imageViewCityWeather.setImageResource(viewModel.getCity().getmWeatherIcon());
    }

    private void updateViewNoConnection() {
        mBinding.linearLayoutCurrentCity.setVisibility(View.INVISIBLE);
        mBinding.floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
        mBinding.textViewNoConnection.setVisibility(View.VISIBLE);
    }
}