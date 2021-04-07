/*
Homework05
Group#A1Homework05.zip
Chase Scallion and Cormac Strickland
 */

package com.example.homework05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherForecastAdapter extends ArrayAdapter<Forecast> {
    public WeatherForecastAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Forecast> forecasts) {
        super(context, resource, forecasts);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_row, parent, false);
        }

        Forecast forecast = getItem(position);

        ImageView image = convertView.findViewById(R.id.weatherRowIv);
        TextView tvTimeDate = convertView.findViewById(R.id.weatherRowTvDate);
        TextView tvTemps = convertView.findViewById(R.id.weatherRowTvTemp);
        TextView tvHumidity = convertView.findViewById(R.id.weatherRowTvHumidity);
        TextView tvDescription = convertView.findViewById(R.id.weatherRowTvDescription);

        Picasso.get().load("https://openweathermap.org/img/wn/" + forecast.getIcon() + "@2x.png").into(image);
        tvTimeDate.setText(forecast.timeDate);
        tvTemps.setText(forecast.temp + " F    Max: " + forecast.tempMax + " F    Min: " + forecast.getTempMin()+ " F");
        tvHumidity.setText("Humidity: " + forecast.humidity + "%");
        tvDescription.setText(forecast.description);

        return convertView;
    }
}
