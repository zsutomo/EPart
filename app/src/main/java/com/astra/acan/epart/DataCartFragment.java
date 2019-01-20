package com.astra.acan.epart;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.List;


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
    private Calendar calander;
    private SimpleDateFormat simpledateformat;
    private String date;
    private String total_hargaEstimasi;
    private String jmlahItemPart;
    private String isiSpiner;
    private TextView tv_nama_partman;
    private String iddataCart;
    private FragmentTransaction fragmenTransaction;
    ArrayAdapter<String> SpinnerAdapter;
    Spinner sp_nama_sa;
    Spinner sp_nama_teknisi;
    Spinner sp_nama_foreman;
    Spinner sp_nama_partman;
    String isi_partman, isi_foreman, isi_teknisi, isi_sa, nomor_polisi, type_kendaraan;

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
        tv_simpan = view.findViewById(R.id.tv_simpanData);


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

//        estimasiArrayList = new ArrayList<>();
        String total_hargaEstimasi = tv_granTotalHarga.getText().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        String key = databaseReference.push().getKey();

        for (int i = 0; i < arrayList.size(); i++) {
            iddataCart = arrayList.get(i).getId();
            nomorPart_cart = arrayList.get(i).getNomorPart();
            namaPart_cart = arrayList.get(i).getNamaPart();
            hargaPart_cart = arrayList.get(i).getHargaPart();
            isiSpiner = arrayList.get(i).getSt_Stok();
            jmlahItemPart = String.valueOf(arrayList.get(i).getJmlahItem());
            totalHargaItem = Double.parseDouble(String.valueOf(arrayList.get(i).getTotalHargaItem()));

//            ModelDataEstimasi dataEstimasi = new ModelDataEstimasi();
//            dataEstimasi.setNomorPart(nomorPart_cart);
//            dataEstimasi.setNamaPart(namaPart_cart);
//            dataEstimasi.setHargaPart(hargaPart_cart);
//            dataEstimasi.setJmlahItem(Integer.parseInt(jmlahItemPart));
//            dataEstimasi.setTotalHargaItem(totalHargaItem);
//            dataEstimasi.setSt_Stok(isiSpiner);

            if (arrayList.get(i).getJmlahItem()==0){
                databaseReference = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                userId = firebaseUser.getUid();
                databaseReference.child(userId).child("DataCart").child(iddataCart).removeValue();
                System.out.println("item yang kosong : " + nomorPart_cart);

            } else {

//                estimasiArrayList.add(dataEstimasi);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                userId = firebaseUser.getUid();
                String keyestimasi = databaseReference.child(userId).child("DataEstimasi").child(key).push().getKey();
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("total_HargaEstimasi").setValue(total_hargaEstimasi);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("namaSA").setValue(isi_sa);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("namaTeknisi").setValue(isi_teknisi);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("namaForeman").setValue(isi_foreman);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("namaPartman").setValue(isi_partman);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("nomorPolisi").setValue(nomor_polisi);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("typeKendaraan").setValue(type_kendaraan);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("userId").setValue(userEmail);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("tanggal").setValue(date);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("nomorPart").setValue(nomorPart_cart);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("namaPart").setValue(namaPart_cart);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("hargaPart").setValue(hargaPart_cart);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("jumlahItem").setValue(jmlahItemPart);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("jumlahHargaItem").setValue(totalHargaItem);
                databaseReference.child(userId).child("DataEstimasi").child(nomor_polisi).child(keyestimasi).child("Status_Stok").setValue(isiSpiner);
                System.out.println("data jumlah item : " + arrayList.size());

            }

        }
        Intent intent = new Intent(getActivity(), Home.class);
        startActivity(intent);
    }

    private void addFormInput() {

        final String[] namaSA = {"Aan", "Adin", "Andreas", "Denny", "Erwin", "Hadiid", "Heru", "Pradana", "Robby", "Taufik", "Septa"};
        final String[] namaTeknisi = {"Agus", "Aji", "Amin", "Ardy", "Crismon", "Dedi", "Denny", "Dian", "Dwi", "Fariz", "Hega", "Hendi", "Hengky", "Herry", "Husin", "Irawan", "Jarni", "Laksamana", "Lingga", "Mulyono", "Oky", "Rijal", "Robby", "Setiawab", "Yusuf"};
        final String[] namaForeman = {"Abdul", "Acep", "Efrizal", "Eko", "Faris", "Jummally", "Ridwan", "Wandiy"};
        final String[] namaPartman = {"Acan", "Zaenal", "Annis", "Gerry"};

        dialogInput = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.form_input_data_estimasi, null);
        dialogInput.setView(view);
        dialogInput.setCancelable(false);
        sp_nama_sa = (Spinner) view.findViewById(R.id.sp_nama_sa);
        sp_nama_teknisi = (Spinner) view.findViewById(R.id.sp_nama_teknisi);
        sp_nama_foreman = (Spinner) view.findViewById(R.id.sp_nama_foreman);
        sp_nama_partman = (Spinner) view.findViewById(R.id.sp_nama_partman);
        et_nomor_polisi = (EditText) view.findViewById(R.id.et_nomor_polisi);
        et_type_kendaraan = (EditText) view.findViewById(R.id.et_type_kendaraan);

        SpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, namaPartman);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_nama_partman.setAdapter(SpinnerAdapter);

        SpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, namaSA);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_nama_sa.setAdapter(SpinnerAdapter);

        SpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, namaTeknisi);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_nama_teknisi.setAdapter(SpinnerAdapter);

        SpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, namaForeman);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_nama_foreman.setAdapter(SpinnerAdapter);

        sp_nama_partman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isi_partman = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_nama_sa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isi_sa = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_nama_teknisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isi_teknisi = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_nama_foreman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isi_foreman = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialogInput.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                nomor_polisi = et_nomor_polisi.getText().toString();
                type_kendaraan = et_type_kendaraan.getText().toString();
                Toast.makeText(getActivity(), "Data : " + isi_sa, Toast.LENGTH_SHORT).show();
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
                                dataCart.setId(id);
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


        private ArrayAdapter<String> dataAdapter;
        private int isiJmlahItem;
        private int itemCount = 1;
        String tempSpiner;
        ArrayList<ModelDataCart> listcart;
        List<String> stStok = new ArrayList<String>();

        public DataCartAdapter(ArrayList<ModelDataCart> arrayList) {
            listcart = arrayList;

            stStok.add("T");
            stStok.add("G");
            stStok.add("D");

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
            dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, stStok);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

            cartViewHolder.spinnerStstok.setAdapter(dataAdapter);
            cartViewHolder.spinnerStstok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    tempSpiner = parent.getItemAtPosition(pos).toString();
                    listcart.get(position).setSt_Stok(tempSpiner);
                    isiSpiner = listcart.get(position).getSt_Stok();
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "Selected: " + isiSpiner, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            cartViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(getActivity(), "Test On LongClick" + listcart.get(position).getId(), Toast.LENGTH_SHORT).show();
                    konfirmasiHapus(listcart.get(position).getId(), listcart.get(position).getNomorPart());
                    return false;
                }

                private void konfirmasiHapus(final String id, String nomorPart) {

                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(getActivity());
                    alBuilder.setTitle("Konfirmasi Hapus");
                    alBuilder.setMessage("Apakah Anda Yakin Akan menghapus data  " + nomorPart + "?");
                    alBuilder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseUser = firebaseAuth.getCurrentUser();
                            userId = firebaseUser.getUid();
                            databaseReference.child(userId).child("DataCart").child(id).removeValue();


                            listcart.clear();
                            listcart.remove(position);
                            DataCartAdapter.this.notifyItemRemoved(position);
                            DataCartAdapter.this.notifyItemRangeChanged(position, listcart.size());
                            DataCartAdapter.this.notifyDataSetChanged();
                            mRecyclerView.removeAllViews();

                        }
                    });
                    alBuilder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            DataCartAdapter.this.notifyDataSetChanged();
                        }
                    }).show();

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
        private final CardView cardView;
        TextView tv_nomorPart;
        TextView tv_namaPart;
        TextView tv_hargaPart;
        TextView tv_total_harga_item;
        TextView tv_jumlahItem;
        Spinner spinnerStstok;

        public DataCartViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tv_nomorPart = itemView.findViewById(R.id.nomor_part_cart);
            tv_namaPart = itemView.findViewById(R.id.nama_part_cart);
            tv_hargaPart = itemView.findViewById(R.id.harga_part_cart);
            tv_total_harga_item = itemView.findViewById(R.id.tv_jmlah_harga_part_cart);
            tv_jumlahItem = itemView.findViewById(R.id.tv_jmlah_item_part_cart);
            plus = itemView.findViewById(R.id.btn_nambah);
            minus = itemView.findViewById(R.id.btn_ngurangin);
            spinnerStstok = itemView.findViewById(R.id.spin_ststok);
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
