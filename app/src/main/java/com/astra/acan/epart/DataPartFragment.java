package com.astra.acan.epart;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


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
    private FloatingActionButton fab_btn_add_datapart, fab_btn_add_datacart;
    private final int PICK_EXCEL_REQUEST=1000;
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private Uri pathUri;
    private String nomorPart;
    private String namaPart;
    private String hargaPart;
    private ProgressDialog progressDialog;
    private String inputcari;
    private String id;
    ArrayList<ModelDataPart> arrayList = new ArrayList<>();
    ArrayList<ModelDataPart> cartList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private View view;
    private CoordinatorLayout coordinatorLayout;
    private TextView tv_datakosong;
    private AlertDialog.Builder dialogInput;
    private LayoutInflater inflater;
    public EditText et_inputNomorPart;
    public EditText et_inputNamaPart;
    public EditText et_inputHargaPart;
    private FragmentTransaction fragmenTransaction;

    public DataPartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_data_part, container, false);

        fab_btn_add_datapart = view.findViewById(R.id.fab_add_data);
        fab_btn_add_datacart = view.findViewById(R.id.fab_add_data_cart);
        editTextCari = (EditText) view.findViewById(R.id.et_cari);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_datapart);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        tv_datakosong = (TextView) view.findViewById(R.id.tv_data_kosong);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

        searchingData();
        tambahDataManual();
        saveDataCart();


        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLinearManager = new LinearLayoutManager(getActivity());
        myLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(arrayList.size()>0 & mRecyclerView != null){
            mRecyclerView.setAdapter(new DataPartAdapter(arrayList));
        }
        mRecyclerView.setLayoutManager(myLinearManager);

        return view;
    }

    private void saveDataCart() {
        fab_btn_add_datacart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartList.size() > 0) {

                    progressDialog =  new ProgressDialog(getActivity());
                    progressDialog.setTitle("Menyimpan Data");
                    progressDialog.setMessage("Mohon Tunggu...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            simpanDataCartFirebase(cartList);
                        }
                    },20000);
                } else {
                    Toast.makeText(getActivity(), "Data Belum di tambahkan ke dalam keranjang!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void simpanDataCartFirebase(ArrayList<ModelDataPart> cartList) {

        for (int i=0;i<cartList.size(); i++){
            String nomorPart = cartList.get(i).getNomorPart();
            String namaPart = cartList.get(i).getNamaPart();
            String hargaPart = cartList.get(i).getHargaPart();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            String key = databaseReference.push().getKey();

            if (databaseReference.child(userId).child("DataCart").getKey().isEmpty()) {
                databaseReference.child(userId).child("DataCart").child(key).child("id_cart").setValue(key);
                databaseReference.child(userId).child("DataCart").child(key).child("nomorPart").setValue(nomorPart);
                databaseReference.child(userId).child("DataCart").child(key).child("namaPart").setValue(namaPart);
                databaseReference.child(userId).child("DataCart").child(key).child("hargaPart").setValue(hargaPart);
            } else {
                databaseReference.child(userId).child("DataCart").child(key).child("id_cart").setValue(key);
                databaseReference.child(userId).child("DataCart").child(key).child("nomorPart").setValue(nomorPart);
                databaseReference.child(userId).child("DataCart").child(key).child("namaPart").setValue(namaPart);
                databaseReference.child(userId).child("DataCart").child(key).child("hargaPart").setValue(hargaPart);
            }
        }

        progressDialog.dismiss();
        editTextCari.setText("");
        mRecyclerView.clearFocus();
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(getActivity());
        alBuilder.setTitle("Sukses");
        alBuilder.setMessage("Data Berhasil disimpan " + cartList.size());
        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    private void tambahDataManual() {
        fab_btn_add_datapart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputManual();
            }
        });
    }

    private void inputManual() {
        dialogInput = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.form_tambah_data, null);
        dialogInput.setView(view);
        dialogInput.setCancelable(false);
        dialogInput.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_inputNomorPart = (EditText) view.findViewById(R.id.et_nomor_part);
                et_inputNamaPart = (EditText) view.findViewById(R.id.et_nama_part);
                et_inputHargaPart = (EditText) view.findViewById(R.id.et_harga_part);

                String nomor_Part = et_inputNomorPart.getText().toString();
                String nama_Part = et_inputNamaPart.getText().toString();
                String harga_Part = et_inputHargaPart.getText().toString();

                editTextCari.setText("");
                editTextCari.clearFocus();
                tv_datakosong.setText("Data Terkirim");

                databaseReference = FirebaseDatabase.getInstance().getReference();
                String key = databaseReference.push().getKey();

                if (databaseReference.child(userId).child("DataInputManual").getKey().isEmpty()) {
                    databaseReference.child(userId).child("DataInputManual").child(key).child("id").setValue(key);
                    databaseReference.child(userId).child("DataInputManual").child(key).child("NomorPart").setValue(nomor_Part);
                    databaseReference.child(userId).child("DataInputManual").child(key).child("NamaPart").setValue(nama_Part);
                    databaseReference.child(userId).child("DataInputManual").child(key).child("HargaPart").setValue(harga_Part);
                } else {
                    databaseReference.child(userId).child("DataInputManual").child(key).child("id").setValue(key);
                    databaseReference.child(userId).child("DataInputManual").child(key).child("NomorPart").setValue(nomor_Part);
                    databaseReference.child(userId).child("DataInputManual").child(key).child("NamaPart").setValue(nama_Part);
                    databaseReference.child(userId).child("DataInputManual").child(key).child("HargaPart").setValue(harga_Part);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).show();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void searchingData() {
        editTextCari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (editTextCari != null && !TextUtils.isEmpty(editTextCari.getText())) {
                        inputcari = editTextCari.getText().toString();
                        CariData(inputcari);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void CariData(final String inputcari) {
        arrayList = new ArrayList<>();


        System.out.println("input : " + inputcari);
        progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setTitle("Mencari..");
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.show();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    System.out.println("isi datapart : " + dataSnapshot1);

                    if (dataSnapshot1.getKey().equals("FileUpload")){
                        System.out.println("data snapshot1 : " + dataSnapshot1);

                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
//                            System.out.println("data snapshot2 : " + dataSnapshot2);

                            if (inputcari.toLowerCase().equals(dataSnapshot2.child("NomorPart").getValue())) {
                                id = dataSnapshot2.getKey();
                                nomorPart = dataSnapshot2.child("NomorPart").getValue().toString();
                                namaPart = dataSnapshot2.child("NamaPart").getValue().toString();
                                hargaPart = dataSnapshot2.child("HargaPart").getValue().toString();


                                ModelDataPart modelDataPart = new ModelDataPart();
                                modelDataPart.setNomorPart(nomorPart);
                                modelDataPart.setNamaPart(namaPart);
                                modelDataPart.setHargaPart(hargaPart);
                                arrayList.add(modelDataPart);

                                System.out.println("hasil cari : " + nomorPart);
                                System.out.println("size : " + arrayList.size());
                                progressDialog.dismiss();
                                tv_datakosong.setVisibility(View.GONE);
                                fab_btn_add_datapart.setVisibility(View.GONE);

                            } else if (arrayList.size()==0) {
                                fab_btn_add_datapart.setVisibility(View.VISIBLE);
                                tv_datakosong.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }
                    } else if (dataSnapshot1.getKey().equals("DataInputManual")) {
                        for (DataSnapshot snapshotInputManual : dataSnapshot1.getChildren()){
//                            System.out.println("data snapshot2 : " + dataSnapshot2);

                            if (inputcari.toLowerCase().equals(snapshotInputManual.child("NomorPart").getValue())) {
                                id = snapshotInputManual.getKey();
                                nomorPart = snapshotInputManual.child("NomorPart").getValue().toString();
                                namaPart = snapshotInputManual.child("NamaPart").getValue().toString();
                                hargaPart = snapshotInputManual.child("HargaPart").getValue().toString();


                                ModelDataPart modelDataPart = new ModelDataPart();
                                modelDataPart.setNomorPart(nomorPart);
                                modelDataPart.setNamaPart(namaPart);
                                modelDataPart.setHargaPart(hargaPart);
                                arrayList.add(modelDataPart);

                                System.out.println("hasil cari : " + nomorPart);
                                System.out.println("size : " + arrayList.size());
                                progressDialog.dismiss();
                                tv_datakosong.setVisibility(View.GONE);
                                fab_btn_add_datapart.setVisibility(View.GONE);

                            } else if (arrayList.size()==0) {
                                fab_btn_add_datapart.setVisibility(View.VISIBLE);
                                tv_datakosong.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }

                    } else {
                        fab_btn_add_datapart.setVisibility(View.VISIBLE);
                        tv_datakosong.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }
                mRecyclerView.setAdapter(new DataPartAdapter(arrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private class DataPartAdapter extends RecyclerView.Adapter<DataPartViewHolder> {
        ArrayList<ModelDataPart> list;

        public DataPartAdapter(ArrayList<ModelDataPart> arrayList) {
            list = arrayList;
        }

        @NonNull
        @Override
        public DataPartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycle_part, viewGroup, false);

            DataPartViewHolder holder = new DataPartViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final DataPartViewHolder dataPartViewHolder, final int position) {
            dataPartViewHolder.tv_nomorPart.setText(list.get(position).getNomorPart());
            dataPartViewHolder.tv_namaPart.setText(list.get(position).getNamaPart());
            dataPartViewHolder.tv_hargaPart.setText(list.get(position).getHargaPart());
            dataPartViewHolder.tv_masukKeranjang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nomorPart = list.get(position).getNomorPart();
                    String namaPart = list.get(position).getNamaPart();
                    String hargaPart = list.get(position).getHargaPart();

                    ModelDataPart part = new ModelDataPart();
                    part.setNomorPart(nomorPart);
                    part.setNamaPart(namaPart);
                    part.setHargaPart(hargaPart);
                    System.out.println("test click : " + cartList.size());
                    cartList.add(part);

                    if (cartList.size() > 0 ) {
                        AlertDialog.Builder alBuilder = new AlertDialog.Builder(getActivity());
                        alBuilder.setTitle("Sukses");
                        alBuilder.setMessage("Data Berhasil di simpan di keranjang : " + part.getNomorPart());
                        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class DataPartViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nomorPart;
        TextView tv_namaPart;
        TextView tv_hargaPart;
        TextView tv_masukKeranjang;
        TextView tv_jumlahItem;
        Button plus, minus;

        public DataPartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nomorPart = itemView.findViewById(R.id.nomor_part);
            tv_namaPart = itemView.findViewById(R.id.nama_part);
            tv_hargaPart = itemView.findViewById(R.id.harga_part);
            tv_masukKeranjang = itemView.findViewById(R.id.simpan_dataPart);

        }
    }
}
