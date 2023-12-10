package com.example.further;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static double[] fromString(String value) {
        return new Gson().fromJson(value, double[].class);
    }

    @TypeConverter
    public static String fromArray(double[] value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Double> arrayList) {
        if (arrayList == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Double>>() {}.getType();
        return gson.toJson(arrayList, type);
    }

    @TypeConverter
    public static ArrayList<Double> toArrayList(String json) {
        if (json == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Double>>() {}.getType();
        return gson.fromJson(json, type);
    }
}