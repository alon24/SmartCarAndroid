//package alon24.smartcarandroid;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class WiFiDemo extends Activity implements View.OnClickListener
// {
//    WifiManager wifi;
//    ListView lv;
//    TextView textStatus;
//    Button buttonScan;
//    int size = 0;
//    List<ScanResult> results;
//
//    String ITEM_KEY = "key";
//    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
//    SimpleAdapter adapter;
//
//    /* Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        textStatus = (TextView) findViewById(R.id.textStatus);
//        buttonScan = (Button) findViewById(R.id.buttonScan);
//        buttonScan.setOnClickListener(this);
//        lv = (ListView)findViewById(R.id.list);
//
//        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        if (wifi.isWifiEnabled() == false)
//        {
//            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
//            wifi.setWifiEnabled(true);
//        }
//        this.adapter = new SimpleAdapter(WiFiDemo.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
//        lv.setAdapter(this.adapter);
//
//        registerReceiver(new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context c, Intent intent)
//            {
//               results = wifi.getScanResults();
//               size = results.size();
//            }
//        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//    }
//
//    public void onClick(View view)
//    {
//        arraylist.clear();
//        wifi.startScan();
//
//        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
//        try
//        {
//            size = size - 1;
//            while (size >= 0)
//            {
//                HashMap<String, String> item = new HashMap<String, String>();
//                item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);
//
//                arraylist.add(item);
//                size--;
//                adapter.notifyDataSetChanged();
//            }
//        }
//        catch (Exception e)
//        { }
//    }
//}