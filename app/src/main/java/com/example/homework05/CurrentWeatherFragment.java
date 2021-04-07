/*
Homework05
Group#A1Homework05.zip
Chase Scallion and Cormac Strickland
 */

package com.example.homework05;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CurrentWeatherFragment extends Fragment {
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private Data.City mCity;

    private final OkHttpClient client = new OkHttpClient();

    TextView currentWeatherTvLocation, currentWeatherTvTempResult, currentWeatherTvTempMaxResult, currentWeatherTvTempMinResult, currentWeatherTvDescriptionResult,
            currentWeatherTvHumidityResult, currentWeatherTvWindSpeedResult, currentWeatherTvWindDegreeResult, currentWeatherTvCloudinessResult;
    ImageView currentWeatherIv;
    String mIcon;
    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(Data.City mCity) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
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
        getActivity().setTitle("Current Weather");

        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        currentWeatherTvLocation = view.findViewById(R.id.currentWeatherTvLocation);
        currentWeatherTvTempResult = view.findViewById(R.id.currentWeatherTvTempResult);
        currentWeatherTvTempMaxResult = view.findViewById(R.id.currentWeatherTvTempMaxResult);
        currentWeatherTvTempMinResult = view.findViewById(R.id.currentWeatherTvTempMinResult);
        currentWeatherTvDescriptionResult = view.findViewById(R.id.currentWeatherTvDescriptionResult);
        currentWeatherTvHumidityResult = view.findViewById(R.id.currentWeatherTvHumidityResult);
        currentWeatherTvWindSpeedResult = view.findViewById(R.id.currentWeatherTvWindSpeedResult);
        currentWeatherTvWindDegreeResult = view.findViewById(R.id.currentWeatherTvWindDegreeResult);
        currentWeatherTvCloudinessResult = view.findViewById(R.id.currentWeatherTvCloudinessResult);
        currentWeatherIv = view.findViewById(R.id.currentWeatherIv);
        getCurrentWeather();

//        Toast.makeText(getActivity(), mIcon, Toast.LENGTH_SHORT).show();

        view.findViewById(R.id.currentWeatherBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToWeatherForecast(mCity);
            }
        });
        return view;
    }

    void getCurrentWeather() {
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q=" + mCity.getCity() + "&appid=1dd0edeeabb327727e29da27e578d2c4&units=imperial")
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
                        Weather currentWeather = new Weather();
                        JSONObject json = new JSONObject(response.body().string());
                        JSONObject jsonMain = json.getJSONObject("main");
                        JSONArray jsonWeather = json.getJSONArray("weather");
                        JSONObject jsonWeather2 = jsonWeather.getJSONObject(0);
                        JSONObject jsonWind = json.getJSONObject("wind");
                        JSONObject jsonClouds = json.getJSONObject("clouds");

                        currentWeather.setTemp(jsonMain.getString("temp") + " F");
                        currentWeather.setTempMax(jsonMain.getString("temp_max") + " F");
                        currentWeather.setTempMin(jsonMain.getString("temp_min") + " F");
                        currentWeather.setDescription(jsonWeather2.getString("description"));
                        currentWeather.setHumidity(jsonMain.getString("humidity") + "%");
                        currentWeather.setWindSpeed(jsonWind.getString("speed") + " miles/hour");
                        currentWeather.setWindDegree(jsonWind.getString("deg") + " degrees");
                        currentWeather.setCloudiness(jsonClouds.getString("all") + "%");
                        currentWeather.setIcon(jsonWeather2.getString("icon"));

                        setText(currentWeather);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    public void setText(Weather currentWeather) {
        Weather weather = currentWeather;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentWeatherTvLocation.setText(mCity.getCity() + ", " + mCity.getCountry());
                currentWeatherTvTempResult.setText(weather.getTemp());
                currentWeatherTvTempMaxResult.setText(weather.getTempMax());
                currentWeatherTvTempMinResult.setText(weather.getTempMin());
                currentWeatherTvDescriptionResult.setText(weather.getDescription());
                currentWeatherTvHumidityResult.setText(weather.getHumidity());
                currentWeatherTvWindSpeedResult.setText(weather.getWindSpeed());
                currentWeatherTvWindDegreeResult.setText(weather.getWindDegree());
                currentWeatherTvCloudinessResult.setText(weather.getCloudiness());
                Picasso.get().load("https://openweathermap.org/img/wn/" + weather.getIcon() + "@2x.png").into(currentWeatherIv);

            }
        });
    }
    CurrentWeatherFragment.CurrentWeatherListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CurrentWeatherFragment.CurrentWeatherListener) context;
    }

    interface CurrentWeatherListener {
        void goToWeatherForecast(Data.City city);
    }
}