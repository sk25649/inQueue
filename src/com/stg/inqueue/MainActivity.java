package com.stg.inqueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.stackmob.android.sdk.common.StackMobAndroid;

public class MainActivity extends Activity {
	public ArrayList<String> restaurantsArrayList;
	public ArrayAdapter<String> restaurantsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StackMobAndroid.init(getApplicationContext(), 0, "f66ba52f-9d96-47a6-97ad-ec4bc95e9687");

		// Set up initial lists of restaurants.
		setupRestaurantList();

		// Set up necessary tabs.
		setupTabs();

		// testing purpose
		// Toast.makeText(this, getDeviceID(), Toast.LENGTH_LONG).show();
		// Toast.makeText(this, getPhoneNumber(), Toast.LENGTH_LONG).show();
		Task myTask = new Task("Learn more about StackMob", new Date());
		myTask.save();
	}

	private void setupRestaurantList() {
		ListView lv = (ListView) findViewById(R.id.restaurants_available_list);
		restaurantsArrayList = new ArrayList<String>();

		// TODO: Make a separate method to add restaurants (possibly throught
		// the internet?) and save the list so that it doesn't continually add
		// it to the list.
		restaurantsArrayList.add("Olive Garden");
		restaurantsArrayList.add("Cheescake Factory");
		restaurantsArrayList.add("Perry's Steakhouse");
		restaurantsArrayList.add("Fogo de Chao");

		restaurantsAdapter = new ArrayAdapter<String>(this,
				R.layout.restaurant_row, restaurantsArrayList);
		lv.setAdapter(restaurantsAdapter);
		// restaurantsAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public String getDeviceID() {
		/*
		 * //String Return_DeviceID =
		 * USERNAME_and_PASSWORD.getString(DeviceID_key,"Guest"); //return
		 * Return_DeviceID;
		 * 
		 * TelephonyManager TelephonyMgr = (TelephonyManager)
		 * getApplicationContext
		 * ().getApplicationContext().getSystemService(Context
		 * .TELEPHONY_SERVICE); String m_szImei = TelephonyMgr.getDeviceId(); //
		 * Requires // READ_PHONE_STATE
		 * 
		 * // 2 compute DEVICE ID String m_szDevIDShort = "35" + // we make this
		 * look like a valid IMEI Build.BOARD.length() % 10 +
		 * Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 +
		 * Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +
		 * Build.HOST.length() % 10 + Build.ID.length() % 10 +
		 * Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +
		 * Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +
		 * Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13 digits //
		 * 3 android ID - unreliable String m_szAndroidID =
		 * Secure.getString(getContentResolver(),Secure.ANDROID_ID); // 4 wifi
		 * manager, read MAC address - requires //
		 * android.permission.ACCESS_WIFI_STATE or comes as null WifiManager wm
		 * = (WifiManager)
		 * getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		 * String m_szWLANMAC = wm.getConnectionInfo().getMacAddress(); // 5
		 * Bluetooth MAC address android.permission.BLUETOOTH required
		 * BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth
		 * adapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		 * String m_szBTMAC = m_BluetoothAdapter.getAddress();
		 * System.out.println("m_szBTMAC "+m_szBTMAC);
		 * 
		 * // 6 SUM THE IDs String m_szLongID = m_szImei + m_szDevIDShort +
		 * m_szAndroidID+ m_szWLANMAC + m_szBTMAC;
		 * System.out.println("m_szLongID "+m_szLongID); MessageDigest m = null;
		 * try { m = MessageDigest.getInstance("MD5"); } catch
		 * (NoSuchAlgorithmException e) { e.printStackTrace(); }
		 * m.update(m_szLongID.getBytes(), 0, m_szLongID.length()); byte
		 * p_md5Data[] = m.digest();
		 * 
		 * String m_szUniqueID = new String(); for (int i = 0; i <
		 * p_md5Data.length; i++) { int b = (0xFF & p_md5Data[i]); // if it is a
		 * single digit, make sure it have 0 in front (proper // padding) if (b
		 * <= 0xF) m_szUniqueID += "0"; // add number to string m_szUniqueID +=
		 * Integer.toHexString(b); } m_szUniqueID = m_szUniqueID.toUpperCase();
		 * 
		 * Log.i("-------------DeviceID------------", m_szUniqueID);
		 * Log.d("DeviceIdCheck",
		 * "DeviceId that generated MPreferenceActivity:"+m_szUniqueID);
		 * 
		 * 
		 * //System.out.println(m_szUniqueID); return m_szUniqueID;
		 * 
		 * }
		 */

		// for prototype purposes, this is fine
		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return deviceId;

	}

	public String getPhoneNumber() {

		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmNumber;

		tmNumber = "" + tm.getLine1Number();
		return tmNumber.toString();

	}

	// set up the ActionBar's tabs
	private void setupTabs() {
		ActionBar queueActionBar = getActionBar(); // get the ActionBar

		// set ActionBar's navigation mode to use tabs
		queueActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab restaurantsTab = queueActionBar.newTab();

		// set the Tab's title
		restaurantsTab.setText("Restaurants");

		// add the Tab
		restaurantsTab.setTabListener(queueTabListener);

		// set the Tab's listener
		queueActionBar.addTab(restaurantsTab);

		Tab positionTab = queueActionBar.newTab();
		positionTab.setText("Position");
		positionTab.setTabListener(queueTabListener);
		queueActionBar.addTab(positionTab);
	}

	// listen for events generated by the ActionBar Tabs
	TabListener queueTabListener = new TabListener() {
		// called when the selected Tab is re-selected
		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		}

		// called when a previously unselected Tab is selected
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction arg1) {
			// display the information corresponding to the selected Tab
			if (tab.getPosition() == 1)
				setContentView(R.layout.position_in_line);
			else {
				setContentView(R.layout.main);
				ListView lv = (ListView) findViewById(R.id.restaurants_available_list);
				lv.setAdapter(restaurantsAdapter);
				restaurantsAdapter.notifyDataSetChanged();
			}
		}

		// called when a tab is unselected
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		}
	};
}
