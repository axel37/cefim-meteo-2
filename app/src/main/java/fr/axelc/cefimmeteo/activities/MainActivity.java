package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import fr.axelc.cefimmeteo.R;
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

        // Get weather data unless we're offline
        if (Util.isActiveNetwork(mContext)) {
            api.requestCityByCoordinates("0.688891", "47.390026", new OpenWeatherMapApi.OnResponseInterface() {
                @Override
                public void onSuccess(City city) {
                    Log.d("APP", "Got API response for " + city.getmName());
                    runOnUiThread(() -> displayCity(city));
                }

                @Override
                public void onError() {
                    runOnUiThread(() -> showErrorToast(R.string.favorite_error_couldnt_get_weather));
                }
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

    public void displayCity(City city) {
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

    private void showErrorToast(int messageId) {
        Toast.makeText(mContext, getText(messageId), Toast.LENGTH_SHORT).show();
    }
}