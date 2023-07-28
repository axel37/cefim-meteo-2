package fr.axelc.cefimmeteo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.databinding.ActivityMainBinding;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.utils.OpenWeatherMapApi;
import fr.axelc.cefimmeteo.utils.Util;
import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    private static final int GEOLOCATION_PERMISSION_REQUEST_CODE = 1132478;
    private Context mContext;
    private ActivityMainBinding mBinding;
    private OpenWeatherMapApi mApi;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private City mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup activity
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mContext = this;
        mBinding.floatingActionButtonFavorite.setOnClickListener(view -> goToFavoriteActivity());
        mApi = new OpenWeatherMapApi();
        // Setup geolocation
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = location -> requestAndDisplayWeather(
                String.valueOf(location.getLongitude()),
                String.valueOf(location.getLatitude())
        );

        //

        // Get weather data (if we're online + have geolocation)
        if (Util.isNetworkingActive(mContext)) {
            if (Util.isGeolocationPermissionGranted(mContext)) {
                configureLocationListener(LocationManager.GPS_PROVIDER);
            } else {
                requestGeolocationPermissions();
            }
        } else {
            displayNoConnectionMessage();
        }
    }










    private void requestAndDisplayWeather(String longitude, String latitude) {
        mLocationManager.removeUpdates(mLocationListener);
        mApi.requestCityByCoordinates(longitude, latitude, new OpenWeatherMapApi.OnResponseInterface() {
            @Override
            public void onSuccess(City city) {
                Log.d("APP", "Got API response for " + city.getmName());
                mCity = city;
                runOnUiThread(() -> displayCity());
            }

            @Override
            public void onError() {
                runOnUiThread(() -> showErrorToast(R.string.favorite_error_couldnt_get_weather));
            }
        });
    }



    public void displayCity() {
        City display = mCity;

        mBinding.textViewCityName.setText(display.getmName());
        mBinding.textViewCityTemp.setText(display.getmTemperature());
        mBinding.textViewCityDesc.setText(display.getmDescription());
        mBinding.imageViewCityWeather.setImageResource(display.getmWeatherIcon());
    }











    private void goToFavoriteActivity() {
        Intent intent = new Intent(mContext, FavoriteActivity.class);
        intent.putExtra(Util.KEY_MESSAGE, mBinding.editTextMessage.getText().toString());
        startActivity(intent);
    }

    public void displayNoConnectionMessage() {
        hideWeather();
        showMessage(R.string.no_connexion);
    }

    public void displayNoGeolocationMessage() {
        hideWeather();
        showMessage(R.string.no_geolocation);
    }

    private void hideWeather() {
        mBinding.linearLayoutCurrentCity.setVisibility(View.INVISIBLE);
        mBinding.floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
    }

    private void showMessage(int messageId) {
        mBinding.textViewErrorMessage.setVisibility(View.VISIBLE);
        mBinding.textViewErrorMessage.setText(messageId);
    }

    private void showErrorToast(int messageId) {
        Toast.makeText(mContext, getText(messageId), Toast.LENGTH_SHORT).show();
    }

    private void requestGeolocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, GEOLOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == GEOLOCATION_PERMISSION_REQUEST_CODE) {
            // I'm unsure about this if
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureLocationListener(LocationManager.GPS_PROVIDER);
            } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                configureLocationListener(LocationManager.NETWORK_PROVIDER);
            } else {
                displayNoGeolocationMessage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void configureLocationListener(String gpsProvider) {
        Log.d("APP", "configureLocationListener() called with: gpsProvider = [" + gpsProvider + "]");
        mLocationManager.requestLocationUpdates(gpsProvider, 0, 0, mLocationListener);
    }
}