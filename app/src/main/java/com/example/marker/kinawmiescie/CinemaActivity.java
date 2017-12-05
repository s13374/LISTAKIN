package com.example.marker.kinawmiescie;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.marker.kinawmiescie.cinemaFragments.CinemaDetails;
import com.example.marker.kinawmiescie.cinemaFragments.CinemaMap;
import com.example.marker.kinawmiescie.cinemaFragments.iFragmentChange;
import com.example.marker.kinawmiescie.models.Cinema;
//import com.example.marker.kinawmiescie.permission.PermissionManager;


public class CinemaActivity extends AppCompatActivity implements iFragmentChange{

    private int cinema_id = 0;
    private Bundle cinema_data;
    private boolean canDisplayMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
            cinema_id = extras.getInt("Cinema_ID");

        Cinema cinema = new DatabaseHandler(this).getCinema(cinema_id);
        setTitle(cinema.getName());

        CinemaDetails cinemaDetails = new CinemaDetails();

        cinema_data = new Bundle();
        cinema_data.putString("Name", cinema.getName());
        cinema_data.putString("Description", cinema.getDescription());
        cinema_data.putString("Address", cinema.getAddress());

        cinemaDetails.setArguments(cinema_data);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_placeholder, cinemaDetails, "details").commit();

//        if(PermissionManager.hasPermissionTo(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            canDisplayMap = true;
            if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
            showMapFragment();
//        }

    }


    @Override
    public void showMapFragment() {
        CinemaMap cinemaMap = new CinemaMap();
        cinema_data.putBoolean("canDisplayMap", canDisplayMap);
        cinemaMap.setArguments(cinema_data);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_map) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_placeholder, cinemaMap, "map");
            transaction.addToBackStack("map");
            transaction.commit();
        }else{
            fragmentManager.beginTransaction().add(R.id.fragment_map, cinemaMap, "map").commit();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(grantResults[0] == 0) {
//            canDisplayMap = true;
//            showMapFragment();
//        }else {
//            Toast.makeText(this, getText(R.string.location_permission_disable) + " "
//                    + getText(R.string.unable_to_show_map) + " "
//                    +getText(R.string.enable_location_permission), Toast.LENGTH_LONG).show();
//
//        }
//    }

}
