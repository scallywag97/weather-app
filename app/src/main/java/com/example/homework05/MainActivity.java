/*
Homework05
Group#A1Homework05.zip
Chase Scallion and Cormac Strickland
 */

package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesListener, CurrentWeatherFragment.CurrentWeatherListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    @Override
    public void goToCurrentWeather(Data.City city) {
        getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rootView, CurrentWeatherFragment.newInstance(city), "currentWeather")
                    .addToBackStack("cities")
                    .commit();
    }

    @Override
    public void goToWeatherForecast(Data.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, WeatherForecastFragment.newInstance(city), "weatherForecast")
                .addToBackStack("currentWeather")
                .commit();
    }
}