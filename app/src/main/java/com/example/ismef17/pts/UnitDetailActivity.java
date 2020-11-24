package com.example.ismef17.pts;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Description;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnitDetailActivity extends AppCompatActivity {

    ImageView mHomeBtnImageView;
    ImageView imageViewBackbtn;
    TextView mIdUnitPageTitle;
    ScrollView mIdTableScrollView;
    TableLayout mtable;

    String unit;

    ArrayList<String>lines = new ArrayList<>();
    ArrayList<String>targets = new ArrayList<>();
    ArrayList<String>outputs = new ArrayList<>();
    ArrayList<String>efficiencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_detail);

        imageViewBackbtn = (ImageView) findViewById(R.id.backBtnImageView);
        mHomeBtnImageView = (ImageView) findViewById(R.id.homeBtnImageView);
        mIdUnitPageTitle = (TextView) findViewById(R.id.idUnitPageTitle);
        mIdTableScrollView = (ScrollView) findViewById(R.id.idTableScrollView);
        mtable = (TableLayout) findViewById(R.id.table);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b!=null)
        {
            unit =(String) b.get("unit");
            mIdUnitPageTitle.setText(unit+" Lines");
        }

        imageViewBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SewingPerformanceActivity.class);
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

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(unit);
        final DatabaseReference myRef_1 = myRef.child(date).child("Line");


        // Read from the database
        myRef_1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lines.clear();
                targets.clear();
                outputs.clear();
                efficiencies.clear();

                for(DataSnapshot child : dataSnapshot.getChildren() ){

                    String line_res = child.getKey();
                    String target_res = dataSnapshot.child(child.getKey()).child("Target").getValue().toString();
                    String output_res = dataSnapshot.child(child.getKey()).child("Output").getValue().toString();
                    String eff_res = dataSnapshot.child(child.getKey()).child("Efficiency").getValue().toString();

                    lines.add(line_res);
                    targets.add(target_res);
                    outputs.add(output_res);
                    efficiencies.add(eff_res);

                }
                addHeaders();
                addData();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }


    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(25, 25, 25, 25);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setTextSize(18);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
    }

    private void addData() {
//        int numCompanies = companies.length;
        int numCompanies = lines.size();

//        Toast.makeText(getApplicationContext(), numCompanies+"", Toast.LENGTH_SHORT).show();

        TableLayout tl = findViewById(R.id.table);
        cleanTable(tl);

        for (int i = 0; i < numCompanies; i++) {

            TableRow tr = new TableRow(this);

            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + 1, lines.get(i), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorLightYellow)));
            tr.addView(getTextView(i + numCompanies, targets.get(i), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorLightGreen)));
            tr.addView(getTextView(i + numCompanies, outputs.get(i), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorLightGreen)));
            tr.addView(getTextView(i + numCompanies, efficiencies.get(i), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorLightGreen)));
            tl.addView(tr, getTblLayoutParams());
        }
    }

    private void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "LINE", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorSlidDarkPaste)));
        tr.addView(getTextView(0, "TARGET", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorSlidDarkPaste)));
        tr.addView(getTextView(0, "OUTPUT", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorSlidDarkPaste)));
        tr.addView(getTextView(0, "EFFICIENCY", Color.WHITE, Typeface.BOLD, ContextCompat.getColor(this, R.color.colorSlidDarkPaste)));
        tl.addView(tr, getTblLayoutParams());
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    @Override
    public void onBackPressed() {
        Intent redi = new Intent(getApplicationContext(), UnitDetailActivity.class);
        redi.putExtra("unit", unit);
        startActivity(redi);
    }
}
