package com.letsrace;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	TextView textResponse;
	Button btnCheckWifi;
	Button btnActAccelerometer;
	Button btnActAbout;

	SharedPreferences mySharedPreferences;
	String wifiServer;
	String wifiServerPort;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView textv = (TextView) findViewById(R.id.textView1);
		textv.setShadowLayer(1, 3, 3, Color.GRAY);

		btnActAccelerometer = (Button) findViewById(R.id.btnAccel);
		btnActAccelerometer.setOnClickListener(this);

		btnActAbout = (Button) findViewById(R.id.button_about);
		btnActAbout.setOnClickListener(this);

		btnCheckWifi = (Button) findViewById(R.id.btnCheckWifi);

		textResponse = (TextView) findViewById(R.id.txtviewResponse);

		mySharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		wifiServer = (String) getResources().getText(
				R.string.default_WiFiServer);
		wifiServerPort = (String) getResources().getText(
				R.string.default_WiFiServerPort);

		OnClickListener btnCheckWifiOnClickListener = new OnClickListener() {
			public void onClick(View arg0) {
				textResponse.setText("waiting for server response");
				class MyClientTask extends AsyncTask<Void, Void, Void> {

					String dstAddress;
					int dstPort;
					String response = "";

					// Socket socket = null;

					MyClientTask(String addr, int port) {
						dstAddress = addr;
						dstPort = port;
					}

					@Override
					protected Void doInBackground(Void... arg0) {

						// Socket socket = null;

						try {
							// socket = new Socket(dstAddress, dstPort);
							SocketAddress sockaddr = new InetSocketAddress(
									dstAddress, dstPort);
							// Create your socket
							Socket socket = new Socket();
							// Connect with 10 s timeout
							socket.connect(sockaddr, 5000);

							DataInputStream din = new DataInputStream(
									socket.getInputStream());
							DataOutputStream dout = new DataOutputStream(
									socket.getOutputStream());

							// dout.writeUTF("hello from client");
							if (dstPort == 1111)
								dout.writeUTF("chk1");
							else if (dstPort == 2222)
								dout.writeUTF("chk2");
							else if (dstPort == 3333)
								dout.writeUTF("chk3");

							response = din.readUTF();
							if (response != null
									&& response.equalsIgnoreCase("OkWifi")) {
								MainActivity.this.runOnUiThread(new Runnable() {
									public void run() {
										MainActivity.this.textResponse
												.setText("WiFi OK.");
									}
								});
							}

							// onPostExecute will display the message from
							// server

							socket.close();

						} catch (SocketTimeoutException ste) {
							ste.printStackTrace();
							MainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									MainActivity.this.textResponse
											.setText("WiFi NG. timeout.");
								}
							});
						} catch (UnknownHostException e) {
							e.printStackTrace();
							MainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									MainActivity.this.textResponse
											.setText("WiFi NG. unknown host.");
								}
							});
						} catch (IOException e) {
							e.printStackTrace();
							MainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									MainActivity.this.textResponse
											.setText("WiFi NG.");
								}
							});
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						// super.onPostExecute(result);
					}

				}

				MyClientTask myClientTask = new MyClientTask(wifiServer,
						Integer.parseInt(wifiServerPort));
				myClientTask.execute();
			}
		};

		btnCheckWifi.setOnClickListener(btnCheckWifiOnClickListener);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAccel:
			// Intent intent_ready = new Intent(this, ActivityReady.class);
			// startActivity(intent_ready);
			Intent intent_accel = new Intent(this, ActivityAccelerometer.class);
			startActivity(intent_accel);
			break;
		case R.id.button_about:
			Intent intent_about = new Intent(this, ActivityAbout.class);
			startActivity(intent_about);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SetPreferenceActivity.class);
		startActivityForResult(intent, 0);

		return true;
	}

}
