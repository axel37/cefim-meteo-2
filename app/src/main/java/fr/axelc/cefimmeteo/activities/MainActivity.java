package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.databinding.ActivityMainBinding;
import fr.axelc.cefimmeteo.models.City;
import fr.axelc.cefimmeteo.utils.Util;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private static final String REQUEST_URL = "https://api.openweathermap.org/data/2.5/weather?lat=47.390026&lon=0.688891&appid=01897e497239c8aff78d9b8538fb24ea&units=metric&lang=fr";
    private Context mContext;
    private OkHttpClient mHttpClient;
    private City mCurrentCity;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mContext = this;
        mHttpClient = new OkHttpClient();

        mBinding.floatingActionButtonFavorite.setOnClickListener(view -> {
            goToFavoriteActivity();
        });

        if (Util.isActiveNetwork(mContext)) {
            Log.d("APP", "Je suis connecté");
            doWeatherRequest();


        } else {
            Log.d("APP", "Je ne suis pas connecté");
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response.body() != null) {
                        updateUi(response.body().string());
                    }
                } catch (JSONException | IOException e) {
                    Log.e("APP", "run: updateUi", e);
                }
            }
        });
    }

    private void goToFavoriteActivity() {
        Intent intent = new Intent(mContext, FavoriteActivity.class);
        intent.putExtra(Util.KEY_MESSAGE, mBinding.editTextMessage.getText().toString());
        startActivity(intent);
    }

    public void updateUi(String responseJson) throws JSONException {
        Log.d("APP", "updateUi() called with: responseJson = [" + responseJson + "]");
        mCurrentCity = new City(responseJson);
        mBinding.textViewCityName.setText(mCurrentCity.getmName());
        mBinding.textViewCityTemp.setText(mCurrentCity.getmTemperature());
        mBinding.textViewCityDesc.setText(mCurrentCity.getmDescription());
        mBinding.imageViewCityWeather.setImageResource(mCurrentCity.getmWeatherIcon());

    }

    public void updateViewNoConnection() {
        mBinding.linearLayoutCurrentCity.setVisibility(View.INVISIBLE);
        mBinding.floatingActionButtonFavorite.setVisibility(View.INVISIBLE);
        mBinding.textViewNoConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}