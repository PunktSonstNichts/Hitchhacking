package com.landtanin.hitchhacker.HitchHiker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.landtanin.hitchhacker.JSONObtained;
import com.landtanin.hitchhacker.R;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HitchHikePreferenceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

//    ActivityHitchHikePreferenceBinding binding;

    private int seatCounter = 1;

    private String apiKey;
    private String amountOfSeat, swtchstatus;


    // map
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    private Location mLastLocation;


    private String strLat, strLong, strAccuracy, strBearing;


    private String resultServer;

    private float desHeading;

    private LocationManager locationManager;

    private TextView txtHitchDone;
    private EditText destinationTxt;
    TextView seatNumberTxt;
    ImageButton minusBtn;

    ImageButton plusBtn;
    Switch swtch_secure;

    //--- audio record----

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitch_hike_preference);

        // ----------------Spinner-Start----------------

        final Spinner staticSpinner = (Spinner) findViewById(R.id.hitchhike_select_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> typeSelectAdapter = ArrayAdapter
                .createFromResource(this, R.array.event_type_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        typeSelectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(typeSelectAdapter);

//        Toast.makeText(this, String.valueOf(staticSpinner.getId()), Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            staticSpinner.setRevealOnFocusHint(true);
        }


        // ----------------Spinner-End----------------

        // map
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        txtHitchDone = (TextView) findViewById(R.id.txtHitchDone);
        destinationTxt = (EditText) findViewById(R.id.destination_txt);
        seatNumberTxt = (TextView) findViewById(R.id.seat_number_txt);
        minusBtn = (ImageButton) findViewById(R.id.minusBtn);
        plusBtn = (ImageButton) findViewById(R.id.plusBtn);
        swtch_secure = (Switch) findViewById(R.id.swtch_secure);


        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatCounter > 1) seatCounter--;
                else seatCounter = 1;

                seatNumberTxt.setText(String.valueOf(seatCounter));
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatCounter < 7) seatCounter++;
                else seatCounter = 7;

                seatNumberTxt.setText(String.valueOf(seatCounter));
            }

        });

        txtHitchDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectDatabase();

                Intent objIntent = new Intent(HitchHikePreferenceActivity.this, RecordVoice3.class);
                startActivity(objIntent);
                Toast.makeText(HitchHikePreferenceActivity.this, "Waiting for matching with drivers", Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void connectDatabase() {

        final HttpUrl myurl = HttpUrl.parse(JSONObtained.getAbsoluteUrl("accessHikes.php")).newBuilder().build();

        apiKey = getDefaultSharedPreferences(getBaseContext()).getString("shareAPI", "defaultStringIfNothingFound");
        amountOfSeat = seatNumberTxt.getText().toString();

        if (swtch_secure.getText().equals("off")) {
            swtchstatus = "0";
        } else if (swtch_secure.getText().equals("on")) {
            swtchstatus = "1";
        }
        swtchstatus = "1"; // security mode on

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        // desLocation
//        GeoPoint newLoc = new GeoPoint(51.528308, -0.122225);
//        Barcode.GeoPoint desLoc = new Barcode.GeoPoint(1,51.528308, -0.122225);

        Location desLoc = new Location("London");
        desLoc.setLatitude(51.528308);
        desLoc.setLongitude(-0.122225);


//        desHeading = mLastLocation.bearingTo(desLoc);

        // get Api key
        strLat = "51.760372";
        strLong = "-1.262584";

        final RequestBody formBody = new FormBody.Builder()
                .add("f", "pushHike")
                .add("api", apiKey)
                .add("needed_seats", amountOfSeat)
                .add("security_mode", swtchstatus)
                .add("start_timestamp", String.valueOf(timestamp.getTime()))
                .add("destination_lat", String.valueOf(51.528308))
                .add("destination_lon", String.valueOf(-0.122225))
                .add("destination_heading", String.valueOf(desHeading))
                .add("destination_name", "London")
                .add("current_lat", strLat)
                .add("current_lon", strLong)
                .add("current_name", "Oxford").build();


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

//                    Response response = JSONObtained.getInstance().newCall(JSONObtained.getRequest(myurl)).execute();
                Response response = null;

                try {

                    response = JSONObtained.getInstance().newCall(JSONObtained.postRequest(myurl, formBody)).execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.d("HitchHikeResponse", String.valueOf(response));
                //-----------TODO:cancel the comment---------

                try {

                    resultServer = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //TODO: get the API key
                Log.d("hitchHikeREsult", resultServer);
                //--------TODO:cancel the comment---------


//                JSONObject someJSONObj = null;
//                    JSONObject apiJSONstr = new JSONObject(resultServer);


//                    JSONArray array = new JSONArray(resultServer);
//                    someJSONObj = array.getJSONObject(0);
//                    strApiKey = someJSONObj.getString("api");

//                    someJSONObj = apiJSONstr.getJSONObject();
//                    strApiKey = someJSONObj.getString("api");


                return null;
            }

            @Override
            protected void onPostExecute(String s) {

//                recyclerAdapter.notifyDataSetChanged();

                super.onPostExecute(s);
            }

        }.execute();
//
//    }


    }

    // --------------------------------map stuff--------------------------------

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {

            strLat = String.valueOf(mLastLocation.getLatitude());
            Log.d("LAT", strLat);
            strLong = String.valueOf(mLastLocation.getLongitude());

            Log.d("Lng", strLong);
        }

        updateLocation();
        addEvents();
        
    }

    private void addEvents() {
        MarkerOptions marker = new MarkerOptions();
        marker.title("Sample event");
        marker.position(new LatLng(52.941190, -1.189594)); // Example event market
        map.addMarker(marker);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("LocationPractice", "SUSPENDED");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LocationPractice", "FAILED");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        
            return;
        }
        
        googleMap.setMyLocationEnabled(true);
        updateLocation();

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mGoogleApiClient.isConnected()) {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (map != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                Log.d("HitchHacker", "LOCATION UPDATE");
            }
        }
    }


}





