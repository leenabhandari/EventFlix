package com.example.leena.mypills;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mmi.LicenceManager;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.distance.DistanceManager;
import com.mmi.apis.place.reversegeocode.ReverseGeocodeListener;
import com.mmi.apis.place.reversegeocode.ReverseGeocodeManager;
import com.mmi.apis.routing.Advise;
import com.mmi.apis.routing.DirectionListener;
import com.mmi.apis.routing.DirectionManager;
import com.mmi.apis.routing.Trip;
import com.mmi.c.m;
import com.mmi.layers.BasicInfoWindow;
import com.mmi.layers.InfoWindow;
import com.mmi.layers.MapEventsOverlay;
import com.mmi.layers.MapEventsReceiver;
import com.mmi.layers.Marker;
import com.mmi.layers.MarkerInfoWindow;
import com.mmi.layers.PathOverlay;
import com.mmi.layers.UserLocationOverlay;
import com.mmi.layers.b;
import com.mmi.layers.location.GpsLocationProvider;
import com.mmi.util.GeoPoint;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class Map_data extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    MapmyIndiaMapView mapmyIndiaMapView;
    MapView mMapView;
    double js;
    UserLocationOverlay mLocationOverlay;

    DatabaseReference mRef;
    String venueLatString, venueLngString;
    Button regev,btn,find;
    ImageButton navimen;
    Button nearby;
    TextView tit, desc, timing;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private CircleImageView navProfileImg;
    private TextView navProfileName;
    private FirebaseAuth firebaseAuth;
    String currentUserId,name;
    private DatabaseReference userRef,postsRef;
    TextToSpeech finalT;
    String Curid;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermissions() {

        TedPermission.with(this)
                .setDeniedMessage("Application does not have necessary permissions")
                .setPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                       )
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Map_data.this.finish();
                    }
                })
                .check();
    }

    public void displayPromptForEnablingGPS(final Activity activity) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Click OK to go to"
                + " location services settings to let you do so.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                               // activity.startActivity(new Intent(action));
                               // EnableGPSAutoMatically();
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // toast("Success");
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //toast("GPS is not on");
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(Map_data.this, 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // toast("Setting change not allowed");
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        checkPermissions();
        if(!isLocationEnabled(this))
        {
           // displayPromptForEnablingGPS(this);
            EnableGPSAutoMatically();

        }


        firebaseAuth=FirebaseAuth.getInstance();


        find=findViewById(R.id.imageButton);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToFindActivity();
            }
        });
        currentUserId=firebaseAuth.getCurrentUser().getUid();
        Curid=firebaseAuth.getCurrentUser().getEmail();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("fullname")) {
                        name = dataSnapshot.child("fullname").getValue().toString();
                        navProfileName.setText(name);
                    }
                    if(dataSnapshot.hasChild("profile_img")) {
                        String image = dataSnapshot.child("profile_img").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(navProfileImg);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        currentUserId=firebaseAuth.getCurrentUser().getUid();

      //  toolbar=(Toolbar)findViewById(R.id.main_page_toolbar);
      //  setSupportActionBar(toolbar);
      //  getSupportActionBar().setTitle("Home");

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(Map_data.this,drawerLayout, R.string.drawer_open,R.string.drawer_close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        View navView=navigationView.inflateHeaderView(R.layout.navigation_header);

        navimen=findViewById(R.id.navclick);
        navimen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                else{
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        navProfileImg=(CircleImageView)navView.findViewById(R.id.nav_profile_img);
        navProfileName=(TextView)navView.findViewById(R.id.nav_username);

        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mypills-c2d76.firebaseio.com/events");


        mapmyIndiaMapView = (MapmyIndiaMapView) findViewById(R.id.map);
        mMapView = mapmyIndiaMapView.getMapView();
        GeoPoint geoPoint2= new GeoPoint(21.343958, 78.134765);
        Drawable dr = getResources().getDrawable(R.drawable.pin);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
        final Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 75, 75, true));
        mMapView.setCenter(geoPoint2);
        mLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(getApplicationContext()), mMapView);
        mLocationOverlay.setCurrentLocationDrawable(d);
        mLocationOverlay.enableMyLocation();
        mMapView.getOverlays().add(mLocationOverlay);
        mMapView.getOverlays().add(mLocationOverlay);
        mMapView.invalidate();
        if (mLocationOverlay.getMyLocation() != null) {
            {
                mMapView.setCenter(mLocationOverlay.getMyLocation());
                mMapView.setZoom(mMapView.getMaxZoomLevel());
            }
        }

        //retrieve
        regev =  findViewById(R.id.regev);
        mMapView.setCenter(geoPoint2);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

        ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot snapshot : children) {

                    //Log.v("E_VALUE","Data:"+child.getValue());
                    Double latitude = (Double) snapshot.child("Latitude").getValue();
                    Double longitude = (Double) snapshot.child("Longitude").getValue();
                    final String date2 = (String) snapshot.child("Date").getValue();
                    //SimpleDateFormat sdf = new SimpleDa   teFormat("yyyyMMdd");
                    Date date3 = null;
                    try {
                        date3 = new SimpleDateFormat("dd/MM/yyyy").parse(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    final String name2 = (String) snapshot.getKey();
                    final String descr = (String) snapshot.child("Description").getValue();
                    final String ingurl = (String) snapshot.child("imageURL").getValue();
                    final String typee1 = (String) snapshot.child("Type").getValue();
                    final String tim = (String) snapshot.child("timing").getValue();
                    final String orgz=(String) snapshot.child("Organiser").getValue();
                    final String cost=(String) snapshot.child("Cost").getValue();
                    Log.v("E_VALUE", "latitude:" + latitude);
                    Log.v("E_VALUE", "longitude:" + longitude);

                    GeoPoint geoPoint = new GeoPoint(latitude, longitude);
                    //ithe marker.getposition() ni tula marker che coordinates miltil
                    if (mLocationOverlay.getMyLocation() != null) {
                        if (mLocationOverlay.getMyLocation().distanceTo(geoPoint) < 10000) {
                            ReverseGeocodeManager reverseGeocodeManager;
                            new ReverseGeocodeManager().getPlace(geoPoint, new ReverseGeocodeListener() {
                                @Override
                                public void onResult(int i, com.mmi.apis.place.Place place) {
                                    showNotification("hello", "Woah! you are near " + place.getFullAddress());
                                }


                            });

                        }
                    }
                    Date date4 = new Date(System.currentTimeMillis());
                    Marker marker = new Marker(mMapView);
                    marker.setPosition(geoPoint);
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    try {
                        if (date2 != null & date3.after(date4)) {
                            mMapView.getOverlays().add(marker);
                        }
                    }
                    catch (NullPointerException e){


                    }
                    marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker, final MapView mapView) {


                            LayoutInflater layoutInflater
                                    = (LayoutInflater) getBaseContext()
                                    .getSystemService(LAYOUT_INFLATER_SERVICE);
                            final View popupView = layoutInflater.inflate(R.layout.popup,null);
                            final PopupWindow popupWindow = new PopupWindow(
                                    popupView,
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT);
                            Button dir = (Button) popupView.findViewById(R.id.get_directions);
                            Button btnDismiss = (Button) popupView.findViewById(R.id.dismis);
                            TextView D = (TextView) popupView.findViewById(R.id.des1);
                            TextView N = (TextView) popupView.findViewById(R.id.des);
                            TextView T = (TextView) popupView.findViewById(R.id.time1);
                            TextView tt = popupView.findViewById(R.id.typee);
                            TextView cc=popupView.findViewById(R.id.costee);
                            ImageView poster = popupView.findViewById(R.id.imgposter);
                            Button Rem=(Button)popupView.findViewById(R.id.removee);
                            Picasso.get().load(ingurl).placeholder(R.drawable.profile).into(poster);
                            N.setText(name2);
                            D.setText(date2);
                            T.setText(tim);
                            tt.setText(typee1);
                            cc.setText(cost);
                            popupWindow.setAnimationStyle(R.style.Animation);
                            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setTouchable(true);

                            popupWindow.showAsDropDown(popupView);
                            Rem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                 //   Log.d("HIIII","HIII")
                                    if(Curid==orgz) {
                                        mRef.child(name2).removeValue();
                                        marker.remove(mMapView);
                                        popupWindow.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Only Organiser can remove Event,Sorry!",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    popupWindow.dismiss();
                                }
                            });
                            dir.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mLocationOverlay.getMyLocation() == null) {
                                        Toast.makeText(getApplicationContext(), "Try reloading your location", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }else{
                                        Intent main=new Intent(Map_data.this,Navigation_View.class);
                                        main.putExtra("Posi",marker.getPosition().getLatitude());
                                        main.putExtra("Posi1",marker.getPosition().getLongitude());
                                        startActivity(main);
                                  /*  DirectionManager directionManager = new DirectionManager();
                                    directionManager.setAdvises(DirectionManager.Advises.YES);
                                    directionManager.setOptimize(true);
                                    directionManager.setRouteType(DirectionManager.RouteType.SHORTEST);

                                    directionManager.setAllowedAlternate(true);
                                    directionManager.getDirections
                                            (mLocationOverlay.getMyLocation(), marker.getPosition(), null, new DirectionListener() {
                                                @Override
                                                public void onResult(int code, final ArrayList<Trip> trips) {

                                                    PathOverlay pathOverlay = new PathOverlay(getApplicationContext());
                                                    mMapView.getOverlays().remove(pathOverlay.getOverlayIndex()-1);
                                                    pathOverlay.setColor(getResources().getColor(R.color.colorPrimaryDark));
                                                    pathOverlay.setWidth(10);
                                                    pathOverlay.setPoints((List<GeoPoint>) trips.get(0).getPath());
                                                    final String dada = trips.get(0).getAdvises().get(0).getHtmlInstructions();
                                                    final String dada1 = Html.fromHtml(dada).toString();
                                                    finalT = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                                        @Override
                                                        public void onInit(int i) {
                                                            finalT.setLanguage(Locale.ENGLISH);
                                                            finalT.speak(dada1, TextToSpeech.QUEUE_ADD, null);
                                                        }
                                                    });


                                                    mMapView.getOverlays().add(pathOverlay);
                                                    mMapView.invalidate();
                                                    //                                                  popupWindow.dismiss();
                                                    //code:0 success, 1 exception, 2 no result
                                                    // array of Trip class. a trip represents a Route)


                                                }

                                            });
                                    mLocationOverlay.getMyLocation().bearingTo(marker.getPosition());

                                    popupWindow.dismiss();*/
                                }
                                }

                            });

                            return false;
                        }
                    });

                    mMapView.invalidate();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //retrieve en
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {


                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                LatLng L = place.getLatLng();
                GeoPoint geoPoint = new GeoPoint(L.latitude, L.longitude);
                //store

              mMapView.setCenter(geoPoint);
              mMapView.setZoom(mMapView.getMaxZoomLevel());
         /*       marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, final MapView mapView) {
                        marker.getPosition();
                        System.out.println("SURHUD"+marker.getPosition());
                        DirectionManager directionManager = new DirectionManager();

                        directionManager.setRouteType(DirectionManager.RouteType.SHORTEST);
                        directionManager.setAllowedAlternate(true);
                        directionManager.getDirections
                                (mLocationOverlay.getMyLocation(), marker.getPosition(), null, new DirectionListener() {
                                    @Override
                                    public void onResult(int code, final ArrayList<Trip> trips) {

                                        PathOverlay pathOverlay = new PathOverlay(getApplicationContext());
                                        mMapView.getOverlays().remove(pathOverlay);
                                        pathOverlay.setColor(getResources().getColor(R.color.colorPrimaryDark));
                                        pathOverlay.setWidth(10);
                                        pathOverlay.setPoints((List<GeoPoint>) trips.get(0).getPath());

                                        mMapView.getOverlays().add(pathOverlay);
                                        mMapView.invalidate();
                                        //code:0 success, 1 exception, 2 no result
                                        // array of Trip class. a trip represents a Route)


                                                                            }

                                });

                        return false;
                    }

                });*/
            }


            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
