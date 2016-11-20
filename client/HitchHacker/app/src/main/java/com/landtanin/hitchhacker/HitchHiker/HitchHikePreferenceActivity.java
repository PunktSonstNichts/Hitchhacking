package com.landtanin.hitchhacker.HitchHiker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.landtanin.hitchhacker.JSONObtained;
import com.landtanin.hitchhacker.R;
import com.landtanin.hitchhacker.databinding.ActivityHitchHikePreferenceBinding;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HitchHikePreferenceActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ActivityHitchHikePreferenceBinding binding;

    private int seatCounter = 1;

    private String apiKey;
    private String amountOfSeat, swtchstatus;


    // map
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;

    private android.location.Location mLastLocation;

    private String strLat, strLong, strAccuracy, strBearing;


    private String resultServer;

    private float desHeading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hitch_hike_preference);

        // map
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

////        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
////        mapFragment.getMapAsync(this);




        binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatCounter > 1) seatCounter--;
                else seatCounter = 1;

                binding.seatNumberTxt.setText(String.valueOf(seatCounter));
            }
        });

        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatCounter < 7) seatCounter++;
                else seatCounter = 7;

                binding.seatNumberTxt.setText(String.valueOf(seatCounter));
            }

        });

        binding.txtHitchDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectDatabase();

            }
        });

    }


    private void connectDatabase() {

        final HttpUrl myurl = HttpUrl.parse(JSONObtained.getAbsoluteUrl("accessHikes.php")).newBuilder().build();

        apiKey = getDefaultSharedPreferences(getBaseContext()).getString("shareAPI", "defaultStringIfNothingFound");
        amountOfSeat = binding.seatNumberTxt.getText().toString();

        if (binding.swtchSecure.getText().equals("off")) {
            swtchstatus = "0";
        } else if (binding.swtchSecure.getText().equals("on")) {
            swtchstatus = "1";
        }
        swtchstatus = "1"; // security mode on

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        // desLocation
//        GeoPoint newLoc = new GeoPoint(51.528308, -0.122225);
//        Barcode.GeoPoint desLoc = new Barcode.GeoPoint(1,51.528308, -0.122225);

        android.location.Location desLoc = new android.location.Location("London");
        desLoc.setLatitude(51.528308);
        desLoc.setLongitude(-0.122225);

        Log.d("TellMeCurrentLoc", String.valueOf(mLastLocation));

//        desHeading = mLastLocation.bearingTo(desLoc);

        // get Api key


        final RequestBody formBody = new FormBody.Builder()
                .add("f", "pushHike")
                .add("api", apiKey)
                .add("needed_seats", amountOfSeat)
                .add("security_mode", swtchstatus)
                .add("start_timestamp", String.valueOf(timestamp))
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

                try {

                    resultServer = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //TODO: get the API key
                Log.d("hitchHikeREsult", resultServer);

                JSONObject someJSONObj = null;
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
            strLong = String.valueOf(mLastLocation.getLongitude());

        }

        updateLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

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
        googleMap.setMyLocationEnabled(true);
        updateLocation();

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mGoogleApiClient.isConnected()) {

            android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (map != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
                Log.d("HitchHacker", "LOCATION UPDATE");
            }
        }
    }
}

