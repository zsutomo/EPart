package com.astra.acan.epart;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class UploadFileActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private DatabaseReference userReference;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String userId;
    private String userEmail;
    private String nomorPart;
    private String namaPart;
    private String hargaPart;
    private String path;
    private DatabaseReference storageData;
    private final int PICK_EXCEL_REQUEST=1000;
    private Uri pathUri;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvStatus = (TextView) findViewById(R.id.textViewStatus);
        btn_upload = (Button) findViewById(R.id.btn_upload);


        userReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userEmail = firebaseUser.getEmail();

        System.out.println("user" + userId);
    }

    public void browse(View view) {

//        userReference.child(userId).removeValue();

//        checkPermissionsAndOpenFilePicker();

        chooseFileToUpload();
    }

    private void chooseFileToUpload() {
        Intent intent = new Intent();
        intent.setType("application/vnd.ms-excel");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Excel"), PICK_EXCEL_REQUEST);
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(UploadFileActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadFileActivity.this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(UploadFileActivity.this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_EXCEL_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pathUri = data.getData();
            if (pathUri != null) {
                System.out.println("String Path : " + pathUri);
                UploadFileToServer(pathUri);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//
////                        ArrayList<ArrayList> data_get = load_excel_format_xls(path);
////                        getDataExcel(data_get);
////                        System.out.println("data size : " + data_get.size());
//                        Log.d("Path: ", String.valueOf(pathUri));
//                    }
//                }).start();
            } else {
                Toast.makeText(this, "Tidak Ada data yang terpilih", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadFileToServer(final Uri fileUri) {
        btn_upload.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        System.out.println("file selected : " + fileUri.getLastPathSegment());
        StorageReference fileStorageReference, storageReference;
        fileStorageReference = FirebaseStorage.getInstance().getReference().child(userEmail);
        storageReference = fileStorageReference.child("fileUpload").child(fileUri.getLastPathSegment());
        storageReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UploadFileActivity.this);
                alertDialogBuilder.setTitle("Success");
                alertDialogBuilder.setMessage("Your File Uploaded Successfully");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UploadFileActivity.this, Home.class);
                        startActivity(intent);
                        UploadFileActivity.this.finish();
                    }
                });

                alertDialogBuilder.create();
                alertDialogBuilder.show();

                progressBar.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                tvStatus.setText((int) progress + "% Uploading...");

            }
        });

    }

    public ArrayList<ArrayList> load_excel_format_xls(String filename) {

        ArrayList<ArrayList> data = new ArrayList<>();

        try {
            FileInputStream excelFile = new FileInputStream(new File(filename));
            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet datatypeSheet = (Sheet) workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            ArrayList<String> data_from_excel = new ArrayList<>();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                ArrayList<Double> angka = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        data_from_excel.add(currentCell.getStringCellValue().trim().toLowerCase());
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        data_from_excel.add(String.valueOf((double) currentCell.getNumericCellValue()));
                    }
                }
                data.add(data_from_excel);
                data_from_excel = new ArrayList<>();
            }
        } catch (IOException ex) {

        }
        return data;
    }

    private void getDataExcel(ArrayList<ArrayList> data_get) {

        System.out.println("data get isi : " + data_get.size());

        for (int i = 1; i < data_get.size(); i++) {
            nomorPart = String.valueOf(data_get.get(i).get(0));
            namaPart = String.valueOf(data_get.get(i).get(1));
            hargaPart = String.valueOf(data_get.get(i).get(2));

            System.out.println("isi nomorpart : " + nomorPart);
            System.out.println("isi namapart : " + namaPart);
            System.out.println("isi hargapart : " + hargaPart);

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final String key = databaseReference.push().getKey();


            if (databaseReference.child(userId).child("FileUpload").getKey().isEmpty()){

                databaseReference.child(userId).child("FileUpload").child(key).child("id").setValue(key);
                databaseReference.child(userId).child("FileUpload").child(key).child("NomorPart").setValue(nomorPart);
                databaseReference.child(userId).child("FileUpload").child(key).child("NamaPart").setValue(namaPart);
                databaseReference.child(userId).child("FileUpload").child(key).child("HargaPart").setValue(hargaPart);

            } else {

                databaseReference.child(userId).child("FileUpload").child(key).child("id").setValue(key);
                databaseReference.child(userId).child("FileUpload").child(key).child("NomorPart").setValue(nomorPart);
                databaseReference.child(userId).child("FileUpload").child(key).child("NamaPart").setValue(namaPart);
                databaseReference.child(userId).child("FileUpload").child(key).child("HargaPart").setValue(hargaPart);
            }




        }
    }

    private void showError() {
        Toast.makeText(UploadFileActivity.this, "Eror", Toast.LENGTH_SHORT).show();
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .start();

    }

}
