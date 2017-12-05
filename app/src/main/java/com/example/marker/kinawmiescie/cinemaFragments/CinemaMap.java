package com.example.marker.kinawmiescie.cinemaFragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.marker.kinawmiescie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CinemaMap extends Fragment implements OnMapReadyCallback{

    private Address address;
    TextView distance;
    Bundle data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = getArguments();
        return inflater.inflate(R.layout.fragment_cinema_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        distance = (TextView) getActivity().findViewById(R.id.cinemaName);

        Geocoder geocoder = new Geocoder(getContext());
        try {
            address = geocoder.getFromLocationName(data.getString("Address"), 1).get(0);
        } catch (Exception e) {
            if(getActivity().findViewById(R.id.fragment_map) == null)
                getFragmentManager().popBackStack();
            else
                getFragmentManager().beginTransaction().remove(this).commit();
        }

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;

            LatLng cinema = new LatLng(address.getLatitude(), address.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(cinema).title(data.getString("Name")));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cinema, 16f));


    }
    public void onPause() {
        super.onPause();
    }
}