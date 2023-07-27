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
import fr.axelc.cefimmeteo.utils.OpenWeatherMapApi;
import fr.axelc.cefimmeteo.utils.Util;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private ArrayList<City> mCities;
    private ActivityFavoriteBinding binding;
    private FavoriteAdapter mAdapter;
    private Context mContext;
    private OpenWeatherMapApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());
        mContext = this;
        api = new OpenWeatherMapApi();

        binding.fab.setOnClickListener(view ->
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.favorite_add);
                    builder.setMessage(R.string.favorite_add_description);
                    View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
                    final EditText editTextCity = v.findViewById(R.id.favorite_add_edit_text_city);
                    builder.setView(v);
                    builder.setPositiveButton(getString(R.string.favorite_add_confirm), (dialogInterface, i) ->
                            api.requestCityByName(editTextCity.getText().toString(), new OpenWeatherMapApi.OnResponseInterface() {
                                @Override
                                public void onSuccess(City city) {
                                    Log.d("APP", "Got API response for " + city.getmName());
                                    runOnUiThread(() -> addCityToList(city));
                                }

                                @Override
                                public void onError() {
                                    runOnUiThread(() -> Toast.makeText(mContext, getText(R.string.favorite_error_couldnt_get_weather), Toast.LENGTH_SHORT).show());
                                }

                            })
                    );
                    builder.create().show();
                }
        );

        initCityList();
    }

    @Override
    protected void onDestroy() {
        Util.saveFavoriteCities(mContext, mCities);
        super.onDestroy();
    }

    /**
     * Create list of cities and bind it RecyclerViewCities
     */
    private void initCityList() {
        mCities = Util.loadFavoriteCities(mContext);

        binding.included.recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteAdapter(mContext, mCities);
        binding.included.recyclerViewCities.setAdapter(mAdapter);
    }

    private void addCityToList(City city) {
        mCities.add(0, city);
        mAdapter.notifyItemInserted(0);
    }
}