package com.ecoentorno.ecomobile.printer;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bixolon.printer.BixolonPrinter;
import com.ecoentorno.ecomobile.BillActivityFragment;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

public class DialogManager {

	private static final String[] CODE_PAGE_ITEMS = {
		"Page 0 437 (USA, Standard Europe)",
		"Page 1 Katakana",
		"Page 2 850 (Multilingual)",
		"Page 3 860 (Portuguese)",
		"Page 4 863 (Canadian-French)",
		"Page 5 865 (Nordic)",
		"Page 16 1252 (Latin I)",
		"Page 17 866 (Cyrillic #2)",
		"Page 18 852 (Latin 2)",
		"Page 19 858 (Euro)",
		"Page 21 862 (Hebrew DOS code)",
		"Page 22 864 (Arabic)",
		"Page 23 Thai42",
		"Page 24 1253 (Greek)",
		"Page 25 1254 (Turkish)",
		"Page 26 1257 (Baltic)",
		"Page 27 Farsi",
		"Page 28 1251 (Cyrillic)",
		"Page 29 737 (Greek)",
		"Page 30 775 (Baltic)",
		"Page 31 Thai14",
		"Page 33 1255 (Hebrew New code)",
		"Page 34 Thai 11",
		"Page 35 Thai 18",
		"Page 36 855 (Cyrillic)",
		"Page 37 857 (Turkish)",
		"Page 38 928 (Greek)",
		"Page 39 Thai 16",
		"Page 40 1256 (Arabic)",
		"Page 41 1258 (Vietnam)",
		"Page 42 KHMER(Cambodia)",
		"Page 47 1250 (Czech)",
		"KS5601 (double byte font)",
		"BIG5 (double byte font)",
		"GB2312 (double byte font)",
		"SHIFT-JIS (double byte font)"
	};

	private static final String[] PRINTER_ID_ITEMS = {
		"Firmware version",
		"Manufacturer",
		"Printer model",
		"Code page"
	};
	
	private static final String[] PRINT_SPEED_ITEMS = {
		"High speed",
		"Medium speed",
		"Low Speed"
	};
	
	private static final String[] PRINT_DENSITY_ITEMS = {
		"Light density",
		"Default density",
		"Dark density"
	};
	
	private static final String[] PRINT_COLOR_ITEMS = {
		"Black",
		"Red"
	};
	
	public static void showBluetoothDialog(Context context, final Set<BluetoothDevice> pairedDevices) {
		final String[] items = new String[pairedDevices.size()];
		int index = 0;
		for (BluetoothDevice device : pairedDevices) {
			items[index++] = device.getAddress();
		}

		new AlertDialog.Builder(context).setTitle("Paired Bluetooth printers")
				.setItems(items, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						BillActivityFragment.mBixolonPrinter.connect(items[which]);
						
					}
				}).show();
	}
	
	public static void showUsbDialog(final Context context, final Set<UsbDevice> usbDevices, final BroadcastReceiver usbReceiver) {
		final String[] items = new String[usbDevices.size()];
		int index = 0;
		for (UsbDevice device : usbDevices) {
			items[index++] = "Device name: " + device.getDeviceName() + ", Product ID: " + device.getProductId() + ", Device ID: " + device.getDeviceId();
		}

		new AlertDialog.Builder(context).setTitle("Connected USB printers")
				.setItems(items, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						BillActivityFragment.mBixolonPrinter.connect((UsbDevice) usbDevices.toArray()[which]);
						
						// listen for new devices
						IntentFilter filter = new IntentFilter();
						filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
						filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
						context.registerReceiver(usbReceiver, filter);
					}
				}).show();
	}

	public static void showNetworkDialog(Context context, Set<String> ipAddressSet) {
		if (ipAddressSet != null) {
			 final String[] items = ipAddressSet.toArray(new String[ipAddressSet.size()]);
			
			new AlertDialog.Builder(context).setTitle("Connectable network printers")
			.setItems(items, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					BillActivityFragment.mBixolonPrinter.connect(items[which], 9100, 5000);
				}
			}).show();
		}
	}



	static void showPrinterIdDialog(AlertDialog dialog, Context context) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(context).setTitle("Get printer ID")
					.setItems(PRINTER_ID_ITEMS, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
								case 0:
									BillActivityFragment.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_FIRMWARE_VERSION);
									break;
								case 1:
									BillActivityFragment.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_MANUFACTURER);
									break;
								case 2:
									BillActivityFragment.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_PRINTER_MODEL);
									break;
								case 3:
									BillActivityFragment.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_CODE_PAGE);
									break;
							}

						}
					}).create();

		}
		dialog.show();
	}

	private static int mSpeed = BixolonPrinter.PRINT_SPEED_HIGH;

}
