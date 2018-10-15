package com.astra.acan.epart;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CariDataPart extends AppCompatActivity {

    private EditText editTextCari;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String userEmail;
    private String inputcari;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_data_part);

        editTextCari = (EditText) findViewById(R.id.et_caridatapart);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

        editTextCari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    inputcari = editTextCari.getText().toString();
                    CariData(inputcari);
                    return true;
                }
                return false;
            }
        });
    }

    private void CariData(final String inputcari) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        System.out.println("input : " + inputcari);

        progressDialog =  new ProgressDialog(CariDataPart.this);
        progressDialog.setTitle("Mencari..");
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    if (dataSnapshot1.getKey().equals("FileUpload")){
                        System.out.println("data snapshot1 : " + dataSnapshot1);

                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                            System.out.println("data snapshot2 : " + dataSnapshot2);

                            if (inputcari.toLowerCase().equals(dataSnapshot2.child("Nomor Part").getValue().toString().toLowerCase())) {
                                String id = dataSnapshot2.getKey();
                                String nomorPart = dataSnapshot2.child("Nomor Part").getValue().toString();
                                String namaPart = dataSnapshot2.child("Nama Part").getValue().toString();
                                String hargaPart = dataSnapshot2.child("Harga Part").getValue().toString();
                                System.out.println("hasil cari : " + dataSnapshot2.child("Nomor Part"));
                                progressDialog.dismiss();
                            }

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
