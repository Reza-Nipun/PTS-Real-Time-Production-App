package com.example.ismef17.pts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button mBtnLogin;
    EditText mInputPassword;

    String pin;
    String res_pin;
    String shared_pref_value;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Progress Loader Start
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("LOADING...");
        //Progress Loader End

        mInputPassword = (EditText) findViewById(R.id.idInputPassword);
        mBtnLogin = (Button) findViewById(R.id.idBtnLogin);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        shared_pref_value = sharedpreferences.getString("pin", "");

        if (shared_pref_value != ""){

            Intent intent = new Intent(getApplicationContext(), UnitSelectionActivity.class);
            startActivity(intent);

        }else{

            mBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pin = mInputPassword.getText().toString();

                    if (pin.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Input Valid PIN !", Toast.LENGTH_LONG).show();
                    }else {

                        progressDialog.show();

                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        DatabaseReference myRef = firebaseDatabase.getReference("users");

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(pin)){
                                    Intent intent = new Intent(getApplicationContext(), UnitSelectionActivity.class);
                                    startActivity(intent);

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("pin", pin);
                                    editor.apply();
                                    editor.commit();

                                    progressDialog.dismiss();
                                }else {
                                    mInputPassword.setText("");
                                    Toast.makeText(getApplicationContext(), "Invalid PIN !", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                }
            });
        }

    }
}
