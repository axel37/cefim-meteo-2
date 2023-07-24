package fr.axelc.cefimmeteo.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.adapters.FavoriteAdapter;
import fr.axelc.cefimmeteo.databinding.ActivityFavoriteBinding;
import fr.axelc.cefimmeteo.models.City;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private ArrayList<City> mCities;
    private ActivityFavoriteBinding binding;
    private FavoriteAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());
        mContext = this;

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mCities = new ArrayList<City>();
        City city1 = new City("Tours", "Il fait chaud", "39°C", R.drawable.weather_sunny_white);
        City city2 = new City("Paris", "Il fait chaud", "41°C", R.drawable.weather_sunny_white);
        City city3 = new City("Toulouse", "Il fait très chaud", "43°C", R.drawable.weather_sunny_white);
        City city4 = new City("Marseille", "Il fait vraiment très chaud", "46°C", R.drawable.weather_sunny_white);

        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);
        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);
        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);

        binding.included.recyclerViewCities.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteAdapter(mContext, mCities);
        binding.included.recyclerViewCities.setAdapter(mAdapter);
    }
}