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
import androidx.core.content.ContextCompat;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.databinding.ActivityMainBinding;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.utils.OpenWeatherMapApi;
import fr.axelc.cefimmeteo.utils.Util;
import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {
    private static final String PERMISSION_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String PERMISSION_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE = 1132478;
    private Context mContext;
    private ActivityMainBinding mBinding;
    private OpenWeatherMapApi mApi;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mContext = this;
        mBinding.floatingActionButtonFavorite.setOnClickListener(view -> goToFavoriteActivity());
        mApi = new OpenWeatherMapApi();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = location -> requestAndDisplayWeather(
                String.valueOf(location.getLongitude()),
                String.valueOf(location.getLatitude())
        );

        // Get weather data (if we're online + have geolocation)
        if (Util.isNetworkingActive(mContext)) {
            // If geolocation on : get location and call api
            // Else : ask for permission
            // If permission denied : back out and show message

            boolean permissionsNotGranted = ContextCompat.checkSelfPermission(mContext, PERMISSION_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(mContext, PERMISSION_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
            if (permissionsNotGranted) {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_COARSE_LOCATION, PERMISSION_FINE_LOCATION}, REQUEST_CODE);
            } else {
                configureLocationListener();
            }
        } else {
            updateViewNoConnection();
        }
    }

    private void configureLocationListener() {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
    }

    private void requestAndDisplayWeather(String longitude, String latitude) {
        mApi.requestCityByCoordinates(longitude, latitude, new OpenWeatherMapApi.OnResponseInterface() {
            @Override
            public void onSuccess(City city) {
                Log.d("APP", "Got API response for " + city.getmName());
                mLocationManager.removeUpdates(mLocationListener);
                runOnUiThread(() -> displayCity(city));
            }

            @Override
            public void onError() {
                runOnUiThread(() -> showErrorToast(R.string.favorite_error_couldnt_get_weather));
            }
        });
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
        mBinding.textViewErrorMessage.setVisibility(View.VISIBLE);
        mBinding.textViewErrorMessage.setText(R.string.no_connexion);
    }

    public void updateViewNoGeolocation() {
        mBinding.linearLayoutCurrentCity.setVisibility(View.INVISIBLE);
        mBinding.floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
        mBinding.textViewErrorMessage.setVisibility(View.VISIBLE);
        mBinding.textViewErrorMessage.setText(R.string.no_geolocation);
    }

    private void showErrorToast(int messageId) {
        Toast.makeText(mContext, getText(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureLocationListener();
            } else {
                updateViewNoGeolocation();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}