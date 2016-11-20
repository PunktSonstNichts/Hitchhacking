package com.landtanin.hitchhacker.Driver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.landtanin.hitchhacker.JSONObtained;
import com.landtanin.hitchhacker.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DriverModeActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST = 1;
    LocationManager locationManager;
    LocationListener locationListener;
    static Location location;
    private boolean permissionsGranted = false;
    private static JSONObject someJSONObj;

    AlertDialog.Builder mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_mode);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DriverModeActivity.location = location;
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };

        startLocationUpdate();

        callAsynchronRequester();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = true;
                } else {
                    Toast.makeText(this, "Permission Denied. Cant search the Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = false;
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
        } else {
            permissionsGranted = true;
        }
        if(permissionsGranted) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    private void stopLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = false;
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
        } else {
            permissionsGranted = true;
        }
        if(permissionsGranted) {
            locationManager.removeUpdates(locationListener);
        }
    }
    private void callAsynchronRequester() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            AsynchRequest performBackgroundTask = new AsynchRequest();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 50000);
    }

    private class AsynchRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final HttpUrl myurl = HttpUrl.parse(JSONObtained.getAbsoluteUrl("accessDriver.php")).newBuilder().build();
            // TODO
            String api = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shareAPI", "");
            final RequestBody formBody = new FormBody.Builder()
                    .add("f", "loginUser")
                    .add("api", api)
                    .add("lat", location.getLatitude()+"")
                    .add("long", location.getLongitude()+"").build();
            Response response = null;
            String resultServer;
            try {
                response = JSONObtained.getInstance().newCall(JSONObtained.postRequest(myurl, formBody)).execute();
                Log.d("CONNECTDATABASE", String.valueOf(response));
                resultServer = response.body().string();
                Log.d("RESULTSERVER", resultServer);
                someJSONObj = null;
                JSONArray array = new JSONArray(resultServer);
                someJSONObj = array.getJSONObject(0);
                // TODO Successfull or not?
                if (someJSONObj!=null) {

                    mAlertDialog = new AlertDialog.Builder(DriverModeActivity.this);

                    mAlertDialog.setMessage("A new person matched").setTitle("Woohoo!");

                    mAlertDialog.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {



                        }
                    });

                    mAlertDialog.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {



                        }
                    });

                    AlertDialog dialog = mAlertDialog.create();

//                            if (apiJSONstr!=null) {
//                                dialog.show();
//                            }


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
