package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import fr.axelc.cefimmeteo.databinding.ActivityMainBinding;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.utils.OpenWeatherMapApi;
import fr.axelc.cefimmeteo.utils.Util;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ActivityMainBinding mBinding;
    private OpenWeatherMapApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mContext = this;
        mBinding.floatingActionButtonFavorite.setOnClickListener(view -> goToFavoriteActivity());
        api = new OpenWeatherMapApi();

        if (Util.isActiveNetwork(mContext)) {
            api.requestCityByCoordinates("0.688891", "47.390026", city -> {
                Log.d("APP", "Got API response for " + city.getmName());
                runOnUiThread(() -> updateUi(city));
            });
        } else {
            updateViewNoConnection();
        }
    }

    private void goToFavoriteActivity() {
        Intent intent = new Intent(mContext, FavoriteActivity.class);
        intent.putExtra(Util.KEY_MESSAGE, mBinding.editTextMessage.getText().toString());
        startActivity(intent);
    }

    public void updateUi(City city) {
        mBinding.textViewCityName.setText(city.getmName());
        mBinding.textViewCityTemp.setText(city.getmTemperature());
        mBinding.textViewCityDesc.setText(city.getmDescription());
        mBinding.imageViewCityWeather.setImageResource(city.getmWeatherIcon());
    }

    public void updateViewNoConnection() {
        mBinding.linearLayoutCurrentCity.setVisibility(View.INVISIBLE);
        mBinding.floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
        mBinding.textViewNoConnection.setVisibility(View.VISIBLE);
    }
}