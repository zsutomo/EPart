package com.astra.acan.epart;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListDataFile extends Fragment {


    private RecyclerView mRecylerView;
    ArrayList<UploadInfo> arrayListFile = new ArrayList<>();
    DatabaseReference fileReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private String userEmail;
    private StorageReference fileUploadReference;
    private StorageReference fileRef;
    private ProgressDialog progressDialog;

    public ListDataFile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_data_file, container, false);
        initView(view);


        fileReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();
        fileUploadReference = FirebaseStorage.getInstance().getReference().child(userEmail).child("fileUpload");
        fileRef=null;

        progressDialog = new ProgressDialog(getActivity());

        showDataFromServer();

        mRecylerView.setHasFixedSize(true);
        LinearLayoutManager myLinearManager = new LinearLayoutManager(getActivity());
        myLinearManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(arrayListFile.size()>0 & mRecylerView != null){
            mRecylerView.setAdapter(new ListDataFileAdapter(arrayListFile));
        }
        mRecylerView.setLayoutManager(myLinearManager);


        return view;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        mRecylerView = (RecyclerView) view.findViewById(R.id.recycler_file);
    }


    private class ListDataFileAdapter extends RecyclerView.Adapter<ListFileViewHolder> {

        private ArrayList<UploadInfo> list;



        public ListDataFileAdapter(ArrayList<UploadInfo> arrayListFile) {
            list = arrayListFile;
        }

        @NonNull
        @Override
        public ListFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);

            ListFileViewHolder holder = new ListFileViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ListFileViewHolder listFileViewHolder, final int position) {
            listFileViewHolder.namaFile.setText(list.get(position).getFile_name());
            listFileViewHolder.tanggalUpload.setText(list.get(position).getDate_upload());
            listFileViewHolder.userName.setText(list.get(position).getUser_upload());
            listFileViewHolder.simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Test" + list.get(position).getFile_name(), Toast.LENGTH_SHORT).show();
                    String url = list.get(position).getFile_url();
                    System.out.println("url : " + url);
//                    downloadFileToMemory(fileUploadReference);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private void downloadFileToMemory(StorageReference fileRef) {
        if (fileRef != null) {
            System.out.println("fileref : " + fileUploadReference);
            progressDialog.setTitle("Downloading...");
            progressDialog.setMessage(null);
            progressDialog.show();

            try {
                File localFile = File.createTempFile("Downloads", "xls");
                fileUploadReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("Success");
                        alertDialogBuilder.setMessage("Your File Uploaded Successfully");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                        Intent intent = new Intent(getContext(), Home.class);
//                        startActivity(intent);

                            }
                        });
                        AlertDialog dialog = alertDialogBuilder.create();
                        dialog.show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        // percentage in progress dialog
                        progressDialog.setMessage("Downloaded " + ((int) progress) + "%...");

                    }
                });

            } catch (IOException e) {

            }
        }
    }

    private class ListFileViewHolder extends RecyclerView.ViewHolder {
        public TextView namaFile;
        public CardView cardviews;
        public TextView tanggalUpload;
        public TextView fileUrl;
        public TextView userName;
        public TextView simpan;
        public TextView hapus;



        public ListFileViewHolder(@NonNull View view) {
            super(view);

            namaFile = view.findViewById(R.id.nama_file);
            tanggalUpload = view.findViewById(R.id.waktu_upload);
            userName = view.findViewById(R.id.user_upload);
            hapus = view.findViewById(R.id.hapus_data);
            simpan = view.findViewById(R.id.simpan_data);
            cardviews = view.findViewById(R.id.card_view);
        }
    }

    public void showDataFromServer(){

        fileReference.child(userId).child("FileUpload").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListFile.clear();
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()){

                    UploadInfo uploadInfo = new UploadInfo();

                    uploadInfo.setFile_name(fileSnapshot.child("nama_file").getValue().toString());
                    uploadInfo.setDate_upload(fileSnapshot.child("tanggal").getValue().toString());
                    uploadInfo.setUser_upload(fileSnapshot.child("username").getValue().toString());
                    uploadInfo.setFile_url(fileSnapshot.child("url").getValue().toString());
                    arrayListFile.add(uploadInfo);

//                    String key = fileSnapshot.getKey();
//                    String namaFile = fileSnapshot.child("nama_file").getValue().toString();
//                    String tanggal = fileSnapshot.child("tanggal").getValue().toString();
//                    String url = fileSnapshot.child("url").getValue().toString();
//                    String username = fileSnapshot.child("username").getValue().toString();

                    System.out.println("isi : " + uploadInfo.getUser_upload() + arrayListFile.size());
                }
                mRecylerView.setAdapter(new ListDataFileAdapter(arrayListFile));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
