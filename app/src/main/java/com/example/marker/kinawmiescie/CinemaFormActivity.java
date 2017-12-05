package com.example.marker.kinawmiescie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marker.kinawmiescie.enums.ButtonAction;
import com.example.marker.kinawmiescie.models.Cinema;

public class CinemaFormActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name, street, city, description;
    private ButtonAction action;
    private DatabaseHandler db;
    private Cinema cinema;
    private int cinema_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_form);

        name = (TextView) findViewById(R.id.cinemaName);
        street = (TextView) findViewById(R.id.cinemaStreet);
        city = (TextView) findViewById(R.id.cinemaCity);
        description = (TextView) findViewById(R.id.cinemaDescription);
        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        if(intent != null)
            cinema_id = (int) intent.getExtras().getLong("CINEMA_ID");

        if(cinema_id == 0){
            action = ButtonAction.SAVE;
            cinema = new Cinema();
        }
        else {
            action = ButtonAction.UPDATE;
            cinema = db.getCinema(cinema_id);
            setUpTextViewsValues();
        }

        setButtonAction(action);
    }


    @Override
    public void onClick(View view) {
        String mName = name.getText().toString();
        String mStreet = street.getText().toString();
        String mCity = city.getText().toString();
        String mDescription = description.getText().toString();

        cinema.setName(mName);
        cinema.setStreet(mStreet);
        cinema.setCity(mCity);
        cinema.setDescription(mDescription);


        if(mStreet.isEmpty()){
            street.setError(getText(R.string.cinema_street_required));
        }else if(mCity.isEmpty()){
            city.setError(getText(R.string.cinema_city_required));
        }else{
            switch(action){
                case SAVE:
                    db.addCinema(cinema);
                    finish();
                    break;
                case UPDATE:
                    db.updateCinema(cinema);
                    finish();
                    break;
            }
        }
    }

    public void setButtonAction(ButtonAction action){
        this.action = action;
        Button actionButton = (Button) findViewById(R.id.cinemaActionBTN);
        setTitle(action == ButtonAction.SAVE ? getText(R.string.new_cinema) : getText(R.string.update_cinema));
        actionButton.setText(action == ButtonAction.SAVE ? getString(R.string.save_cinema) : getString(R.string.update_cinema));
        actionButton.setOnClickListener(this);
    }

    public void setUpTextViewsValues(){
        cinema = db.getCinema(cinema_id);
        name.setText(cinema.getName());
        street.setText(cinema.getStreet());
        city.setText(cinema.getCity());
        description.setText(cinema.getDescription());
    }
}
