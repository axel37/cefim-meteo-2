package fr.axelc.cefimmeteo.uistate;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import fr.axelc.cefimmeteo.models.City;

public class MainViewModel extends ViewModel {
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    protected void onCleared() {
        Log.d("APP", "MainViewModel cleared. Destroying cached city data.");
    }
}
