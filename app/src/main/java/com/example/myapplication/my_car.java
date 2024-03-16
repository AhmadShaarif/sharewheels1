package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class my_car extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView3;
    private AutoCompleteTextView autoCompleteTextView4;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_car);

        // Initialize Places
        String apiKey = "AIzaSyAzedGnY_elM2zM3diKLY_W_LqLnsbC44k";
        Places.initialize(getApplicationContext(), apiKey);
        placesClient = Places.createClient(this);

        // Initialize views
        autoCompleteTextView3 = findViewById(R.id.AutoCompleteTextView3);
        autoCompleteTextView3.setThreshold(1);
        autoCompleteTextView4 = findViewById(R.id.AutoCompleteTextView4);
        autoCompleteTextView4.setThreshold(1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_my_car);

        // BottomNavigationView item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.dashboard) {
                startActivity(new Intent(getApplicationContext(), Dashboard_Activity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_my_car) {

                return true;
            } else if (item.getItemId() == R.id.inbox) {
                startActivity(new Intent(getApplicationContext(), inbox.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.car_pool) {
                startActivity(new Intent(getApplicationContext(), carpool.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            }
            return false;
        });

        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // AutoCompleteTextView text change listener
        autoCompleteTextView3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    performAutoComplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // AutoCompleteTextView2 text change listener
        autoCompleteTextView4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    performAutoComplete(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performAutoComplete(String query) {
        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    predictionList = response.getAutocompletePredictions();
                    List<String> predictions = new ArrayList<>();
                    for (AutocompletePrediction prediction : predictionList) {
                        predictions.add(prediction.getPrimaryText(null).toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_dropdown_item_1line, predictions);
                    autoCompleteTextView3.setAdapter(adapter);
                    autoCompleteTextView4.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        // Handle error with API call
                        Toast.makeText(my_car.this, "Place not found: " + apiException.getStatusCode(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPlaceDetails(String placeId) {
        List<Place.Field> placeFields = new ArrayList<>();
        placeFields.add(Place.Field.ID);
        placeFields.add(Place.Field.NAME);
        placeFields.add(Place.Field.LAT_LNG);

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request)
                .addOnSuccessListener(fetchPlaceResponse -> {
                    Place place = fetchPlaceResponse.getPlace();
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        double latitude = latLng.latitude;
                        double longitude = latLng.longitude;
                        // Use latitude and longitude as needed
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        // Handle error with API call
                        Toast.makeText(my_car.this, "Place not found: " + apiException.getStatusCode(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
