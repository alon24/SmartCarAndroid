package alon24.smartcarandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import alon24.smartcarandroid.utils.RepeatListener;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    private static String TAG = "SmartCar";
//    private WebSocketClient mWebSocketClient;
    WifiManager wifi;
    String wifis[];

    int size = 0;
    List<ScanResult> results;

    WifiScanReceiver wifiReciever;
//    String smartCarIp = "192.168.4.1";
    SharedPreferences sharedPref;
    String AP_SSID = "SmartCar";

    private Switch connectModeSwitch;
    private Button connectBtn;
    private EditText carIpText;

    private boolean isConnected = false;
    private boolean isConnecting = false;

    RepeatListener repeatListener;
    int curretYPower = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "מצא מכונית והתחבר", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        connectModeSwitch = (Switch) findViewById(R.id.connectModeSwitch);
        connectBtn = (Button)findViewById(R.id.connectStateBtn);
        carIpText = (EditText)findViewById(R.id.carIpText);
        carIpText.setText(readData("carIp"));
        carIpText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                saveData("carIp", s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
//                TextView myOutputBox = (TextView) findViewById(R.id.myOutputBox);
//                myOutputBox.setText(s);
            }
        });

        connectBtn.setOnClickListener(this);
        connectModeSwitch.setOnCheckedChangeListener(this);
        repeatListener = new RepeatListener(0,100, this);
//        carIpText.addTextChangedListener((this);
//        ((Button) findViewById(R.id.upBtn)).setOnClickListener(this);
        ((Button) findViewById(R.id.upBtn)).setOnTouchListener(repeatListener);

        ((Button)findViewById(R.id.downBtn)).setOnTouchListener(repeatListener);
        ((Button)findViewById(R.id.leftBtn)).setOnTouchListener(repeatListener);
        ((Button)findViewById(R.id.rightBtn)).setOnTouchListener(repeatListener);

        ((Button)findViewById(R.id.upLeftBtn)).setOnTouchListener(repeatListener);
        ((Button)findViewById(R.id.upRightBtn)).setOnTouchListener(repeatListener);
        ((Button)findViewById(R.id.downLeftBtn)).setOnTouchListener(repeatListener);
        ((Button)findViewById(R.id.downRightBtn)).setOnTouchListener(repeatListener);
        ((Button)findViewById(R.id.stopBtn)).setOnTouchListener(repeatListener);
        SeekBar seekBar = ((SeekBar)findViewById(R.id.seekBar));
        seekBar.setMax(100);
        seekBar.setProgress(curretYPower);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                curretYPower = progress;
                Log.d(TAG, "current power = " + curretYPower);
//                int MIN = 5;
//                if (progress < MIN) {
//
//                    value.setText(" Time Interval (" + seektime + " sec)");
//                } else {
//                    seektime = progress;
//                }
//                value.setText(" Time Interval (" + seektime + " sec)");

            }
        });
//        Menu m = navigationView.getMenu();
//        SubMenu wifiConnectionsMenu = m.addSubMenu("WifiConnections");
//        wifiConnectionsMenu.add("Foo");
//        wifiConnectionsMenu.add("Bar");
//        wifiConnectionsMenu.add("Baz");
//
//        MenuItem mi = m.getItem(m.size()-1);
//        mi.setTitle("testing123");
//        mi.setTitle(mi.getTitle());

//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mEthernet = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
//        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (mWifi!=null) isOnWifi = mWifi.isConnected();
//        if (mEthernet!=null) isOnEthernet = mEthernet.isConnected();
//        if (m3G!=null) is3G = m3G.isConnected();

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled, you will not be able to connect to the car", Toast.LENGTH_LONG).show();
//            wifi.setWifiEnabled(true);
        }
        else {
            startLooking();
        }
//        connectToCar();
        updateUI();
//        registerReceiver(new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context c, Intent intent)
//            {
//               results = wifi.getScanResults();
//               size = results.size();
////                wifi.conn
//            }
//        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//
//        if (wifi.isWifiEnabled()) {
//            wifi.startScan();
//        }
//        connectWebSocket();

    }

    private void saveData(String key, String val){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.commit();
    }

    private String readData(String key) {
        String defaultValue = getResources().getString(R.string.default_ip_address);
        return sharedPref.getString(key, defaultValue);
    }

    public void startLooking() {
        //first get the wifi networks list
        //search for car wifif (ap)
        // see if have connection data stored
        // if on same network connect to car
        //update screen
    }

    private final WebSocketConnection mConnection = new WebSocketConnection();

    private void stopTryingToConnect() {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    private void connectToCar() {

        String smartCarIp = carIpText.getText().toString();
        final String wsuri = "ws://" + smartCarIp + ":80/index.html?command=true";
//        final String wsuri = "ws://192.168.1.132:9000";

        try {
            Log.d(TAG, "Trying to websocket connect to " + wsuri);
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);
                    mConnection.sendTextMessage("Hello, world!");
                    isConnected = true;
                    isConnecting = false;
                    updateUI();
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");
                    Toast.makeText(getApplicationContext(), "Not connected to car", Toast.LENGTH_LONG).show();
                    isConnecting = false;
                    isConnected = false;
                    updateUI();
                }
            });
        } catch (WebSocketException e) {

            Log.d(TAG, e.toString());
            isConnecting = false;
            isConnected = false;
            Toast.makeText(getApplicationContext(), "Could not connect to car", Toast.LENGTH_LONG).show();
            updateUI();
        }
    }