//        MarkerInfoWindow markerInfoWindow = new MarkerInfoWindow(1, mMapView);


        btn = findViewById(R.id.currloc);
       // mLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(getApplicationContext()), mMapView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLocationOverlay.setCurrentLocationDrawable(d);
                mLocationOverlay.enableMyLocation();
                mMapView.getOverlays().add(mLocationOverlay);
                mMapView.invalidate();
                if (mLocationOverlay.getMyLocation() != null) {
                    {
                        mMapView.setCenter(mLocationOverlay.getMyLocation());
                        mMapView.setZoom(mMapView.getMaxZoomLevel());
                    }
                }
                //mMapView.setCenter(geoPoint);


            }
        });
//Add a Map View to your XML layout
        Log.d(LicenceManager.getInstance().getMapSDKKey(), "Surhud");



        //return false;
    }







    void showNotification(String title, String content) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Surhud dada",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("App cha notification");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.drawable.google) // notification icon
                .setContentTitle(title) // title for notification
                // message for notification
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setGroup("app")

                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), Map_data.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(m, mBuilder.build());

    }

    @Override
    protected void onStart() {
        super.onStart();
        regev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(Map_data.this,Post_register.class);

                startActivity(mainIntent);

            }
        });

    }


    private void UserMenuSelector(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_add:
                //Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();
               // sendUserToPostActivity();
                Intent mainIntent=new Intent(Map_data.this,Post_register.class);

                startActivity(mainIntent);
                break;

            case R.id.nav_home:
                drawerLayout.closeDrawers();
                Toast.makeText(this,"This is Home!",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_find:
                sendUserToFindActivity();
                // Toast.makeText(this,"Find",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                SendUserToLoginActivity();
                // Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent=new Intent(Map_data.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //  Toast.makeText(this,"starting activity",Toast.LENGTH_SHORT).show();
        startActivity(loginIntent);
    }

    private void sendUserToPostActivity() {
        Intent postIntent=new Intent(Map_data.this,PostActivity.class);

        startActivity(postIntent);
        finish();
    }


    private void sendUserToFindActivity() {
        Intent findIntent=new Intent(Map_data.this,FindActivity.class);
        findIntent.putExtra("currentUserId",currentUserId);
        findIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(findIntent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

