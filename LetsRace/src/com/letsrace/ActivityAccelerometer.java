package com.letsrace;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAccelerometer extends Activity implements
        SensorEventListener {

    RaceStartServer raceStartServer;

    private SensorManager mSensorManager;
    private Sensor mAccel;

    private cBluetooth btObject = null;
    private String btAddress;

    Button btnForward;
    Button btnBackward;

    private boolean showDebug;

    private String wifiServer;
    private String wifiServerPort;
    private String raceStartServerPort;

    private String centerPosition;
    private int centerPositionInt;
    private String centerRange;

    private String turnMsg;
    private float tilt;

    private final String msgForward = "+";
    private final String msgStopForward = "-";
    private final String msgBackward = "(";
    private final String msgStopBackward = ")";

    private final String bltSend00 = "a";
    private final String bltSend01 = "b";
    private final String bltSend02 = "c";
    private final String bltSend03 = "d";
    private final String bltSend04 = "e";
    private final String bltSend05 = "f";
    private final String bltSend06 = "g";
    private final String bltSend07 = "h";
    private final String bltSend08 = "i";
    private final String bltSend09 = "j";
    private final String bltSend10 = "k";
    private final String bltSend11 = "l";
    private final String bltSend12 = "m";
    private final String bltSend13 = "n";
    private final String bltSend14 = "o";
    private final String bltSend15 = "p";
    private final String bltSend16 = "q";
    private final String bltSend17 = "r";
    private final String bltSend18 = "s";
    private final String bltSend19 = "t";
    private final String bltSend20 = "u";
    private final String bltSend21 = "v";
    private final String bltSend22 = "w";
    private final String bltSend23 = "x";
    private final String bltSend24 = "y";
    private final String bltSend25 = "A";
    private final String bltSend26 = "B";
    private final String bltSend27 = "C";
    private final String bltSend28 = "D";
    private final String bltSend29 = "E";
    private final String bltSend30 = "F";
    private final String bltSend31 = "G";
    private final String bltSend32 = "H";
    private final String bltSend33 = "I";
    private final String bltSend34 = "J";
    private final String bltSend35 = "K";
    private final String bltSend36 = "L";
    private final String bltSend37 = "M";
    private final String bltSend38 = "N";
    private final String bltSend39 = "O";
    private final String bltSend40 = "P";
    private final String bltSend41 = "Q";
    private final String bltSend42 = "R";
    private final String bltSend43 = "S";
    private final String bltSend44 = "T";
    private final String bltSend45 = "U";
    private final String bltSend46 = "V";
    private final String bltSend47 = "W";
    private final String bltSend48 = "X";
    private final String bltSend49 = "Y";
    private final String bltSend50 = "0";
    private final String bltSend51 = "1";
    private final String bltSend52 = "2";
    private final String bltSend53 = "3";
    private final String bltSend54 = "4";
    private final String bltSend55 = "5";
    private final String bltSend56 = "6";
    private final String bltSend57 = "7";
    private final String bltSend58 = "8";
    private final String bltSend59 = "9";
    private final String bltSend60 = "*";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        // get default settings from string.xml
        btAddress = (String) getResources().getText(R.string.default_bluetooth);
        wifiServer = (String) getResources().getText(R.string.default_WiFiServer);
        wifiServerPort = (String) getResources().getText(
                R.string.default_WiFiServerPort);
        raceStartServerPort = (String) getResources().getText(
                R.string.default_RaceServerPort);
        centerPosition = (String) getResources().getText(
                R.string.default_CenterPosition);
        centerPositionInt = Integer.parseInt(centerPosition);
        centerRange = (String) getResources().getText(
                R.string.default_CenterRange);

        // load the saved settings
        loadPref();

        // create a new server to wait for go signal from wifi server
        // need to check first that this is numeric
        raceStartServer = new RaceStartServer(this,
                Integer.parseInt(raceStartServerPort));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btObject = new cBluetooth(this, mHandler);
        btObject.checkBTState();

        btnForward = (Button) findViewById(R.id.forward);

        btnForward.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btObject.sendData(msgForward);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btObject.sendData(msgStopForward);
                }
                return false;
            }
        });

        btnBackward = (Button) findViewById(R.id.backward);

        btnBackward.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btObject.sendData(msgBackward);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btObject.sendData(msgStopBackward);
                }
                return false;
            }
        });

    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            class MyClientTask extends AsyncTask<Void, Void, Void> {

                String socketServerAddress;
                int socketServerAddressPort;

                // String response = "";

                MyClientTask(String addr, int port) {
                    socketServerAddress = addr;
                    socketServerAddressPort = port;
                }

                @Override
                protected Void doInBackground(Void... arg0) {

                    Socket socket = null;

                    try {
                        socket = new Socket(socketServerAddress,
                                socketServerAddressPort);

                        DataInputStream din = new DataInputStream(
                                socket.getInputStream());
                        DataOutputStream dout = new DataOutputStream(
                                socket.getOutputStream());

                        // dout.writeUTF("hello from client");
                        dout.writeUTF("#");

                        // response = "sent to server";

                        // String msg = din.readUTF();
                        // response += msg;

                        socket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        // response = e.toString();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                }
            }
            switch (msg.what) {
                case cBluetooth.BL_NOT_AVAILABLE:
                    Log.d(cBluetooth.TAG, "Bluetooth is not available. Exit");
                    Toast.makeText(getBaseContext(), "Bluetooth is not available",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case cBluetooth.BL_INCORRECT_ADDRESS:
                    Log.d(cBluetooth.TAG, "Incorrect MAC address");
                    Toast.makeText(getBaseContext(), "Incorrect Bluetooth address",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case cBluetooth.BL_REQUEST_ENABLE:
                    Log.d(cBluetooth.TAG, "Request Bluetooth Enable");
                    BluetoothAdapter.getDefaultAdapter();
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                    break;
                case cBluetooth.BL_SOCKET_FAILED:
                    Toast.makeText(getBaseContext(), "Socket failed",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case cBluetooth.RECIEVE_MESSAGE:
                    Log.d(cBluetooth.TAG, "Bluetooth Message Received");
                    if (msg.obj != null) {
                        byte[] readBuf = (byte[]) msg.obj;
                        String strMsgFromCar = new String(readBuf, 0, msg.arg1);

                        // received a message when crossing a magnet
                        if (strMsgFromCar != null
                                && strMsgFromCar.equalsIgnoreCase("&")) {
                            TextView msgFromCar = (TextView) findViewById(R.id.textViewCmdReceive);
                            // textTimer.setText(sb.toString() + "->test");
                            msgFromCar.setText("< " + strMsgFromCar + " >");

                            // let garbage collector do its task
                            // i want new connection everytime, so to avoid being
                            // disconnected

                            // values should have been gotten from settings
//                            wifiServer = (String) getResources().getText(
//                                    R.string.default_WiFiServer);
//                            wifiServerPort = (String) getResources().getText(
//                                    R.string.default_WiFiServerPort);

                            MyClientTask myClientTask = new MyClientTask(
                                    wifiServer, Integer.parseInt(wifiServerPort));
                            myClientTask.execute();
                        }

                    }
                    break;
            }
        }
    };

    public void onSensorChanged(SensorEvent e) {
        // middle value, mean go straight
//        turnMsg = bltSend30;

        // e.values[1] is -10.0 up to 10.0
        // make it -100.0 up to 100.0
        tilt = (e.values[1] * 10);
        // limit it to -60.0 up to 60.0
        if (tilt > 60)
            tilt = 60;
        if (tilt < -60)
            tilt = -60;
        // set value from -30 to 30. remove the decimal by rounding off
        tilt = Math.round(tilt / 2);
        // set value from 0 - 60
        tilt = tilt + 30;

        tilt = tilt + centerPositionInt;

        // check if tilt was within center range that was set in preference
        tilt = checkAndSetCenterRange(tilt, centerPositionInt);

        // sending to arduino is delayed so, not turning to right
        // i'm wrong! it's not delayed, it's just need to have values more than 60 degrees
        //tilt = setTiltByCenterPref(tilt);

        if (tilt == 0)
            turnMsg = bltSend00;
        else if (tilt == -1)
            turnMsg = bltSend00;
        else if (tilt == -2)
            turnMsg = bltSend00;
        else if (tilt == -3)
            turnMsg = bltSend00;
        else if (tilt == -4)
            turnMsg = bltSend00;
        else if (tilt == -5)
            turnMsg = bltSend00;
        else if (tilt == 1)
            turnMsg = bltSend01;
        else if (tilt == 2)
            turnMsg = bltSend02;
        else if (tilt == 3)
            turnMsg = bltSend03;
        else if (tilt == 4)
            turnMsg = bltSend04;
        else if (tilt == 5)
            turnMsg = bltSend05;
        else if (tilt == 6)
            turnMsg = bltSend06;
        else if (tilt == 7)
            turnMsg = bltSend07;
        else if (tilt == 8)
            turnMsg = bltSend08;
        else if (tilt == 9)
            turnMsg = bltSend09;
        else if (tilt == 10)
            turnMsg = bltSend10;
        else if (tilt == 11)
            turnMsg = bltSend11;
        else if (tilt == 12)
            turnMsg = bltSend12;
        else if (tilt == 13)
            turnMsg = bltSend13;
        else if (tilt == 14)
            turnMsg = bltSend14;
        else if (tilt == 15)
            turnMsg = bltSend15;
        else if (tilt == 16)
            turnMsg = bltSend16;
        else if (tilt == 17)
            turnMsg = bltSend17;
        else if (tilt == 18)
            turnMsg = bltSend18;
        else if (tilt == 19)
            turnMsg = bltSend19;
        else if (tilt == 20)
            turnMsg = bltSend20;
        else if (tilt == 21)
            turnMsg = bltSend21;
        else if (tilt == 22)
            turnMsg = bltSend22;
        else if (tilt == 23)
            turnMsg = bltSend23;
        else if (tilt == 24)
            turnMsg = bltSend24;
        else if (tilt == 25)
            turnMsg = bltSend25;
        else if (tilt == 26)
            turnMsg = bltSend26;
        else if (tilt == 27)
            turnMsg = bltSend27;
        else if (tilt == 28)
            turnMsg = bltSend28;
        else if (tilt == 29)
            turnMsg = bltSend29;
        else if (tilt == 30)
            turnMsg = bltSend30;
        else if (tilt == 31)
            turnMsg = bltSend31;
        else if (tilt == 32)
            turnMsg = bltSend32;
        else if (tilt == 33)
            turnMsg = bltSend33;
        else if (tilt == 34)
            turnMsg = bltSend34;
        else if (tilt == 35)
            turnMsg = bltSend35;
        else if (tilt == 36)
            turnMsg = bltSend36;
        else if (tilt == 37)
            turnMsg = bltSend37;
        else if (tilt == 38)
            turnMsg = bltSend38;
        else if (tilt == 39)
            turnMsg = bltSend39;
        else if (tilt == 40)
            turnMsg = bltSend40;
        else if (tilt == 41)
            turnMsg = bltSend41;
        else if (tilt == 42)
            turnMsg = bltSend42;
        else if (tilt == 43)
            turnMsg = bltSend43;
        else if (tilt == 44)
            turnMsg = bltSend44;
        else if (tilt == 45)
            turnMsg = bltSend45;
        else if (tilt == 46)
            turnMsg = bltSend46;
        else if (tilt == 47)
            turnMsg = bltSend47;
        else if (tilt == 48)
            turnMsg = bltSend48;
        else if (tilt == 49)
            turnMsg = bltSend49;
        else if (tilt == 50)
            turnMsg = bltSend50;
        else if (tilt == 51)
            turnMsg = bltSend51;
        else if (tilt == 52)
            turnMsg = bltSend52;
        else if (tilt == 53)
            turnMsg = bltSend53;
        else if (tilt == 54)
            turnMsg = bltSend54;
        else if (tilt == 55)
            turnMsg = bltSend55;
        else if (tilt == 56)
            turnMsg = bltSend56;
        else if (tilt == 57)
            turnMsg = bltSend57;
        else if (tilt == 58)
            turnMsg = bltSend58;
        else if (tilt == 59)
            turnMsg = bltSend59;
        else if (tilt == 60)
            turnMsg = bltSend60;
        else if (tilt == 61)
            turnMsg = bltSend60;
        else if (tilt == 62)
            turnMsg = bltSend60;
        else if (tilt == 63)
            turnMsg = bltSend60;
        else if (tilt == 64)
            turnMsg = bltSend60;
        else if (tilt == 65)
            turnMsg = bltSend60;

        // send the single character data
        btObject.sendData(turnMsg);

        // show value if in debug mode
        TextView textCmdSend = (TextView) findViewById(R.id.textViewCmdSend);

        if (showDebug) {
            textCmdSend.setText(String.valueOf("Send:" + turnMsg));
        } else {
            textCmdSend.setText("");
        }

    }

    /**
     * check center range if it falls under the set range
     *
     * @param tilt
     * @param centerPositionInt
     * @return the new tilt value if it was covered by center position and range
     *
     */
    private float checkAndSetCenterRange(float tilt, int centerPositionInt) {
        // assume was check already checked in PrefsFragement class to be 0-10 integer
        int centerRangeInt = Integer.parseInt(centerRange);

        if (centerRangeInt == 0)
            return tilt;

        float retVal = 0;

        // center default is 30 degrees then add the set adjustment via centerpoistion (-5 to +5)
        // this is the new center
        int centerPoint = 30 + centerPositionInt;

        // check if tilt is within range
        if ((tilt > (centerPoint - centerRangeInt)) && (tilt < (centerPoint + centerRangeInt)))
            return (float)centerPoint;
        else
            return tilt;
    }

    /**
     * changed the center at 30 degrees by adding or subtracting
     * the negative value of pref (5 to -5)
     */
    private float setTiltByCenterPref(float tilt) {
        float returnTilt = tilt;

//        returnTilt = returnTilt + Float.parseFloat(centerPosition);

        return returnTilt;
    }

    private void loadPref() {
        SharedPreferences mySharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        // getstring (key , default)
        btAddress = mySharedPreferences
                .getString("pref_MAC_address", btAddress);
        wifiServer = mySharedPreferences.getString("pref_WiFiServer_address",
                wifiServer);
        wifiServerPort = mySharedPreferences.getString("pref_WiFiServer_port",
                wifiServerPort);
        raceStartServerPort = mySharedPreferences.getString(
                "pref_RaceServer_port", raceStartServerPort);
        showDebug = mySharedPreferences.getBoolean("pref_Debug", false);
        centerPosition = mySharedPreferences.getString("pref_CenterPosition", centerPosition);
        centerPositionInt = Integer.parseInt(centerPosition);
        centerRange = mySharedPreferences.getString("pref_CenterRange", centerRange);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btObject.BT_Connect(btAddress, true);

        mSensorManager.registerListener(this, mAccel,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        btObject.BT_onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadPref();
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

}