//    private void connectWebSocket() {
//        URI uri;
//        try {
////            uri = new URI("ws://10.100.102.149:8080/index.html?command=true");
//            uri = new URI("ws://10.100.102.149:8080");
////            uri = new URI("ws://echo.websocket.org");
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        mWebSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                Log.i("Websocket", "Opened");
//                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
//            }
//
//            @Override
//            public void onMessage(String s) {
////                final String message = s;
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        TextView textView = (TextView)findViewById(R.id.messages);
////                        textView.setText(textView.getText() + "\n" + message);
////                    }
////                });
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                Log.i("Websocket", "Closed " + s);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                isConnected = false;
//                updateUI();
//                Log.i("Websocket", "Error " + e.getMessage());
//            }
//        };
//        WebSocketImpl.DEBUG = true;
//        mWebSocketClient.connect();
//    }

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!connectModeSwitch.isChecked()) {
                    connectModeSwitch.setText("מכובה");
                    connectBtn.setEnabled(false);
                    connectBtn.setText("מנותק!");
                    connectBtn.setEnabled(false);
                    if (mConnection != null && mConnection.isConnected()) {
                        mConnection.disconnect();
                    }
                }
                else {
                    connectModeSwitch.setText("מאופשר");
                    connectBtn.setEnabled(true);
                    if (isConnecting) {
                        connectBtn.setText("trying to connect!");
                        connectBtn.setEnabled(true);
                    } else if (isConnected) {
                        connectBtn.setText("מחובר!");
//                        connectBtn.setEnabled(true);
                    } else {
                        connectBtn.setText("התחבר");
//                        connectBtn.setEnabled(false);
                    }
                }
            }
        });
    }


    private void sendMessage(String msg) {
        if (mConnection == null || !mConnection.isConnected()) {
            return;
        }
        mConnection.sendTextMessage(msg);
//        mWebSocketClient.send(msg);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void onPause() {
        if (wifiReciever != null){
            unregisterReceiver(wifiReciever);
        }
        super.onPause();
    }


    protected void onResume() {

//        if (wifi.isWifiEnabled() == false)
//        {
//            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//            wifi.setWifiEnabled(true);
//        }
//
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//
        if (wifi.isWifiEnabled()) {
            connectModeSwitch.setChecked(true);
            wifi.startScan();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the camera action
            Intent i = new Intent(this, CarSettings.class);
            startActivity(i);
        } else if (id == R.id.nav_conenctToCar) {
            connectToCar();
//            connectWebSocket();
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectStateBtn:
                if (isConnected) {
                    connectBtn.setText("לא מחובר");
                    isConnecting = false;
                    isConnected = false;
                }
                else if (isConnecting) {
                    stopTryingToConnect();
                    isConnecting = false;
                    updateUI();
                }
                else {
//                    connectBtn.setText("מתחבר!");
                    connectToCar();
////                    connectWebSocket();
                    isConnecting = true;
                    updateUI();
                }
                break;
            //Move x%,y%
            case R.id.upBtn:
//                Log.d(TAG, "upbtn rpt listener");
                sendMoveXYMessage(0,100);
                break;
            case R.id.downBtn:
                sendMoveXYMessage( 0,-100);
                break;
            case R.id.leftBtn:
                sendMoveXYMessage(-100,0);
                break;
            case R.id.rightBtn:
                sendMoveXYMessage(100,0);
                break;
            case R.id.upLeftBtn:
                sendMoveXYMessage( -100,100);
                break;
            case R.id.upRightBtn:
                sendMoveXYMessage(100,100);
                break;
            case R.id.downRightBtn:
                sendMoveXYMessage(-100,-100);
                break;
            case R.id.downLeftBtn:
                sendMoveXYMessage(100,-100);
                break;
            case R.id.stopBtn:
                sendMoveXYMessage(0,0);
                break;
        }
    }

    private void sendMoveXYMessage(int x, int y) {
        sendMessage("Move xy " + x + " " + curretYPower);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    Log.d(TAG, carIpText.getText().toString());
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++) {

//                item = wifiScanList.get(i);
                wifis[i] = ((wifiScanList.get(i)).toString());
                Log.d(TAG, "SSID " + wifis[i]);
                if (wifis[i].equals(AP_SSID)) {
                    Log.d(TAG, "SSID " + wifis[i]);
                    WifiConfiguration config = new WifiConfiguration();
                    config.SSID = "\"" + AP_SSID + "\"";
                    config.preSharedKey = "\"" + "\"";
                    int netId = wifi.addNetwork(config);
                    wifi.saveConfiguration();
                    wifi.disconnect();
                    wifi.enableNetwork(netId, true);
                    wifi.reconnect();
////                    WifiManager.
//                }
                }
//            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
            }
        }
    }
}
