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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CitiesFragment extends Fragment {


    ListView listView;
    ArrayAdapter<String> adapter;

    ArrayList<String> citiesList = new ArrayList<>();

    public CitiesFragment() {
        // Required empty public constructor
    }

    public static CitiesFragment newInstance() {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Cities");

        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        if (citiesList.isEmpty()) {
            for (Data.City city : Data.cities) {
                citiesList.add(city.getCity() + ", " + city.getCountry());
            }
        }

        listView = view.findViewById(R.id.citiesLv);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, citiesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mListener.goToCurrentWeather(Data.cities.get(position));
            }
        });

        return view;
    }

    CitiesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CitiesListener) context;
    }

    interface CitiesListener {
        void goToCurrentWeather(Data.City city);
    }

}