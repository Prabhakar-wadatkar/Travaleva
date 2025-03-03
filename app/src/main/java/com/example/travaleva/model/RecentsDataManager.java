package com.example.travaleva.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RecentsDataManager {

    private static final String PREF_NAME = "TravalevaPrefs";
    private static final String RECENT_DATA_KEY = "RecentDataList";

    public static void saveRecentData(Context context, List<RecentsData> recentsDataList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(recentsDataList);
        editor.putString(RECENT_DATA_KEY, json);
        editor.apply();
    }

    public static List<RecentsData> getRecentData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(RECENT_DATA_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RecentsData>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return null; // Return null if no data is found
        }
    }
}
