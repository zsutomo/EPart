package com.astra.acan.epart;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataCartFragment extends Fragment {


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private String userEmail;
    private RecyclerView mRecyclerView;
    ArrayList<ModelDataCart> arrayList = new ArrayList<>();
    ArrayList<ModelDataEstimasi> estimasiArrayList = new ArrayList<>();
    private String id, nomorPart_cart, namaPart_cart, hargaPart_cart, jmlah_itemCart;
    private double totalHargaItem;
    private TextView tv_granTotalHarga;
    private FloatingActionButton fab_btn;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder dialogInput;
    private LayoutInflater inflater;
    private View view;
    private EditText et_nama_sa;
    private EditText et_nama_teknisi;
    private EditText et_nama_foreman;
    private EditText et_nomor_polisi;
    private EditText et_type_kendaraan;
    private TextView tv_simpan;
    private TextView tv_lihatdteail;
    private LinearLayout view_linear;
    private TextView tv_nama_sa;
    private TextView tv_nama_teknisi;
    private TextView tv_nama_foreman;
    private TextView tv_nomor_polisi;
    private TextView tv_type_kendaraan;
    private String nama_sa, nama_teknisi, nama_foreman, nomor_polisi, type_kendaraan;
    private Calendar calander;
    private SimpleDateFormat simpledateformat;
    private String date;
    private String total_hargaEstimasi;
    private String jmlahItemPart;

    public DataCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_cart, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_datacart);
        tv_granTotalHarga = view.findViewById(R.id.tv_totalEstimasi);
        tv_lihatdteail = view.findViewById(R.id.tv_lihatdetail);
        tv_simpan = view.findViewById(R.id.tv_simpanData);
        view_linear = view.findViewById(R.id.rl_input_data);
        tv_nama_sa = view.findViewById(R.id.nama_sa);
        tv_nama_teknisi = view.findViewById(R.id.nama_teknisi);
        tv_nama_foreman = view.findViewById(R.id.nama_foreman);
        tv_nomor_polisi = view.findViewById(R.id.nomor_polisi);
        tv_type_kendaraan = view.findViewById(R.id.type_kendaraan);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

        listCart();


        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLinearManager = new LinearLayoutManager(getActivity());
        myLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (arrayList.size() > 0 & mRecyclerView != null) {
            mRecyclerView.setAdapter(new DataCartAdapter(arrayList));
        }
        mRecyclerView.setLayoutManager(myLinearManager);

        tv_lihatdteail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_linear.setVisibility(View.VISIBLE);
            }
        });

        tv_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TestData", Toast.LENGTH_SHORT).show();
                getDateTime();
                addFormInput();
            }
        });


        return view;
    }

    private void simpanData() {

        estimasiArrayList = new ArrayList<>();
        String total_hargaEstimasi = tv_granTotalHarga.getText().toString();
        String nama_sa = tv_nama_sa.getText().toString();
        String nama_teknisi = tv_nama_teknisi.getText().toString();
        String nama_foreman = tv_nama_foreman.getText().toString();
        String nomor_polisi = tv_nomor_polisi.getText().toString();
        String type_kendaraan = tv_type_kendaraan.getText().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        String key = databaseReference.push().getKey();

        for (int i = 0; i < arrayList.size(); i++) {
            nomorPart_cart = arrayList.get(i).getNomorPart();
            namaPart_cart = arrayList.get(i).getNamaPart();
            hargaPart_cart = arrayList.get(i).getHargaPart();
            jmlahItemPart = String.valueOf(arrayList.get(i).getJmlahItem());
            totalHargaItem = Double.parseDouble(String.valueOf(arrayList.get(i).getTotalHargaItem()));

            ModelDataEstimasi dataEstimasi = new ModelDataEstimasi();
            dataEstimasi.setNomorPart(nomorPart_cart);
            dataEstimasi.setNamaPart(namaPart_cart);
            dataEstimasi.setHargaPart(hargaPart_cart);
            dataEstimasi.setJmlahItem(Integer.parseInt(jmlahItemPart));
            dataEstimasi.setTotalHargaItem(totalHargaItem);

//            if (arrayList.get(i).getJmlahItem() > 0) {

                estimasiArrayList.add(dataEstimasi);

                databaseReference = FirebaseDatabase.getInstance().getReference();
                String keyestimasi = databaseReference.child(userId).child("DataEstimasi").child(key).push().getKey();
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("total_HargaEstimasi").setValue(total_hargaEstimasi);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("namaSA").setValue(nama_sa);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("namaTeknisi").setValue(nama_teknisi);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("namaForeman").setValue(nama_foreman);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("nomorPolisi").setValue(nomor_polisi);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("typeKendaraan").setValue(type_kendaraan);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("userId").setValue(userEmail);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("tanggal").setValue(date);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("nomorPart").setValue(nomorPart_cart);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("namaPart").setValue(namaPart_cart);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("hargaPart").setValue(hargaPart_cart);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("jumlahItem").setValue(jmlahItemPart);
                databaseReference.child(userId).child("DataEstimasi").child(key).child(keyestimasi).child("jumlahHargaItem").setValue(totalHargaItem);
                System.out.println("data jumlah item : " + arrayList.size());

//        } else if (arrayList.get(i).getJmlahItem() <=0){
//                arrayList.remove(i);
//                System.out.println("data jumlah item : " + arrayList.size());
//
//            }

        }

        Intent intent = new Intent(getActivity(), Home.class);
        startActivity(intent);
    }

    private void addFormInput() {


        dialogInput = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.form_input_data_estimasi, null);
        dialogInput.setView(view);
        dialogInput.setCancelable(false);
        dialogInput.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_nama_sa = (EditText) view.findViewById(R.id.et_nama_sa);
                et_nama_teknisi = (EditText) view.findViewById(R.id.et_nama_teknisi);
                et_nama_foreman = (EditText) view.findViewById(R.id.et_nama_foreman);
                et_nomor_polisi = (EditText) view.findViewById(R.id.et_nomor_polisi);
                et_type_kendaraan = (EditText) view.findViewById(R.id.et_type_kendaraan);

                nama_sa = et_nama_sa.getText().toString();
                nama_teknisi = et_nama_teknisi.getText().toString();
                nama_foreman = et_nama_foreman.getText().toString();
                nomor_polisi = et_nomor_polisi.getText().toString();
                type_kendaraan = et_type_kendaraan.getText().toString();

                tv_nama_sa.setText("SA : " + nama_sa);
                tv_nama_teknisi.setText("Teknisi : " + nama_teknisi);
                tv_nama_foreman.setText("Foreman : " + nama_foreman);
                tv_nomor_polisi.setText("Nomor Polisi : " + nomor_polisi);
                tv_type_kendaraan.setText("Type Kendaraan : " + type_kendaraan);

                Toast.makeText(getActivity(), "Data : " + nama_sa, Toast.LENGTH_SHORT).show();
                simpanData();
            }
        });
        dialogInput.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }


    private void listCart() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Load Data");
        progressDialog.setMessage("Mohon Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshotCart : dataSnapshot.getChildren()) {

                    if (snapshotCart.getKey().equals("DataCart")) {

                        System.out.println("data snapshot1 : " + snapshotCart);

                        for (DataSnapshot snapshotCartItem : snapshotCart.getChildren()) {

                            if (snapshotCartItem.hasChildren()) {
                                System.out.println("data snapshot2 : " + snapshotCartItem);
                                String id = snapshotCartItem.getKey();
                                String nomorPart_cart = String.valueOf(snapshotCartItem.child("nomorPart").getValue());
                                String namaPart_cart = String.valueOf(snapshotCartItem.child("namaPart").getValue());
                                String hargaPart_cart = String.valueOf(snapshotCartItem.child("hargaPart").getValue());

                                ModelDataCart dataCart = new ModelDataCart();
                                dataCart.setNomorPart(nomorPart_cart);
                                dataCart.setNamaPart(namaPart_cart);
                                dataCart.setHargaPart(hargaPart_cart);
                                arrayList.add(dataCart);
                                System.out.println("data : " + arrayList.size());
                                progressDialog.dismiss();
                            } else if (arrayList.size() == 0) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Data Cart Kosong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        progressDialog.dismiss();
                    }
                }
                mRecyclerView.setAdapter(new DataCartAdapter(arrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private class DataCartAdapter extends RecyclerView.Adapter<DataCartViewHolder> {

        private int isiJmlahItem;
        private int itemCount = 1;

        ArrayList<ModelDataCart> listcart;

        public DataCartAdapter(ArrayList<ModelDataCart> arrayList) {
            listcart = arrayList;
        }

        @NonNull
        @Override
        public DataCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycle_cart, viewGroup, false);

            DataCartViewHolder holder = new DataCartViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final DataCartViewHolder cartViewHolder, final int position) {
            cartViewHolder.tv_nomorPart.setText(listcart.get(position).getNomorPart());
            cartViewHolder.tv_namaPart.setText(listcart.get(position).getNamaPart());
            cartViewHolder.tv_hargaPart.setText(listcart.get(position).getHargaPart());
            cartViewHolder.tv_jumlahItem.setText(String.valueOf(listcart.get(position).getJmlahItem()));
            cartViewHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCount = Integer.parseInt(cartViewHolder.tv_jumlahItem.getText().toString()) + 1;
                    listcart.get(position).setJmlahItem(Integer.parseInt(String.valueOf(itemCount)));
                    cartViewHolder.tv_jumlahItem.setText(String.valueOf(listcart.get(position).getJmlahItem()));
                    isiJmlahItem = Integer.parseInt(String.valueOf(cartViewHolder.tv_jumlahItem.getText().toString()));
                    System.out.println("jumlah item : " + isiJmlahItem);
                    totalHargaItem = Double.parseDouble(String.valueOf(cartViewHolder.tv_hargaPart.getText().toString())) * ((double) isiJmlahItem);
                    listcart.get(position).setTotalHargaItem(totalHargaItem);
                    cartViewHolder.tv_total_harga_item.setText("Rp." + String.valueOf(listcart.get(position).getTotalHargaItem()));
                    grandTotalHarga(listcart);
                }
            });

            cartViewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCount = Integer.parseInt(cartViewHolder.tv_jumlahItem.getText().toString()) - 1;
                    listcart.get(position).setJmlahItem(Integer.parseInt(String.valueOf(itemCount)));
                    cartViewHolder.tv_jumlahItem.setText(String.valueOf(listcart.get(position).getJmlahItem()));
                    isiJmlahItem = Integer.parseInt(String.valueOf(cartViewHolder.tv_jumlahItem.getText().toString()));
                    System.out.println("jumlah item : " + isiJmlahItem);
                    totalHargaItem = Double.parseDouble(String.valueOf(cartViewHolder.tv_hargaPart.getText().toString())) * ((double) isiJmlahItem);
                    listcart.get(position).setTotalHargaItem(totalHargaItem);
                    cartViewHolder.tv_total_harga_item.setText("Rp." + String.valueOf(listcart.get(position).getTotalHargaItem()));
                    grandTotalHarga(listcart);
                }
            });

        }

        @Override
        public int getItemCount() {
            return listcart.size();
        }

    }


    private class DataCartViewHolder extends RecyclerView.ViewHolder {
        private final Button plus;
        private final Button minus;
        TextView tv_nomorPart;
        TextView tv_namaPart;
        TextView tv_hargaPart;
        TextView tv_hapus;
        TextView tv_total_harga_item;
        TextView tv_jumlahItem;

        public DataCartViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nomorPart = itemView.findViewById(R.id.nomor_part_cart);
            tv_namaPart = itemView.findViewById(R.id.nama_part_cart);
            tv_hargaPart = itemView.findViewById(R.id.harga_part_cart);
//            tv_hapus = itemView.findViewById(R.id.hapus_datacart);
            tv_total_harga_item = itemView.findViewById(R.id.tv_jmlah_harga_part_cart);
            tv_jumlahItem = itemView.findViewById(R.id.tv_jmlah_item_part_cart);
            plus = itemView.findViewById(R.id.btn_nambah);
            minus = itemView.findViewById(R.id.btn_ngurangin);
        }
    }

    private int grandTotalHarga(ArrayList<ModelDataCart> arrayList) {
        int totalHargaEstimasi = 0;
        int i = 0;
        while (i < arrayList.size()) {
            totalHargaEstimasi = (int) (totalHargaEstimasi + arrayList.get(i).getTotalHargaItem());
            i++;
        }
        System.out.println("total : " + totalHargaEstimasi);
        double totalHarga = totalHargaEstimasi;
        tv_granTotalHarga.setText("Rp." + totalHarga);
        return totalHargaEstimasi;
    }

    private void getDateTime() {
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calander.getTime());
    }
}
