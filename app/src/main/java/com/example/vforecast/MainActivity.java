package com.example.vforecast;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;

//My  API Key: 4def3f878a0ff2a1d68b8fa8283f8aba

public class MainActivity extends AppCompatActivity {

    static TextView timezoneTextView;
    static TextView currentTemperatureTextView;
    static TextView descriptionTextView;
    static EditText locationEditText;

    static TextView degreesTextView;
    static ImageView weatherImageView;

    static TextView humidityTextView;
    static TextView pressureTextView;
    static TextView cloudinessTextView;
    static TextView windSpeedTextView;
    static TextView sunriseTextView;
    static TextView sunsetTextView;

    public static double latitude;
    public static double longitude;
    public static double windSpeedInMetersPerSecond;

    public static String timezone;
    public static String weatherDescription;
    public static String weatherIconId;

    public static int currentTemp;
    public static int currentUnixDate;
    public static int unixTimeSunrise;
    public static int unixTimeSunset;
    public static int currentPressure;
    public static int currentHumidityPercent;
    public static int cloudinessPercent;

    public void search (View view) {
        if (locationEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter a valid location!", Toast.LENGTH_SHORT).show();
        } else {
            LatLonTask latLonTask = new LatLonTask();
            latLonTask.execute("https://api.openweathermap.org/data/2.5/weather?q=" + locationEditText.getText().toString() +
                    "&appid=4def3f878a0ff2a1d68b8fa8283f8aba");
        }

    }

    public static void setTexts () {
        timezoneTextView.setText(timezone);
        currentTemperatureTextView.setText(String.valueOf(currentTemp));
        descriptionTextView.setText(capitalizeWords(weatherDescription));
        humidityTextView.setText(String.valueOf(currentHumidityPercent));
        pressureTextView.setText(String.valueOf(currentPressure));
        cloudinessTextView.setText(String.valueOf(cloudinessPercent));
        windSpeedTextView.setText(String.valueOf(windSpeedInMetersPerSecond));

        java.util.Date sunriseTime =new java.util.Date((long)unixTimeSunrise*1000);
        java.util.Date sunsetTime =new java.util.Date((long)unixTimeSunset*1000);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String sunriseTimeString = localDateFormat.format(sunriseTime);
        String sunsetTimeString = localDateFormat.format(sunsetTime);

        sunriseTextView.setText(sunriseTimeString);
        sunsetTextView.setText(sunsetTimeString);


        String imageLink = "https://openweathermap.org/img/wn/"+ weatherIconId + "@2x.png";
        Log.i("imageLink", imageLink);
        new setImageFromInternet(weatherImageView).execute(imageLink);

        setVisible();

        locationEditText.setText("");

        hideKeyboard(locationEditText.getContext());
    }

    private static void hideKeyboard(Context c) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
    }

    private static void setVisible() {
        timezoneTextView.setVisibility(View.VISIBLE);
        currentTemperatureTextView.setVisibility(View.VISIBLE);
        descriptionTextView.setVisibility(View.VISIBLE);
        degreesTextView.setVisibility(View.VISIBLE);
        weatherImageView.setVisibility(View.VISIBLE);
    }

    private static class setImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public setImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    public static String capitalizeWords(String description) {
        String words[] = description.split("\\s");
        String capitalizedWord = "";

        for (String word:words) {
            String first = word.substring(0,1);
            String afterFirst = word.substring(1);
            capitalizedWord += first.toUpperCase() + afterFirst + " ";
        }

        return capitalizedWord.trim();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timezoneTextView = findViewById(R.id.timezoneTextView);
        locationEditText = findViewById(R.id.locationEditText);
        currentTemperatureTextView = findViewById(R.id.currentTempTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        degreesTextView = findViewById(R.id.degreesTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        humidityTextView = findViewById(R.id.humidityTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        cloudinessTextView = findViewById(R.id.cloudinessTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        sunriseTextView = findViewById(R.id.sunriseTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);

    }
}