package com.landtanin.hitchhacker;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.landtanin.hitchhacker.Driver.DriverSettingActivity;
import com.landtanin.hitchhacker.HitchHiker.HitchHikePreferenceActivity;
import com.landtanin.hitchhacker.databinding.ActivityDriverOrHitchBinding;

public class driverOrHitchActivity extends AppCompatActivity {

    ActivityDriverOrHitchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_driver_or_hitch);


//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
////        final String defaultValue = sharedPref.getString("shareApi", "nothing");
//        final String retrievedApiKey = sharedPref.getString("adsf", "noooo");


        binding.driveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String apiKey = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shareAPI", "defaultStringIfNothingFound");
                Log.d("yourgood", apiKey);

                Intent objIntent = new Intent(driverOrHitchActivity.this, DriverSettingActivity.class);
                startActivity(objIntent);

            }
        });

        binding.hitchHikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent objIntent = new Intent(driverOrHitchActivity.this, HitchHikePreferenceActivity.class);
                startActivity(objIntent);

            }
        });

    }
}
