package com.example.vforecast;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LatLonTask extends AsyncTask<String, Void, String> {

    String result = "";

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connnection = (HttpURLConnection)url.openConnection();
            connnection.setRequestMethod("GET");
            connnection.connect();

            int responseCode = connnection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else  {
                Scanner sc = new Scanner(url.openStream());
                while(sc.hasNext())
                {
                    result += sc.nextLine();
                }
            }

            connnection.disconnect();

            return result;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s == null) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(s);
            String coordinates = jsonObject.getString("coord");
            JSONObject coordsObject = new JSONObject(coordinates);

            MainActivity.latitude = coordsObject.getDouble("lat");
            MainActivity.longitude = coordsObject.getDouble("lon");

            ForecastInfoTask forecastInfoTask = new ForecastInfoTask();
            forecastInfoTask.execute("https://api.openweathermap.org/data/2.5/onecall?lat=" + String.valueOf(MainActivity.latitude)
                    +  "&lon=" + String.valueOf(MainActivity.longitude) +  "&appid=4def3f878a0ff2a1d68b8fa8283f8aba");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}