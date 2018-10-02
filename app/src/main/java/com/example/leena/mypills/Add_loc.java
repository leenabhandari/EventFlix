package com.example.leena.mypills;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.reversegeocode.ReverseGeocodeListener;
import com.mmi.apis.place.reversegeocode.ReverseGeocodeManager;
import com.mmi.apis.routing.DirectionListener;
import com.mmi.apis.routing.DirectionManager;
import com.mmi.apis.routing.Trip;
import com.mmi.layers.MapEventsOverlay;
import com.mmi.layers.MapEventsReceiver;
import com.mmi.layers.Marker;
import com.mmi.layers.PathOverlay;
import com.mmi.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Add_loc extends AppCompatActivity {
    MapmyIndiaMapView mapmyIndiaMapView1;
    MapView mMapView1;
    LatLng latLng;
    String Lon,Lat;
    String NAME;
    String DESC;
    String TIME;
    String DATE;
    int SPINNER;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.add_loc);
            Bundle bundle=new Bundle();

                NAME = getIntent().getStringExtra("NAME");
                DESC = getIntent().getStringExtra("DESC");
                TIME = getIntent().getStringExtra("TIME");
                SPINNER= getIntent().getIntExtra("SPINNER",0);

                DATE=getIntent().getStringExtra("DATE");
            System.out.println("SURHUUD"+NAME);
        mapmyIndiaMapView1 = (MapmyIndiaMapView) findViewById(R.id.map1);
        mMapView1 = mapmyIndiaMapView1.getMapView();
        mMapView1.setCenter(mMapView1.getMapCenter());
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete1);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {


                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                latLng = place.getLatLng();
                GeoPoint gp = new GeoPoint(latLng.latitude, latLng.longitude);
                mMapView1.setCenter(gp);
                mMapView1.setZoom(mMapView1.getMaxZoomLevel());
                Toast.makeText(getApplicationContext(), "Hold on the place",
                        Toast.LENGTH_LONG).show();

            }


            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getApplicationContext(), new MapEventsReceiver(){
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return true;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                Marker marker= new Marker(mMapView1);
                marker.setPosition(p);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mMapView1.getOverlays().add(marker);
                mMapView1.invalidate();

              Intent mainIntent=new Intent(Add_loc.this,Post_register.class);
                mainIntent.putExtra("Lat",marker.getPosition().getLatitude());
                mainIntent.putExtra("Lon",marker.getPosition().getLongitude());
                mainIntent.putExtra("NAME",NAME);
                mainIntent.putExtra("DESC",DESC);
                mainIntent.putExtra("TIME",TIME);
                mainIntent.putExtra("SPINNER",SPINNER);
                System.out.println("DADA!"+SPINNER);
                mainIntent.putExtra("DATE",DATE);
                System.out.println("SURHUDDDD"+marker.getPosition().getLatitude());
                startActivity(mainIntent);
                return false;
            }
        });
        mMapView1.getOverlays().add(mapEventsOverlay);
        mMapView1.invalidate();
    }

}
