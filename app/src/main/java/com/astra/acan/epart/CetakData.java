package com.astra.acan.epart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class CetakData extends AppCompatActivity implements Runnable {


    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private Button btn_scan;
    private Button btn_print;
    private Button btn_off;
    ArrayList<ModelItem> itemArrayList = new ArrayList<>();
    private String tanggal;
    private String nama_sa;
    private String nama_teknisi;
    private String nama_foreman;
    private String nomor_polisi;
    private String type_kendaraan;
    private String admin;
    private String total_estimasi;
    private Calendar calander;
    private SimpleDateFormat simpledateformat;
    private String date;
    private String nama_partman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_cetak_data);

        itemArrayList = (ArrayList<ModelItem>) getIntent().getSerializableExtra("arraylist");
        tanggal = getIntent().getStringExtra("tanggal");
        nama_sa = getIntent().getStringExtra("namaSA");
        nama_teknisi = getIntent().getStringExtra("nama_teknisi");
        nama_foreman = getIntent().getStringExtra("nama_foreman");
        nama_partman = getIntent().getStringExtra("nama_partman");
        nomor_polisi = getIntent().getStringExtra("nomor_polisi");
        type_kendaraan = getIntent().getStringExtra("type_kendaraan");
        admin = getIntent().getStringExtra("admin");
        total_estimasi = getIntent().getStringExtra("total_estimasi");

        System.out.println("arrayList Data : " +total_estimasi);

        btn_scan = (Button) findViewById(R.id.scan_btn);
        btn_print = (Button) findViewById(R.id.print_btn);
        btn_off = (Button) findViewById(R.id.shutdown_btn);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cariDeviceTerpasang();
            }
        });

        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();
            }
        });

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetakData();
            }
        });

    }

    private void cetakData() {
        getDateTime();

        Thread t = new Thread() {

            public void run() {
                try {

                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String BILL = "";

                    BILL = "    STRUK ESTIMASI PART MOBIL\n"
                            + "     AUTO 2000 TAA \n "
                            + "   " + tanggal+  "\n";
                    BILL = BILL + "--------------------------------\n";

                    BILL = BILL + nama_sa + "      " + nama_foreman+ "\n";
                    BILL = BILL + "Partman : " + nama_partman + "   " + nama_teknisi+ "\n";
                    BILL = BILL + type_kendaraan +"\n";
                    BILL = BILL + nomor_polisi+ "\n";

                    BILL = BILL + "--------------------------------\n";
                    BILL = BILL + String.format("%1$-6s %2$15s %3$9s", "Item", "Harga", "St");
                    BILL = BILL + "\n--------------------------------\n";

                    for (int i = 0; i<itemArrayList.size(); i++) {
                        String nomorPart = itemArrayList.get(i).getNomorPart();
                        String namaPart = itemArrayList.get(i).getNamaPart().substring(0,6);
                        String hargaPart = itemArrayList.get(i).getHargaPart().trim();
                        String JumlahItem = itemArrayList.get(i).getJmlahItem();
                        String st_stok = itemArrayList.get(i).getStatusStok();
                        String hargaItem = itemArrayList.get(i).getTotalHargaItem();

                        System.out.println("stok : " + nomorPart);

                        BILL = BILL + String.format("%1$-12s %2$17s %3$8s",nomorPart +"\n"+namaPart, hargaPart, st_stok + "\n");
                    }

                    BILL = BILL + "--------------------------------";

                    BILL = BILL + "*NB :"+"\n";
                    BILL = BILL + "  St = Status Stok"+"\n";
                    BILL = BILL + "  G = Gudang"+"\n";
                    BILL = BILL + "  D = Depo"+"\n";
                    BILL = BILL + "  T = Tam"+"\n";

                    BILL = BILL + "--------------------------------";
                    BILL = BILL + "admin : " + admin + "\n";
                    BILL = BILL + "--------------------------------";

                    BILL = BILL + "Stok Status Part\n";
                    BILL = BILL + "Tanggal : " + date + "\n";

                    BILL = BILL + "TERIMAKASIH ATAS KUNJUNGAN ANDA \n";
                    BILL = BILL + "-------AUTO 2000 TAA------\n";
                    BILL = BILL + "\n";
                    os.write(BILL.getBytes());
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 10;
                    os.write(intToByteArray(gs));
                    int h = 80;
                    os.write(intToByteArray(h));
                    int n = 80;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 10;
                    os.write(intToByteArray(gs_width));
                    int w = 80;
                    os.write(intToByteArray(w));
                    int n_width = 1;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();


    }

    private void cariDeviceTerpasang() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(CetakData.this, "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                Intent connectIntent = new Intent(CetakData.this,
                        ListDevice.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(CetakData.this,
                            ListDevice.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(CetakData.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(CetakData.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    private void getDateTime() {
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calander.getTime());
    }

}