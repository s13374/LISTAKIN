package com.example.marker.kinawmiescie.cinemaFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.marker.kinawmiescie.R;

public class CinemaDetails extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cinema_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView cinemaName = (TextView) getView().findViewById(R.id.cinemaNameET);
        TextView cinemaDescription = (TextView) getView().findViewById(R.id.cinemaDescription);
        TextView cinemaLocation = (TextView) getView().findViewById(R.id.cinemaLocation);
        ImageButton showOnMap = (ImageButton) getView().findViewById(R.id.showOnMap);


        cinemaName.setText(getArguments().getString("Name"));
        cinemaDescription.setText(getArguments().getString("Description"));
        cinemaLocation.setText(getArguments().getString("Address"));

        showOnMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        iFragmentChange fc= (iFragmentChange)getActivity();
        fc.showMapFragment();
    }
}