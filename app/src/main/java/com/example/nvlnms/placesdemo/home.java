package com.example.nvlnms.placesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class home extends AppCompatActivity {

    private static final String []types={"accounting",
            "airport",
            "amusement_park",
            "aquarium",
            "art_gallery",
            "atm",
            "bakery",
            "bank",
            "bar",
            "beauty_salon",
            "bicycle_store",
            "book_store",
            "bowling_alley",
            "bus_station",
            "café",
            "campground",
            "car_dealer",
            "car_rental",
            "car_repair",
            "car_wash",
            "casino",
            "cemetery",
            "church",
            "city_hall",
            "clothing_store",
            "convenience_store",
            "courthouse",
            "dentist",
            "department_store",
            "doctor",
            "electrician",
            "electronics_store",
            "embassy",
            "fire_station",
            "florist",
            "funeral_home",
            "furniture_store",
            "gas_station",
            "gym",
            "hair_care",
            "hardware_store",
            "hindu_temple",
            "home_goods_store",
            "hospital",
            "insurance_agency",
            "jewelry_store",
            "laundry",
            "lawyer",
            "library",
            "liquor_store",
            "local_government_office",
            "locksmith",
            "lodging",
            "meal_delivery",
            "meal_takeaway",
            "mosque",
            "movie_rental",
            "movie_theater",
            "moving_company",
            "museum",
            "night_club",
            "painter",
            "park",
            "parking",
            "pet_store",
            "pharmacy",
            "physiotherapist",
            "plumber",
            "police",
            "post_office",
            "real_estate_agency",
            "restaurant",
            "roofing_contractor",
            "rv_park",
            "school",
            "shoe_store",
            "shopping_mall",
            "spa",
            "stadium",
            "storage",
            "store",
            "subway_station",
            "supermarket",
            "synagogue",
            "taxi_stand",
            "train_station",
            "transit_station",
            "travel_agency",
            "veterinary_care",
            "zoo"
    };

    ArrayAdapter<String> adapter;
    SearchView sv=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView lv = (ListView) findViewById(R.id.lst);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, types);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=(String)(((TextView)view).getText()).toString();
                    search1(text);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem item=menu.findItem(R.id.search_bar);
        sv=(SearchView)item.getActionView();
        sv.setQueryHint("enter the type of place");

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search1(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.getDropDownViewTheme();

                return false;
            }
        });

        return true;
    }

    public void search1(String qur)
    {
        Intent i=new Intent(this,MapsActivity.class);

        i.putExtra("type",qur);

        startActivity(i);
    }
}
