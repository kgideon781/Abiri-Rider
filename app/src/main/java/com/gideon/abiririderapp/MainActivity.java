package com.gideon.abiririderapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.gideon.abiririderapp.Common.Common;
import com.gideon.abiririderapp.Model.Rider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout rootLayout;

    private final static  int PERMISSION =1000;

    @Override
    protected void attachBaseContext(Context newbase){


        super.attachBaseContext(CalligraphyContextWrapper.wrap(newbase));



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);
        //init firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.user_rider_tbl);

        //init view
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);



        //Event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDiaog();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");
        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout =inflater.inflate(R.layout.layout_signin, null);
        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);
        dialog.setView(login_layout);

        //set button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //set disable button sign in if processing
                btnSignIn.setEnabled(false);


                // check validation
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {

                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {

                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                if (edtPassword.getText().toString().length() < 6) {

                    Snackbar.make(rootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();


                //Login
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, Home.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout,"Failed"+e.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();
                        //active button
                        btnSignIn.setEnabled(true);
                    }
                });


            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        dialog.show();
    }


    private void showRegisterDiaog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout =inflater.inflate(R.layout.layout_register, null);
        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);
        dialog.setView(register_layout);

        //set button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                // check validation
                if(TextUtils.isEmpty(edtEmail.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                if(TextUtils.isEmpty(edtPhone.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter phone number", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                if(TextUtils.isEmpty(edtPassword.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                if(edtPassword.getText().toString().length() < 6){

                    Snackbar.make(rootLayout, "Password too short !!!", Snackbar.LENGTH_SHORT)
                            .show();
                    return;


                }
                //register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //save user to db
                                Rider rider = new Rider();
                                rider.setEmail(edtEmail.getText().toString());
                                rider.setName(edtName.getText().toString());
                                rider.setPhone(edtPhone.getText().toString());
                                rider.setPassword(edtPassword.getText().toString());

                                //use email to key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(rider)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout, "Register succesfully !!!", Snackbar.LENGTH_SHORT)
                                                        .show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout, "Failed !!!"+e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "Failed !!!"+e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        dialog.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(FirebaseAuth.getInstance().getCurrentUser().getUid() != null)
            startActivity(new Intent(MainActivity.this, Home.class));*/


    }
}
