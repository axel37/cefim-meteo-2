package fr.axelc.cefimmeteo.activities;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import fr.axelc.cefimmeteo.models.City;

public class MainActivityViewModel extends ViewModel {
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    protected void onCleared() {
        Log.d("APP", "Viewmodel cleared; Cached data discarded.");
    }
}
