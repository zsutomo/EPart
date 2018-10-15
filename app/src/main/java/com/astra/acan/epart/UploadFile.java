package com.astra.acan.epart;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import static android.app.Activity.RESULT_OK;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFile extends Fragment {

    private static final String TAG = "FileUploadFragment";

    private FirebaseStorage firebaseStorage;
    private EditText filePath;
    private String userId;
    private String userPath;
    private final int PICK_EXCEL_REQUEST=1000;
    private Button btn_browseFile;
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private Uri pathUri;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private DatabaseReference userReference;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userEmail;
    private String path;
    private DatabaseReference storageData;
    private EditText et_inputfilename;
    private String fileNama;
    private Calendar calander;
    private SimpleDateFormat simpledateformat;
    private String date;

    private static final String[] PUBLIC_DIR = {Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOWNLOADS).getPath(),
            Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES).getPath(),
            Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_MUSIC).getPath(),
            Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_MOVIES).getPath()};
    private ProgressDialog progressDialog;


    public UploadFile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        initView(view);
        browse(view);

        progressDialog = new ProgressDialog(getActivity());

        userReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userReference.removeValue();
            }
        });

        getDateTime();

        return view;
    }

    private void getDateTime() {
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calander.getTime());
    }

    private void initView(View view) {
        et_inputfilename = (EditText) view.findViewById(R.id.et_namafile);
        btn_browseFile = view.findViewById(R.id.btn_upload);
    }

    public void browse(View view) {
        btn_browseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileNama = et_inputfilename.getText().toString();

                if (!TextUtils.isEmpty(fileNama)){
                    chooseFileToUpload();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Gagal");
                    alertDialogBuilder.setMessage("Nama File Tidak Boleh Kosong");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                }
//                checkPermissionsAndOpenFilePicker();
            }
        });
        
    }

    private void chooseFileToUpload() {

        Intent intent = new Intent();
        intent.setType("application/vnd.ms-excel");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Excel"), PICK_EXCEL_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    private void showError() {
        Toast.makeText(getActivity(), "Eror", Toast.LENGTH_SHORT).show();
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .start();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pathUri = data.getData();
            if (pathUri != null) {
                System.out.println("String Path : " + pathUri);
                UploadFileToServer(pathUri);
            } else {
                Toast.makeText(getContext(), "Tidak Ada File yang terpilih", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadFileToServer(Uri fileUri) {
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage(null);
        progressDialog.show();

        System.out.println("file selected : " + fileUri.getLastPathSegment());
        StorageReference fileStorageReference, storageReference;
        fileStorageReference = FirebaseStorage.getInstance().getReference().child(userEmail);
        storageReference = fileStorageReference.child("fileUpload").child(fileNama + "." + getFileExtension(pathUri));
        storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                et_inputfilename.setText("");

                final String name = taskSnapshot.getMetadata().getName();
                final String url = String.valueOf(taskSnapshot.getStorage().getDownloadUrl());

                Log.e(TAG, "Uri: " + url);
                Log.e(TAG, "Name: " + name);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Success");
                alertDialogBuilder.setMessage("Your File Uploaded Successfully");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        saveDataToDB(name,url, date, userEmail);
                        dialog.dismiss();
//                        Intent intent = new Intent(getContext(), Home.class);
//                        startActivity(intent);

                    }
                });

                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                // percentage in progress dialog
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

            }
        });

    }

    private void saveDataToDB(String name, String url, String tanggal, String userEmail) {

        String key = userReference.push().getKey();
        userReference.child(userId).child("FileUpload").child(key).child("nama_file").setValue(name);
        userReference.child(userId).child("FileUpload").child(key).child("tanggal").setValue(tanggal);
        userReference.child(userId).child("FileUpload").child(key).child("url").setValue(url);
        userReference.child(userId).child("FileUpload").child(key).child("username").setValue(userEmail);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}
