package fr.axelc.cefimmeteo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import fr.axelc.cefimmeteo.models.City;

import java.util.ArrayList;

public class Util {
    public static final String KEY_MESSAGE = "key_message";

    public static boolean isActiveNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void saveFavoriteCities(ArrayList<City> cities) {

    }


}
