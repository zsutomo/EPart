package com.astra.acan.epart;


import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataEstimasiFragment extends Fragment{


    private RecyclerView mRecyclerView;
    private ArrayList<ModelItem> arrayList = new ArrayList<>();
    private ArrayList <ModelItem> listItem = new ArrayList<>();
    private ArrayList <ModelItem> listItemPart = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private String userEmail;
    private ProgressDialog progressDialog;
    private String nomorPart_cart;
    private String namaPart_cart;
    private String hargaPart_cart;
    private String namaSa;
    private String total_Estimasi;
    private String tanggal;
    private String nomorPolisi;
    private String typeKendaraan;
    private String namaTeknisi;
    private String namaForeman;
    private AlertDialog.Builder dialogInput;
    private LayoutInflater inflater;
    private View view;
    private ListView listView;
    private TextView tv_namaSa;
    private TextView tv_teknisi;
    private TextView tv_foreman;
    private TextView tv_nomorpolisi;
    private TextView tv_typekendaraan;
    private TextView tv_tanggal;
    private TextView tv_userId;
    private DataEstimasiAdapter DataEstimasiAdapter;
    private String jumlahItem;
    private String jumlahHargaItem;
    private String jumlahItem_part;
    private ListAdapter lisAdapter;
    private TextView tv_total;
    protected static final String TAG = "TAG";
    private String namaPartmant;
    private String st_stok;
    private TextView tv_Partman;
    private EditText editTextCari;
    private String inputcari;


    public DataEstimasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_estimasi, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_dataestimasi);
        editTextCari = (EditText) view.findViewById(R.id.et_cari_estimasi);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

