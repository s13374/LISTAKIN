package com.example.marker.kinawmiescie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marker.kinawmiescie.models.Cinema;
import com.example.marker.kinawmiescie.permission.*;

import java.util.List;


public class MainActivity extends AppCompatActivity implements iPermissionManager, AdapterView.OnItemClickListener{

    private List<Cinema> cinemas;
    private ArrayAdapter<Cinema> adapter;
    private DatabaseHandler db;
    private final int CONTEXT_MENU_EDIT = 1;
    private final int CONTEXT_MENU_DELETE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!PermissionManager.hasPermissionTo(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            PermissionManager.makeRequest(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
        }

        ListView listOfCinema = (ListView) findViewById(R.id.listOfCinema);
        db = new DatabaseHandler(this);

       db.onUpgrade(db.getWritableDatabase(),1,1);  //force to recreate database

        cinemas = db.getAllCinemas();
        adapter = getMyArrayAdapter();

        listOfCinema.setOnItemClickListener(this);
        listOfCinema.setAdapter(adapter);
        registerForContextMenu(listOfCinema);

    }

    @Override
    protected void onResume() {
        refreshListView();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission", "Permission has been denied by user");
        } else {
            Log.i("Permission", "Permission has been granted by user");
        }
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_cinema:
                Intent intent = new Intent(getApplicationContext(), CinemaFormActivity.class);
                intent.putExtra("CINEMA_ID", 0l);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), CinemaActivity.class);
        int position = i+1;
        intent.putExtra("Cinema_ID", position);
        startActivity(intent);
    }

    public ArrayAdapter<Cinema> getMyArrayAdapter(){
        return new ArrayAdapter<Cinema>(this, android.R.layout.simple_list_item_2, android.R.id.text1, cinemas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                int MAX_LEN = 70;
                String desc = cinemas.get(position).getDescription();

                if(desc.length() > MAX_LEN)
                    desc = desc.substring(0, MAX_LEN) + "...";

                text1.setText(cinemas.get(position).getName());
                text2.setText(desc);
                return view;
            }
        };
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("My Context Menu");
        menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Edit");
        menu.add(Menu.NONE, CONTEXT_MENU_DELETE, Menu.NONE, "Delete");
    }

    public boolean onContextItemSelected (MenuItem item){
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long cinema_id = menuinfo.id+1;
        switch (item.getItemId()) {
            case CONTEXT_MENU_EDIT:
                Intent i = new Intent(this, CinemaFormActivity.class);
                i.putExtra("CINEMA_ID", cinema_id);
                startActivity(i);
                break;
            case CONTEXT_MENU_DELETE:
                db.deleteCinema(cinema_id);
                refreshListView();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void refreshListView(){
        cinemas.clear();
        cinemas.addAll(db.getAllCinemas());
        adapter.notifyDataSetChanged();
    }
}
