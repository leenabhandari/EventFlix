package com.example.leena.mypills;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.routing.Advise;
import com.mmi.apis.routing.DirectionListener;
import com.mmi.apis.routing.DirectionManager;
import com.mmi.apis.routing.Trip;
import com.mmi.layers.PathOverlay;
import com.mmi.layers.UserLocationOverlay;
import com.mmi.layers.location.GpsLocationProvider;
import com.mmi.layers.location.IFollowLocationListener;
import com.mmi.layers.location.IMyLocationConsumer;
import com.mmi.layers.location.IMyLocationProvider;
import com.mmi.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Navigation_View extends AppCompatActivity {
    MapView mMapView1;
    UserLocationOverlay mLoc;
    public GeoPoint gp, gp1;
    ProgressDialog loading;
    TextToSpeech finalT;
    Double Lat, Lon;
    TextView t;
    ImageView I;

    ImageView dir;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navview);

        I = (ImageView) findViewById(R.id.imageView3);
        Lat = getIntent().getDoubleExtra("Posi", 0);
        Lon = getIntent().getDoubleExtra("Posi1", 0);
        dir=findViewById(R.id.imageView3);
        t=(TextView)findViewById(R.id.sugg);
        System.out.println("DADU" + Lat);
        gp = new GeoPoint(Lat, Lon);
        final Button b = (Button) findViewById(R.id.strtnav);
        MapmyIndiaMapView mapmyIndiaMapView1 = (MapmyIndiaMapView) findViewById(R.id.map3);
        mMapView1 = mapmyIndiaMapView1.getMapView();
        mLoc = new UserLocationOverlay(new GpsLocationProvider(getApplicationContext()), mMapView1);
        mLoc.enableMyLocation();
        mLoc.setCurrentLocationResId(R.drawable.rsz_gps);
        mMapView1.getOverlays().add(mLoc);
        mMapView1.invalidate();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoc.getMyLocation() == null) {
                    Toast.makeText(getApplicationContext(), "Waiting for location....", Toast.LENGTH_LONG).show();
                } else {
                    b.setVisibility(View.GONE);
                    mMapView1.setCenter(mLoc.getMyLocation());
                    mMapView1.setZoom(mMapView1.getMaxZoomLevel());
//                    mLoc.getFollowLocationListener().followMeEnabled();

                    final DirectionManager directionManager = new DirectionManager();
                    directionManager.setAdvises(DirectionManager.Advises.YES);
                    directionManager.setOptimize(true);

                    directionManager.setRouteType(DirectionManager.RouteType.SHORTEST);

                    directionManager.setAllowedAlternate(true);
                        directionManager.getDirections
                                (mLoc.getMyLocation(), gp, null, new DirectionListener() {
                                    @Override
                                    public void onResult(int code, final ArrayList<Trip> trips) {

                                        PathOverlay pathOverlay = new PathOverlay(getApplicationContext());

                                        pathOverlay.setColor(Color.parseColor("#40e0d0"));
                                        pathOverlay.setWidth(10);
                                        pathOverlay.setPoints((List<GeoPoint>) trips.get(0).getPath());
                                        mMapView1.animateTo(mLoc.getMyLocation());
                                        System.out.println(directionManager.getAdvises().name()+"SURHUDADDA");
                                        LocationListener locationListener=new LocationListener() {
                                            @Override
                                            public void onLocationChanged(Location location) {
                                                mMapView1.setCenter(mLoc.getMyLocation());
                                                mMapView1.animateTo(mLoc.getMyLocation());

                                            }
                                        };
                                        final String dada = trips.get(0).getAdvises().iterator().next().getHtmlInstructions();
                                                final String dada1 = Html.fromHtml(dada).toString();
                                    System.out.println("SSSK"+trips.get(0).getAdvises().get(2).getHtmlInstructions());
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

                                                // Stuff that updates the UI
                                                t.setText(dada1);
                                                if (dada1.contains("Left")) {
                                                    dir.setImageResource(R.drawable.leftarrow);

                                                } else if (dada1.contains("Right")) {
                                                    dir.setImageResource(R.drawable.rightarrow);

                                                } else if (dada1.contains("Straight")) {
                                                    dir.setImageResource(R.drawable.uparrow);

                                                } else if (dada1.contains("right") && dada1.contains("Straight")) {
                                                    dir.setImageResource(R.drawable.rightarrow);

                                                } else if (dada1.contains("left") && dada1.contains("Straight")) {
                                                    dir.setImageResource(R.drawable.leftarrow);

                                                } else if (dada1.contains("U turn")) {
                                                    dir.setImageResource(R.drawable.download);

                                                }
                                            }
                                        });


                                        finalT = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                finalT.setLanguage(Locale.ENGLISH);
                                                finalT.speak(dada1, TextToSpeech.QUEUE_ADD, null);
                                            }
                                        });
                                      //
                                        mMapView1.getOverlays().add(pathOverlay);
                                        mMapView1.invalidate();
                                        //                                                  popupWindow.dismiss();
                                        //code:0 success, 1 exception, 2 no result
                                        // array of Trip class. a trip represents a Route)


                                    }

                                });
                    }


                }




        });
    }
}

