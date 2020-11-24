package com.example.ismef17.pts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SewingPerformanceActivity extends AppCompatActivity {

    ImageView imageViewBackbtn;
    TextView mTargetTextView;
    TextView mOutputTextView;
    TextView mBalanceTextView;
    TextView sewTargetTv;
    TextView sewBalanceTv;
    TextView sewOutputTv;
    Button mViewSewDetails;
    TextView midSewPageTitleTextView;
    ImageView mHomeBtnImageView;
    TextView mIdEfficiencyTextView;

    int count;
    int target_qty;
    int output_qty;
    int balance_qty;
    float total_eff;

    ArrayList<Integer> yData = new ArrayList<>();
    ArrayList<String> xData = new ArrayList<>();



//    Integer[] yData = {1000, 550};
//    String[] xData = {"Target", "Output"};
    PieChart pieChart;
    String unit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewing_performance);

        imageViewBackbtn = (ImageView) findViewById(R.id.backBtnImageView);
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        mTargetTextView = (TextView) findViewById(R.id.sewTarget);
        mOutputTextView = (TextView) findViewById(R.id.sewOutput);
        mBalanceTextView = (TextView) findViewById(R.id.sewBalance);
        sewTargetTv = (TextView) findViewById(R.id.sewTargetTv);
        sewOutputTv = (TextView) findViewById(R.id.sewOutputTv);
        sewBalanceTv = (TextView) findViewById(R.id.sewBalanceTv);
        mViewSewDetails = (Button) findViewById(R.id.viewSewDetails);
        midSewPageTitleTextView = (TextView) findViewById(R.id.idSewPageTitleTextView);
        mHomeBtnImageView = (ImageView) findViewById(R.id.homeBtnImageView);
        mIdEfficiencyTextView = (TextView) findViewById(R.id.idEfficiencyTextView);

        //Progress Loader Start
            final ProgressDialog progressDialog = new ProgressDialog(SewingPerformanceActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("LOADING...");
            progressDialog.show();
        //Progress Loader End

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            unit =(String) b.get("unit");
            midSewPageTitleTextView.setText(unit+" Sewing");
        }

        imageViewBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("unit", unit);
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

        mViewSewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UnitDetailActivity.class);
                i.putExtra("unit", unit);
                startActivity(i);
            }
        });

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(unit);
        final DatabaseReference myRef_1 = myRef.child(date).child("Line");

        // Read from the database
        myRef_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int target_qty = 0;
                int output_qty = 0;
                int balance_qty = 0;
                float total_eff = 0;
                int count = 0;
                float efficiency;

                yData.clear();
                xData.clear();

                for(DataSnapshot child : dataSnapshot.getChildren() ){

                    String target_res = dataSnapshot.child(child.getKey()).child("Target").getValue().toString();
                    String output_res = dataSnapshot.child(child.getKey()).child("Output").getValue().toString();
                    String eff_res = dataSnapshot.child(child.getKey()).child("Efficiency").getValue().toString();

                    target_qty += Integer.parseInt(target_res);
                    output_qty += Integer.parseInt(output_res);
                    total_eff += Float.parseFloat(eff_res);

                    count++;
                }

                efficiency = total_eff / count;

                balance_qty = target_qty - output_qty;

                if(target_qty+"" != "" && balance_qty+"" != "" && output_qty+"" != ""){
                    pieChart.setVisibility(View.VISIBLE);
                    mTargetTextView.setVisibility(View.VISIBLE);
                    mOutputTextView.setVisibility(View.VISIBLE);
                    mBalanceTextView.setVisibility(View.VISIBLE);
                    sewTargetTv.setVisibility(View.VISIBLE);
                    sewOutputTv.setVisibility(View.VISIBLE);
                    sewBalanceTv.setVisibility(View.VISIBLE);
                    mViewSewDetails.setVisibility(View.VISIBLE);
                    mIdEfficiencyTextView.setVisibility(View.VISIBLE);

                    progressDialog.dismiss();

                    yData.add(balance_qty);
                    yData.add(output_qty);

                    xData.add("Target");
                    xData.add("Output");

                    pieChart.setRotationEnabled(true);
                    pieChart.setHoleRadius(20);
                    pieChart.setTransparentCircleAlpha(0);
                    pieChart.setDrawEntryLabels(true);

                    Description description = new Description();
                    description.setText("Sewing Performance");
                    pieChart.setDescription(description);

                    addDataSet(pieChart);

                    mTargetTextView.setText(target_qty+"");
                    mOutputTextView.setText(output_qty+"");
                    mBalanceTextView.setText(balance_qty+"");
                    mIdEfficiencyTextView.setText("Efficiency= "+String.format("%.2f", efficiency));

                }

//                Toast.makeText(getApplicationContext(), "Target: "+target_qty+"~ Output: "+output_qty, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    private void addDataSet(PieChart chart) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i=0; i < yData.size(); i++){
            yEntrys.add(new PieEntry(yData.get(i), i));
        }

        for (int i=1; i < xData.size(); i++){
            xEntrys.add(xData.get(i));
        }

        // create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend =pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    @Override
    public void onBackPressed() {
        Intent redi = new Intent(getApplicationContext(), SewingPerformanceActivity.class);
        redi.putExtra("unit", unit);
        startActivity(redi);
    }
}
