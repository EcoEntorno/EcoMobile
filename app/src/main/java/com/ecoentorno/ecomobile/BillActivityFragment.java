package com.ecoentorno.ecomobile;


import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;

import com.ecoentorno.ecomobile.model.Client;

import java.util.HashMap;
import java.util.Set;

import com.bixolon.printer.BixolonPrinter;
import com.ecoentorno.ecomobile.printer.DialogManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class BillActivityFragment extends Fragment {

    static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;
    static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;
    static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
    static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;

    static final String FIRMWARE_FILE_NAME = "FirmwareFileName";

    private String mConnectedDeviceName = null;
    private boolean mIsConnected;


    public static  BixolonPrinter mBixolonPrinter;
    public static final String TAG = "BixolonPrinterSample";

    public Button mFindPrinters;
    public Button mPrint;

    public static Client client;

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "mUsbReceiver.onReceive(" + context + ", " + intent + ")");
            String action = intent.getAction();

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                mBixolonPrinter.connect();
                Toast.makeText(getContext(), "Found USB device", Toast.LENGTH_SHORT).show();
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                mBixolonPrinter.disconnect();
                Toast.makeText(getContext(), "USB device removed", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private final android.os.Handler mHandler = new android.os.Handler(new android.os.Handler.Callback() {

        @SuppressWarnings("unchecked")
        @Override
        public boolean handleMessage(Message msg) {
            Log.d(TAG, "mHandler.handleMessage(" + msg + ")");

            switch (msg.what) {
                case BixolonPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BixolonPrinter.STATE_CONNECTED:
                            ////setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mListView.setEnabled(true);
                            mIsConnected = true;
                            //invalidateOptionsMenu();
                            break;

                        case BixolonPrinter.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;

                        case BixolonPrinter.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            //mListView.setEnabled(false);
                            mIsConnected = false;
                            //invalidateOptionsMenu();
                            //mProgressBar.setVisibility(View.INVISIBLE);
                            break;
                    }
                    return true;

                case BixolonPrinter.MESSAGE_WRITE:
                    switch (msg.arg1) {
                        case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:
                            mHandler.obtainMessage(MESSAGE_END_WORK).sendToTarget();

                            Toast.makeText(getContext(), "Complete to set double byte font.", Toast.LENGTH_SHORT).show();
                            break;

                        case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:
                            mBixolonPrinter.getDefinedNvImageKeyCodes();
                            Toast.makeText(getContext(), "Complete to define NV image", Toast.LENGTH_LONG).show();
                            break;

                        case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:
                            mBixolonPrinter.getDefinedNvImageKeyCodes();
                            Toast.makeText(getContext(), "Complete to remove NV image", Toast.LENGTH_LONG).show();
                            break;

                        case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:
                            mBixolonPrinter.disconnect();
                            Toast.makeText(getContext(), "Complete to download firmware.\nPlease reboot the printer.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;

                case BixolonPrinter.MESSAGE_READ:
                    //this.dispatchMessage(msg);
                    return true;

                case BixolonPrinter.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME);
                    Toast.makeText(getContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();
                    return true;

                case BixolonPrinter.MESSAGE_TOAST:
                    //mListView.setEnabled(false);
                    Toast.makeText(getContext(), msg.getData().getString(BixolonPrinter.KEY_STRING_TOAST), Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getContext(), "No paired device", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogManager.showBluetoothDialog(getContext(), (Set<BluetoothDevice>) msg.obj);
                    }
                    return true;

                case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
                    Toast.makeText(getContext(), "Complete to print", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_INVALID_ARGUMENT:
                    Toast.makeText(getContext(), "Invalid argument", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_NV_MEMORY_CAPACITY:
                    Toast.makeText(getContext(), "NV memory capacity error", Toast.LENGTH_SHORT).show();
                    return true;

                case BixolonPrinter.MESSAGE_ERROR_OUT_OF_MEMORY:
                    Toast.makeText(getContext(), "Out of memory", Toast.LENGTH_SHORT).show();
                    return true;


                case MESSAGE_START_WORK:
                    //mListView.setEnabled(false);
                    //mProgressBar.setVisibility(View.VISIBLE);
                    return true;

                case MESSAGE_END_WORK:
                    //mListView.setEnabled(true);
                    //mProgressBar.setVisibility(View.INVISIBLE);
                    return true;

                case BixolonPrinter.MESSAGE_USB_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        DialogManager.showUsbDialog(getContext(), (Set<UsbDevice>) msg.obj, mUsbReceiver);
                    }
                    return true;

                case BixolonPrinter.MESSAGE_USB_SERIAL_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    } else {
                        final HashMap<String, UsbDevice> usbDeviceMap = (HashMap<String, UsbDevice>) msg.obj;
                        final String[] items = usbDeviceMap.keySet().toArray(new String[usbDeviceMap.size()]);
                        new AlertDialog.Builder(getContext()).setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mBixolonPrinter.connect(usbDeviceMap.get(items[which]));
                            }
                        }).show();
                    }
                    return true;

                case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:
                    if (msg.obj == null) {
                        Toast.makeText(getContext(), "No connectable device", Toast.LENGTH_SHORT).show();
                    }
                    DialogManager.showNetworkDialog(getContext(), (Set<String>) msg.obj);
                    return true;
            }
            return false;
        }
    });
    

    public BillActivityFragment() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_FIRMWARE && resultCode == RESULT_CODE_SELECT_FIRMWARE) {
            final String binaryFilePath = data.getStringExtra(FIRMWARE_FILE_NAME);
            mHandler.obtainMessage(MESSAGE_START_WORK).sendToTarget();
            new Thread(new Runnable() {

                public void run() {
                    mBixolonPrinter.updateFirmware(binaryFilePath);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mHandler.obtainMessage(MESSAGE_END_WORK).sendToTarget();
                }
            }).start();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_bill, container, false);
        Intent intent = getActivity().getIntent();
        client = (Client)intent.getSerializableExtra("CLIENT");
        Toast.makeText(view.getContext(), client.getSocialName(), Toast.LENGTH_SHORT).show();
        mBixolonPrinter = new BixolonPrinter(view.getContext(), mHandler, null);

        mFindPrinters = (Button) view.findViewById(R.id.btFind);
        mFindPrinters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBixolonPrinter.findBluetoothPrinters();
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPrint = (Button) view.findViewById(R.id.btPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int aligment = BixolonPrinter.ALIGNMENT_LEFT;
                    int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL1;
                    size |= BixolonPrinter.TEXT_SIZE_VERTICAL1;
                    int attribute = BixolonPrinter.TEXT_ATTRIBUTE_FONT_A;
                    //attribute |= BixolonPrinter.TEXT_ATTRIBUTE_UNDERLINE1;
                    //attribute |= BixolonPrinter.TEXT_ATTRIBUTE_EMPHASIZED;
                    String text = new String();

                    text= "-------------------------------\n";
                    text+="|ECOLOGIA Y ENTORNO S.A. E.S.P|\n";
                    text+="|          BOGOTA D.C.        |\n";
                    text+="|" + client.getSocialName()+"|\n";
                    text+="|" + client.getAddress()+"|\n";
                    text+="|" + client.getCity()+"|\n";
                    text+="|" + client.getGroup()+"|\n";
                    text+="|" + client.getNit()+"|\n";
                    text+="|" + client.getTelephone()+"|\n";
                    text+="|" + client.getVerificationDigit()+"|\n";
                    text+="-------------------------------\n";

                    mBixolonPrinter.printText(text, aligment, attribute, size, false);
                    mBixolonPrinter.lineFeed(3, false);
                    mBixolonPrinter.cutPaper(true);
                    mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


}
