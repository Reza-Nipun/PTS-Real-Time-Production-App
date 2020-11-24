package com.example.ismef17.pts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UnitSelectionActivity extends AppCompatActivity {

    ImageView mIdEcofab;
    ImageView mLogoutBtnImageView;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_selection);

        mIdEcofab = (ImageView) findViewById(R.id.idEcofab);
        mLogoutBtnImageView = (ImageView) findViewById(R.id.logoutBtnImageView);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mIdEcofab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("unit", "EcoFab");
                startActivity(i);
            }
        });

        mLogoutBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("pin", "");
                editor.apply();
                editor.commit();

                Intent lo = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(lo);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent redi = new Intent(getApplicationContext(), UnitSelectionActivity.class);
        startActivity(redi);
    }
}
