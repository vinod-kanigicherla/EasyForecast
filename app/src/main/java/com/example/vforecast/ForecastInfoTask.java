package com.example.vforecast;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.LastOwnerException;
import java.util.Scanner;
import java.lang.Math;

public class ForecastInfoTask extends AsyncTask<String, Void, String> {

    String result = "";

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connnection = (HttpURLConnection) url.openConnection();
            connnection.setRequestMethod("GET");
            connnection.connect();

            int responseCode = connnection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    result += sc.nextLine();
                }
            }

            connnection.disconnect();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.i("Success!", "Forecast Ran Successfully!");
        Log.i("JSON for One Call API", s);

        try {

            //Level 1
            JSONObject jsonObject = new JSONObject(s);

            MainActivity.timezone = jsonObject.getString("timezone");

            //Level 2
            String current = jsonObject.getString("current");
            JSONObject currentJSONObject = new JSONObject(current);

            MainActivity.currentTemp = (int) Math.round(currentJSONObject.getDouble("temp") - 273.15);
            MainActivity.currentUnixDate = currentJSONObject.getInt("dt");
            MainActivity.unixTimeSunrise = currentJSONObject.getInt("sunrise");
            MainActivity.unixTimeSunset = currentJSONObject.getInt("sunset");
            MainActivity.currentPressure = currentJSONObject.getInt("pressure");
            MainActivity.currentHumidityPercent = currentJSONObject.getInt("humidity");
            MainActivity.cloudinessPercent = currentJSONObject.getInt("clouds");
            MainActivity.windSpeedInMetersPerSecond = currentJSONObject.getDouble("wind_speed");

            //Level 3
            String weather = currentJSONObject.getString("weather");
            JSONArray weatherJSONArray = new JSONArray(weather);

            for (int i = 0; i < weatherJSONArray.length(); i++) {

                JSONObject jsonPart = weatherJSONArray.getJSONObject(i);

                MainActivity.weatherDescription = jsonPart.getString("description");
                MainActivity.weatherIconId = jsonPart.getString("icon");

                Log.i("desc", MainActivity.weatherDescription);
                Log.i("icon", MainActivity.weatherIconId);

            }



            MainActivity.setTexts();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
