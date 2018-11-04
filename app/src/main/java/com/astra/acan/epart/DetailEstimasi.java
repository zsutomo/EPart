package com.astra.acan.epart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailEstimasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_estimasi);

        if (getIntent().getExtras() !=null) {
            Bundle bundle = getIntent().getExtras();
            String nama = bundle.getString("nama");

            System.out.println(nama);
        }
    }
}
