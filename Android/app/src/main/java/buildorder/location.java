package buildorder;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import home.medico.com.medicohome.R;


public class location extends FragmentActivity {

    GoogleMap googleMap;
    Button next;
    double latitude, longitude;
    Boolean locationgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        locationgot = false;
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapView);

        googleMap = supportMapFragment.getMap();


        next = (Button) findViewById(R.id.next);
        next.setTypeface(custom_font);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!locationgot) {
                    GPSTracker tracker = new GPSTracker(location.this);
                    if (!tracker.canGetLocation()) {
                        tracker.showSettingsAlert();
                        locationgot = false;
                    } else {
                        latitude = tracker.getLatitude();
                        longitude = tracker.getLongitude();
                        locationgot = true;
                    }

                    addMarker();

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));

                } else {


                    Intent i = new Intent(location.this, Build.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    i.putExtra("latitude", Double.toString(latitude));
                    i.putExtra("longitude", Double.toString(longitude));
                    startActivity(i);

                }
            }
        });


        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
            locationgot = false;
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();
            locationgot = true;
        }

        addMarker();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));
    }


    private void addMarker() {
        /** Make sure that the map has been initialised **/
        if (null != googleMap) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("My Location")
                    .draggable(false)
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

