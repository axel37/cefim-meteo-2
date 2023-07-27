package fr.axelc.cefimmeteo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import fr.axelc.cefimmeteo.models.City;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Util {
    public static final String KEY_MESSAGE = "key_message";
    private static final String PREFS_NAME = "MeteoCefimPreferences";
    private static final String PREFS_FAVORITE_CITIES = "FavoriteCities";

    public static boolean isActiveNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void saveFavoriteCities(Context context, ArrayList<City> cities) {
        JSONArray citiesJson = new JSONArray();

        for (City city : cities) {
            citiesJson.put(city.getmJsonString());
        }

        putStringInPreferences(context, PREFS_FAVORITE_CITIES, citiesJson.toString());
    }

    public static ArrayList<City> loadFavoriteCities(Context context) {
        ArrayList<City> cities = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(getStringFromPreferences(context, Util.PREFS_FAVORITE_CITIES));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCity = new JSONObject(jsonArray.getString(i));
                City city = new City(jsonObjectCity.toString());
                cities.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cities;
    }

    @NotNull
    private static String getStringFromPreferences(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    private static void putStringInPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}


