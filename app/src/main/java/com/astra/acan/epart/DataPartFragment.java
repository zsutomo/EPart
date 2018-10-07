package com.astra.acan.epart;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class DataPartFragment extends Fragment {

    EditText editTextCari;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private String userEmail;


    public DataPartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_data_part, container, false);

        initView(view);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

        editTextCari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String inputcari = editTextCari.getText().toString();
                    CariData(inputcari);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void CariData(final String inputcari) {
        System.out.println("input : " + inputcari);

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    if (dataSnapshot1.getKey().equals("FileUpload")){
                        System.out.println("data snapshot1 : " + dataSnapshot1);

                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){

                            if (inputcari.toLowerCase().equals(dataSnapshot2.child("NomorPart").getValue().toString().toLowerCase())) {

                                String id = dataSnapshot2.getKey();
                                String nomorPart = dataSnapshot2.child("NomorPart").getValue().toString();

                                System.out.println("hasil cari : " + nomorPart);
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

    private void initView(View view) {
        editTextCari = (EditText) view.findViewById(R.id.et_cari);
    }

}
