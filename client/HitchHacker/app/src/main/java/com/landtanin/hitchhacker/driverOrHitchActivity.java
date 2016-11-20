package com.landtanin.hitchhacker;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        binding.driveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
