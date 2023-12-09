package com.example.further;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    public static double[][] from2DString(String value) {
        return new Gson().fromJson(value, new TypeToken<double[][]>(){}.getType());
    }

    @TypeConverter
    public static String from2DArray(double[][] value) {
        return new Gson().toJson(value);
    }
}