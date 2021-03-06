package com.landtanin.hitchhacker.Driver;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.landtanin.hitchhacker.JSONObtained;
import com.landtanin.hitchhacker.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class DriverSettingActivity extends AppCompatActivity {

    private String resultServer;
    String apiKey = "";
    AlertDialog.Builder mAlertDialog;

    TextView txtDriverDone;

    private JSONObject apiJSONstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_setting);

        // ----------------Spinner-Start----------------

        final Spinner staticSpinner = (Spinner) findViewById(R.id.event_type_select_spinner);

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

        txtDriverDone = (TextView) findViewById(R.id.txtDriverDone);

        apiKey = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shareAPI","defaultStringIfNothingFound");

        txtDriverDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent objIntent = new Intent(DriverSettingActivity.this, DriverModeActivity.class);
                startActivity(objIntent);

            }
        });

//        while(apiJSONstr!=null){
//        connectDatabase();
//
//            //TODO :A;DLFKHJA;FDHALKSJFHA;SFH;AKSF;AKSJFH;KASH
//            new Thread(new Runnable() {
//                @Override
//                public void run()
//                {
//                    // do the thing that takes a long time
//
//                    try {
//                        TimeUnit.SECONDS.sleep(10);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run()
//                        {
//
//                            // show Pop-up
//                            mAlertDialog = new AlertDialog.Builder(DriverSettingActivity.this);
//
//                            mAlertDialog.setMessage("A new person matched").setTitle("Woohoo!");
//
//                            mAlertDialog.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//
//
//                                }
//                            });
//
//                            mAlertDialog.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//
//
//
//                                }
//                            });
//
//                            AlertDialog dialog = mAlertDialog.create();
//
////                            if (apiJSONstr!=null) {
////                                dialog.show();
////                            }
////
//
//                        }
//
//                    });
//
//                }
//
//            }).start();
//
//        }

    }

    private void connectDatabase() {

        final HttpUrl myurl = HttpUrl.parse(JSONObtained.getAbsoluteUrl("accessHikes.php")).newBuilder().build();

        apiKey = getDefaultSharedPreferences(getBaseContext()).getString("shareAPI", "defaultStringIfNothingFound");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        // desLocation
//        GeoPoint newLoc = new GeoPoint(51.528308, -0.122225);
//        Barcode.GeoPoint desLoc = new Barcode.GeoPoint(1,51.528308, -0.122225);

        Location desLoc = new Location("London");
        desLoc.setLatitude(51.528308);
        desLoc.setLongitude(-0.122225);


//        desHeading = mLastLocation.bearingTo(desLoc);

        // get Api key
        String strLat = "55.2";
        String strLong = "0.1";

        final RequestBody formBody = new FormBody.Builder()
                .add("f", "getHikerRequests")
                .add("api", apiKey)
                .add("driver_lat", strLat)
                .add("driver_lon", strLong).build();

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
                    Log.d("DriverSetting", resultServer);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //TODO: get the API key
                Log.d("hitchHikeResult", resultServer);

                JSONObject someJSONObj = null;

                try {
                    apiJSONstr = new JSONObject(resultServer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DriverData", String.valueOf(apiJSONstr));


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
}
