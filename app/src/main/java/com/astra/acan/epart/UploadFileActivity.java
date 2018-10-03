package com.astra.acan.epart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class UploadFileActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
    }

    public void browse(View view) {
        checkPermissionsAndOpenFilePicker();
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

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            System.out.println("String Path : " + path);

            if (path != null) {
                System.out.println("String Path : " + path);
                ArrayList<ArrayList> data_get = load_excel_format_xls(path);
                getDataExcel(data_get);
                System.out.println("data size : " + data_get.size());
                Log.d("Path: ", path);
                Toast.makeText(UploadFileActivity.this, "Picked file: " + path, Toast.LENGTH_LONG).show();
            }
        }
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
//
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

        for (int i=0; i<data_get.size();i++) {
            String nomorpart = String.valueOf(data_get.get(i).get(0));
            String namaPart = String.valueOf(data_get.get(i).get(1));
            String hargaPart = String.valueOf(data_get.get(i).get(2));
            System.out.println("isi nomorpart : " + nomorpart);
            System.out.println("isi namapart : " + namaPart);
            System.out.println("isi hargapart : " + hargaPart);
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
