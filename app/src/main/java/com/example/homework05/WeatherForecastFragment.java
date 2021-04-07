/*
Homework05
Group#A1Homework05.zip
Chase Scallion and Cormac Strickland
 */

package com.example.homework05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeatherForecastFragment extends Fragment {

    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private Data.City mCity;

    private final OkHttpClient client = new OkHttpClient();
    WeatherForecastAdapter adapter;
    ListView listView;
    TextView weatherForecastTv;
    public WeatherForecastFragment() {
        // Required empty public constructor
    }


    public static WeatherForecastFragment newInstance(Data.City mCity) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, mCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (Data.City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Weather Forecast");

        View view = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

        weatherForecastTv = view.findViewById(R.id.weatherForecastTv);
        weatherForecastTv.setText(mCity.getCity() + ", " + mCity.getCountry());

        listView = view.findViewById(R.id.weatherForecastLv);
        getWeatherForecast();

        return view;
    }

    void getWeatherForecast() {
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?q=" + mCity.getCity() + "&appid=1dd0edeeabb327727e29da27e578d2c4&units=imperial")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        ArrayList<Forecast> forecasts = new ArrayList<>();
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray forecastsJson = json.getJSONArray("list");

                        for(int i = 0; i < forecastsJson.length(); i ++) {
                            JSONObject jsonForecastObject = forecastsJson.getJSONObject(i);
                            JSONObject jsonMain = jsonForecastObject.getJSONObject("main");
                            JSONArray jsonWeather = jsonForecastObject.getJSONArray("weather");
                            JSONObject jsonWeather2 = jsonWeather.getJSONObject(0);

                            Forecast forecast = new Forecast();

                            forecast.setTemp(jsonMain.getString("temp"));
                            forecast.setTempMax(jsonMain.getString("temp_max"));
                            forecast.setTempMin(jsonMain.getString("temp_min"));
                            forecast.setTimeDate(jsonForecastObject.getString("dt_txt"));
                            forecast.setDescription(jsonWeather2.getString("description"));
                            forecast.setHumidity(jsonMain.getString("humidity"));
                            forecast.setIcon(jsonWeather2.getString("icon"));

                            forecasts.add(forecast);
                        }
                        setAdapter(forecasts);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    public void setAdapter(ArrayList<Forecast> mForecasts) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new WeatherForecastAdapter(getContext(), R.layout.weather_row, mForecasts);
                listView.setAdapter(adapter);
            }
        });
    }
}