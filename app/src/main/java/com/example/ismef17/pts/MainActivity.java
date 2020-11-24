package com.example.ismef17.pts;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String unit;

    ImageView mbackBtnImageView;
    ImageView imageViewSewingPerformance;
    TextView mIdUnitPageTitle;
    ImageView mHomeBtnImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIdUnitPageTitle = (TextView) findViewById(R.id.idUnitPageTitle);
        mbackBtnImageView = (ImageView) findViewById(R.id.backBtnImageView);
        mHomeBtnImageView = (ImageView) findViewById(R.id.homeBtnImageView);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if( b != null)
        {
            unit = (String) b.get("unit");
            mIdUnitPageTitle.setText(unit+" PTS");
        }

        imageViewSewingPerformance = (ImageView) findViewById(R.id.sewingPerformance);

        imageViewSewingPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), SewingPerformanceActivity.class);

                i.putExtra("unit", unit);
//              potentially add data to the intent
                startActivity(i);
            }
        });

        mHomeBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UnitSelectionActivity.class);
                startActivity(i);
            }
        });

        mbackBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UnitSelectionActivity.class);
                i.putExtra("unit", unit);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent redi = new Intent(getApplicationContext(), MainActivity.class);
        redi.putExtra("unit", unit);
        startActivity(redi);
    }
}