//        listDataEstimasi();
        searchingData();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLinearManager = new LinearLayoutManager(getActivity());
        myLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (arrayList.size() > 0 & mRecyclerView != null) {
            mRecyclerView.setAdapter(new DataEstimasiAdapter(arrayList));
        }
        mRecyclerView.setLayoutManager(myLinearManager);

        return view;
    }

    private void searchingData() {
        editTextCari.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (editTextCari != null) {
                        inputcari = editTextCari.getText().toString();
                        CariDataEstimasi(inputcari);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void CariDataEstimasi(final String inputcari) {

        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mencari Data..");
        progressDialog.setMessage("Mohon Tunggu...!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            userId = firebaseUser.getUid();
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshotCart : dataSnapshot.getChildren()) {
                        if (snapshotCart.getKey().equals("DataEstimasi")) {
                            System.out.println("data snapshot1 : " + snapshotCart);
                            for (DataSnapshot snapshotEstimasiItem : snapshotCart.getChildren()) {
                                System.out.println("data snapshot2 : " + snapshotEstimasiItem);
                                String keyEstimasi = snapshotEstimasiItem.getKey();
                                System.out.println("data key : " + keyEstimasi);

                                if (inputcari.equals(snapshotEstimasiItem.getKey())){

                                    for (DataSnapshot snapshotEstimasiItem1 : snapshotEstimasiItem.getChildren()){

                                        System.out.println("data snapshot item1 : " + snapshotEstimasiItem1);

                                        String id = snapshotEstimasiItem1.getKey();
                                        nomorPart_cart = String.valueOf(snapshotEstimasiItem1.child("nomorPart").getValue());
                                        namaPart_cart = String.valueOf(snapshotEstimasiItem1.child("namaPart").getValue());
                                        hargaPart_cart = String.valueOf(snapshotEstimasiItem1.child("hargaPart").getValue());
                                        jumlahItem = String.valueOf(snapshotEstimasiItem1.child("jumlahItem").getValue());
                                        jumlahHargaItem = String.valueOf(snapshotEstimasiItem1.child("jumlahHargaItem").getValue());
                                        nomorPolisi = String.valueOf(snapshotEstimasiItem1.child("nomorPolisi").getValue());
                                        typeKendaraan = String.valueOf(snapshotEstimasiItem1.child("typeKendaraan").getValue());
                                        namaSa = String.valueOf(snapshotEstimasiItem1.child("namaSA").getValue());
                                        namaTeknisi = String.valueOf(snapshotEstimasiItem1.child("namaTeknisi").getValue());
                                        namaForeman = String.valueOf(snapshotEstimasiItem1.child("namaForeman").getValue());
                                        userEmail = String.valueOf(snapshotEstimasiItem1.child("userId").getValue());
                                        total_Estimasi = String.valueOf(snapshotEstimasiItem1.child("total_HargaEstimasi").getValue());
                                        tanggal = String.valueOf(snapshotEstimasiItem1.child("tanggal").getValue());
                                        namaPartmant = String.valueOf(snapshotEstimasiItem1.child("namaPartman").getValue());

                                        ModelItem dataEstimasi = new ModelItem();
                                        dataEstimasi.setNamaSa(namaSa);
                                        dataEstimasi.setTanggal(tanggal);
                                        dataEstimasi.setUserEmail(userEmail);
                                        dataEstimasi.setNamaTeknisi(namaTeknisi);
                                        dataEstimasi.setNamaForeMan(namaForeman);
                                        dataEstimasi.setNamaPartman(namaPartmant);
                                        dataEstimasi.setNomorPolisi(nomorPolisi);
                                        dataEstimasi.setNomorPart(nomorPart_cart);
                                        dataEstimasi.setNamaPart(namaPart_cart);
                                        dataEstimasi.setTotalHargaEstimasi(total_Estimasi);
                                        dataEstimasi.setId(keyEstimasi);
                                        arrayList.add(dataEstimasi);
                                        System.out.println("data : " + jumlahItem);
                                        progressDialog.dismiss();
                                    }

                                } else if (arrayList.size()==0){
                                    progressDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Data Kosong");
                                    builder.setCancelable(true);
                                    builder.setMessage("Mohon Maaf Data Yang Anda Cari Tidak ditemukan!");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();


                                }
                            }
                        } else if (arrayList.size()==0){
                            progressDialog.dismiss();
                        }
                    }
                    mRecyclerView.setAdapter(new DataEstimasiAdapter(arrayList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void listDataEstimasi() {
//        arrayList = new ArrayList<>();
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setTitle("Load Data");
//        progressDialog.setMessage("Mohon Tunggu...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        try {
//
//            databaseReference = FirebaseDatabase.getInstance().getReference();
//            firebaseAuth = FirebaseAuth.getInstance();
//            firebaseUser = firebaseAuth.getCurrentUser();
//            userId = firebaseUser.getUid();
//            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for (DataSnapshot snapshotCart : dataSnapshot.getChildren()) {
//
//                        if (snapshotCart.getKey().equals("DataEstimasi")) {
//
//                            System.out.println("data snapshot1 : " + snapshotCart);
//
//                            for (DataSnapshot snapshotEstimasiItem : snapshotCart.getChildren()) {
//                                System.out.println("data snapshot2 : " + snapshotEstimasiItem);
//                                String keyEstimasi = snapshotEstimasiItem.getKey();
//                                System.out.println("data key : " + keyEstimasi);
//
//                                for (DataSnapshot snapshotEstimasiItem1 : snapshotEstimasiItem.getChildren()){
//                                    System.out.println("data snapshot item1 : " + snapshotEstimasiItem1);
//
//                                    String id = snapshotEstimasiItem1.getKey();
//                                    nomorPart_cart = String.valueOf(snapshotEstimasiItem1.child("nomorPart").getValue());
//                                    namaPart_cart = String.valueOf(snapshotEstimasiItem1.child("namaPart").getValue());
//                                    hargaPart_cart = String.valueOf(snapshotEstimasiItem1.child("hargaPart").getValue());
//                                    jumlahItem = String.valueOf(snapshotEstimasiItem1.child("jumlahItem").getValue());
//                                    jumlahHargaItem = String.valueOf(snapshotEstimasiItem1.child("jumlahHargaItem").getValue());
//                                    nomorPolisi = String.valueOf(snapshotEstimasiItem1.child("nomorPolisi").getValue());
//                                    typeKendaraan = String.valueOf(snapshotEstimasiItem1.child("typeKendaraan").getValue());
//                                    namaSa = String.valueOf(snapshotEstimasiItem1.child("namaSA").getValue());
//                                    namaTeknisi = String.valueOf(snapshotEstimasiItem1.child("namaTeknisi").getValue());
//                                    namaForeman = String.valueOf(snapshotEstimasiItem1.child("namaForeman").getValue());
//                                    userEmail = String.valueOf(snapshotEstimasiItem1.child("userId").getValue());
//                                    total_Estimasi = String.valueOf(snapshotEstimasiItem1.child("total_HargaEstimasi").getValue());
//                                    tanggal = String.valueOf(snapshotEstimasiItem1.child("tanggal").getValue());
//                                    namaPartmant = String.valueOf(snapshotEstimasiItem1.child("namaPartman").getValue());
//
//
//                                }
//
//                                ModelItem dataEstimasi = new ModelItem();
//                                dataEstimasi.setNamaSa(namaSa);
//                                dataEstimasi.setTanggal(tanggal);
//                                dataEstimasi.setUserEmail(userEmail);
//                                dataEstimasi.setNamaTeknisi(namaTeknisi);
//                                dataEstimasi.setNamaForeMan(namaForeman);
//                                dataEstimasi.setNamaPartman(namaPartmant);
//                                dataEstimasi.setTotalHargaEstimasi(total_Estimasi);
//                                dataEstimasi.setId(keyEstimasi);
//                                arrayList.add(dataEstimasi);
//                                System.out.println("data : " + jumlahItem);
//                                progressDialog.dismiss();
//                            }
//                        } else if (arrayList.size()==0){
//                            progressDialog.dismiss();
//                        }
//                    }
//                    mRecyclerView.setAdapter(new DataEstimasiAdapter(arrayList));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private class DataEstimasiAdapter extends RecyclerView.Adapter<EstimasiViewHolder> {

        ArrayList<ModelItem> listEstimasi;
        String keyEstimasi;

        public DataEstimasiAdapter(ArrayList<ModelItem> arrayList) {
            listEstimasi = arrayList;
        }

        @NonNull
        @Override
        public EstimasiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycle_estimasi, viewGroup, false);
            EstimasiViewHolder holder = new EstimasiViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull EstimasiViewHolder estimasiViewHolder, final int position) {
            estimasiViewHolder.tv_UserEmail.setText("User Id : " + listEstimasi.get(position).getUserEmail());
            estimasiViewHolder.tv_tanggalEstimasi.setText("Waktu : " + listEstimasi.get(position).getTanggal());
            estimasiViewHolder.tv_namaSA.setText("SA : " +listEstimasi.get(position).getNamaSa());
            estimasiViewHolder.tv_total_hargaEstimasi.setText(listEstimasi.get(position).getTotalHargaEstimasi());
            estimasiViewHolder.tv_lihatDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listItem = new ArrayList<>();

                    keyEstimasi = listEstimasi.get(position).getId();
                    String namaSA = listEstimasi.get(position).getNamaSa();
                    String totalEstimasi = listEstimasi.get(position).getTotalHargaEstimasi();
                    System.out.println("test key estimasi : " + keyEstimasi);

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child(userId).child("DataEstimasi").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshotEstimasi : dataSnapshot.getChildren()) {
                                if (dataSnapshotEstimasi.getKey().equals(keyEstimasi)) {
                                    for (DataSnapshot dataSnapshotListItem : dataSnapshotEstimasi.getChildren()) {
//                                            System.out.println("data snapshot isi estimasi : " + dataSnapshotListItem);

                                        String id = dataSnapshotListItem.getKey();
                                        nomorPart_cart = String.valueOf(dataSnapshotListItem.child("nomorPart").getValue());
                                        namaPart_cart = String.valueOf(dataSnapshotListItem.child("namaPart").getValue());
                                        hargaPart_cart = String.valueOf(dataSnapshotListItem.child("hargaPart").getValue());
                                        jumlahItem = String.valueOf(dataSnapshotListItem.child("jumlahItem").getValue());
                                        jumlahHargaItem = String.valueOf(dataSnapshotListItem.child("jumlahHargaItem").getValue());
                                        nomorPolisi = String.valueOf(dataSnapshotListItem.child("nomorPolisi").getValue());
                                        typeKendaraan = String.valueOf(dataSnapshotListItem.child("typeKendaraan").getValue());
                                        namaSa = String.valueOf(dataSnapshotListItem.child("namaSA").getValue());
                                        namaTeknisi = String.valueOf(dataSnapshotListItem.child("namaTeknisi").getValue());
                                        namaForeman = String.valueOf(dataSnapshotListItem.child("namaForeman").getValue());
                                        userEmail = String.valueOf(dataSnapshotListItem.child("userId").getValue());
                                        total_Estimasi = String.valueOf(dataSnapshotListItem.child("total_HargaEstimasi").getValue());
                                        tanggal = String.valueOf(dataSnapshotListItem.child("tanggal").getValue());
                                        namaPartmant = String.valueOf(dataSnapshotListItem.child("namaPartman").getValue());
                                        st_stok = String.valueOf(dataSnapshotListItem.child("Status_Stok").getValue());

                                        ModelItem dataitem = new ModelItem();
                                        dataitem.setNomorPart(nomorPart_cart);
                                        dataitem.setNamaPart(namaPart_cart);
                                        dataitem.setHargaPart(hargaPart_cart);
                                        dataitem.setStatusStok(st_stok);
                                        dataitem.setJmlahItem(jumlahItem);
                                        dataitem.setTotalHargaItem(jumlahHargaItem);
                                        listItem.add(dataitem);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "ERor" + databaseError, Toast.LENGTH_SHORT).show();
                        }
                    });

                    if (listItem != null) {
                        lihatDetail(totalEstimasi,namaSA, listItem);
                    } else {
                        Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            estimasiViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Test card" + keyEstimasi, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(getContext());
                    aBuilder.setTitle("Info Data");
                    aBuilder.setCancelable(true);
                    aBuilder.setMessage("Data Estimasi : " + keyEstimasi);
                    aBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), keyEstimasi+" Belum Bisa di Edit", Toast.LENGTH_SHORT).show();
                        }
                    });
                    aBuilder.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), keyEstimasi+" Belum Bisa di Hapus", Toast.LENGTH_SHORT).show();
                        }
                    });
                    aBuilder.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return listEstimasi.size();
        }
    }

    private void lihatDetail(final String totalEstimasi, final String testsa, final ArrayList<ModelItem> listItem) {

        if (listItem != null) {

            for (int i =0; i<listItem.size();i++){

                String nomorPart_cart = listItem.get(i).getNomorPart();
                String namaPart_cart = listItem.get(i).getNamaPart();
                String hargaPart_cart = listItem.get(i).getHargaPart();
                jumlahItem_part = listItem.get(i).getJmlahItem();
                String jumlahHargaItem = String.valueOf(listItem.get(i).getTotalHargaItem());
                String st_stok = listItem.get(i).getStatusStok();

                System.out.println("test list Item Nomor : "+ nomorPart_cart);
                System.out.println("test list Item nama : "+ namaPart_cart);
                System.out.println("test list Item harga : "+ hargaPart_cart);
                System.out.println("test list Item Jumlah : "+ jumlahItem_part);
                System.out.println("test list Item Harga Item : "+ jumlahHargaItem);

                ModelItem item = new ModelItem();
                item.setNomorPart(nomorPart_cart);
                item.setNamaPart(namaPart_cart);
                item.setHargaPart(hargaPart_cart);
                item.setJmlahItem(jumlahItem_part);
                item.setTotalHargaItem(jumlahHargaItem);
                item.setStatusStok(st_stok);
                listItemPart.add(item);

                System.out.println("test data item : " + item.getJmlahItem());
            }

            dialogInput = new AlertDialog.Builder(getActivity());
            inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.detail_estimasi, null);
            dialogInput.setView(view);

            lisAdapter = new ListAdapter(getActivity(), listItem);
            listView = (ListView) view.findViewById(R.id.listViewItem);
            listView.setAdapter(lisAdapter);

            tv_namaSa = (TextView) view.findViewById(R.id.tv_nama_sa);
            tv_teknisi = (TextView) view.findViewById(R.id.tv_nama_teknisi);
            tv_foreman = (TextView) view.findViewById(R.id.tv_nama_foreman);
            tv_nomorpolisi = (TextView) view.findViewById(R.id.tv_nomor_polisi);
            tv_typekendaraan = (TextView) view.findViewById(R.id.tv_type_kendaraan);
            tv_tanggal = (TextView) view.findViewById(R.id.tv_tanggal);
            tv_userId = (TextView) view.findViewById(R.id.tv_userId);
            tv_total = (TextView) view.findViewById(R.id.tv_totalEstimasi);
            tv_Partman = (TextView) view.findViewById(R.id.tv_nama_partman);

            tv_namaSa.setText("SA : " +testsa);
            tv_teknisi.setText("Teknisi : " + namaTeknisi);
            tv_foreman.setText("Foreman : " +namaForeman);
            tv_Partman.setText("Partman : " + namaPartmant);
            tv_nomorpolisi.setText("NomorPolisi : " +nomorPolisi);
            tv_typekendaraan.setText("Type Kendaraan : " +typeKendaraan);
            tv_tanggal.setText("Waktu : " + tanggal);
            tv_userId.setText("admin : " + userEmail);
            tv_total.setText("Total : " + total_Estimasi);
            dialogInput.setCancelable(true);
            dialogInput.setPositiveButton("Cetak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    System.out.println("test data : " + listItem.size());

                    Intent intent = new Intent(getActivity(), CetakData.class);
                    intent.putExtra("arraylist", listItem);
                    intent.putExtra("tanggal", tanggal);
                    intent.putExtra("namaSA", testsa);
                    intent.putExtra("nama_teknisi", namaTeknisi);
                    intent.putExtra("nama_foreman", namaForeman);
                    intent.putExtra("nama_partman", namaPartmant);
                    intent.putExtra("nomor_polisi", nomorPolisi);
                    intent.putExtra("type_kendaraan", typeKendaraan);
                    intent.putExtra("admin", userEmail);
                    intent.putExtra("total_estimasi", totalEstimasi);
                    startActivity(intent);
                }
            });
            dialogInput.show();
        } else {
            Toast.makeText(getActivity(), "Data Kosong", Toast.LENGTH_SHORT).show();
        }

    }


    private class EstimasiViewHolder extends RecyclerView.ViewHolder {

        TextView tv_UserEmail;
        TextView tv_tanggalEstimasi;
        TextView tv_namaSA;
        TextView tv_total_hargaEstimasi;
        TextView tv_lihatDetail;
        CardView cardView;

        public EstimasiViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_UserEmail = itemView.findViewById(R.id.user_email);
            tv_namaSA = itemView.findViewById(R.id.nama_sa);
            tv_tanggalEstimasi = itemView.findViewById(R.id.tanggal_estimasi);
            tv_total_hargaEstimasi = itemView.findViewById(R.id.tv_totalEstimasi);
            tv_lihatDetail = itemView.findViewById(R.id.tv_lihatdetail);
            cardView = itemView.findViewById(R.id.card_view);


        }
    }
}
