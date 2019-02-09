package codetivelab.bluetoothaurdino;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String NewName;
    Boolean removedialoguedone=false,socet_connected=false;
    public String name;
    public static final String Name = "nameKey";
    public static final String name2 = "nameKey2";
    public static final String name3 = "nameKey3";
    public static final String name4 = "nameKey4";
    public static final String name5 = "nameKey5";
    public static final String name6 = "nameKey6";
    public static final String name7 = "nameKey7";
    private Boolean onAdShown=false;
    SharedPreferences sharedpreferences;
    private String m_Text ="";
    Switch Light1,Light2,Light3,Light4,Light5,Light6,Light7;
    TextView Switch1Text,Switch2Text,Switch3Text,Switch4Text,Switch5Text,Switch6Text,Switch7Text;
    BluetoothAdapter mBluetoothAdapter;
    private Boolean onScanBtnClicked;
    AlertDialog dialogue;
    AlertDialog.Builder builder;
    ArrayAdapter<String>DevicesListArrayAdapter;
    ListView DevicesList;
    String TAG;
    private CountDownTimer countDownTimer;
    Boolean onCreateCalled;
    ProgressDialog progressDialog;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        socet_connected=false;
        setContentView(R.layout.activity_main);
        Navigation_activity_code();
        onScanBtnClicked=false;
        get_textview_ids();
        onCreateCalled=true;
        Changing_names_TextViews();
        get_switch_ids();
        Setting_checked_state();
        MobileAds.initialize(this,
                "");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mAdView = (AdView) findViewById(R.id.adView);

        if(onCreateCalled=true) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish();
            }
            DevicesList = new ListView(this);
            SetListeners();
            paireddevices();
        }

    }

    private void Setting_checked_state() {
        Light1.setChecked(false);
        Light2.setChecked(false);
        Light4.setChecked(false);
        Light5.setChecked(false);
        Light6.setChecked(false);
        Light7.setChecked(false);
    }

    private void Navigation_activity_code() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.rate_this) {
            try {
                Uri mUri = Uri.parse("market://drtails?id=" + getPackageName());
                Intent mintent = new Intent(Intent.ACTION_VIEW, mUri);
                startActivity(mintent);
            }catch (ActivityNotFoundException e)
            {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
            }

        } else
        if (id == R.id.more_apps) {
            more();
        }else if(id==R.id.like_us){
            likeUs();
        }else if(id==R.id.dev_ref) {
           conactDev();
        }else if(id==R.id.privacy_policy){
            privacyPolicy();
        }
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void get_switch_ids() {
        Light1=(Switch)findViewById(R.id.Light1);
        Light2=(Switch)findViewById(R.id.Light2);
        Light4=(Switch)findViewById(R.id.Light4);
        Light5=(Switch)findViewById(R.id.Light5);
        Light6=(Switch)findViewById(R.id.Light6);
        Light7=(Switch)findViewById(R.id.Light7);
    }

    private void Changing_names_TextViews() {
        String Text1=checknames(Name);
        String Text2=checknames(name2);
        String Text3=checknames(name3);
        String Text4=checknames(name4);
        String Text5=checknames(name5);
        String Text6=checknames(name6);
        String Text7=checknames(name7);
        if(Text1!=null)
            Switch1Text.setText(Text1);
        if(Text2!=null)
            Switch2Text.setText(Text2);
        if(Text3!=null) {
            Switch3Text.setText(Text3);
        } if(Text4!=null) {
            Switch4Text.setText(Text4);
        }if(Text5!=null) {
            Switch5Text.setText(Text5);
        }
        if(Text6!=null) {
            Switch6Text.setText(Text6);
        }if(Text7!=null) {
            Switch7Text.setText(Text7);
        }
    }

    private void get_textview_ids() {
        Switch1Text = (TextView) findViewById(R.id.Switch1);
        Switch2Text = (TextView) findViewById(R.id.Switch2);
        Switch4Text=(TextView)findViewById(R.id.Switch4);
        Switch5Text=(TextView)findViewById(R.id.Switch5);
        Switch6Text=(TextView)findViewById(R.id.Switch6);
        Switch7Text=(TextView)findViewById(R.id.Switch7);
    }

    public String checknames(String key) {
        sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        NewName = sharedpreferences.getString(key,name);
        return  NewName;
    }
    private void handler() {

            mHandler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(android.os.Message msg) {

                    if (msg.what == MESSAGE_READ) {
                        String readMessage = null;
                        try {
                            readMessage = new String((byte[]) msg.obj, "UTF-8");
                            if(readMessage=="z")
                            {
                                Light1.setChecked(true);
                            }
                            if(readMessage=="y")
                            {
                                Light2.setChecked(true);
                            }
                            if(readMessage=="x")
                            {
                                Light3.setChecked(true);
                            }
                            if(readMessage=="w")
                            {
                                Light4.setChecked(true);
                            }
                            if(readMessage=="v")
                            {
                                Light5.setChecked(true);
                            }
                            if(readMessage=="u")
                            {
                                Light6.setChecked(true);
                            }
                            if(readMessage=="t")
                            {
                                Light3.setChecked(true);
                            }
                                                    } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    if (msg.what == CONNECTING_STATUS) {
                        if (msg.arg1 == 1) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startCountDownTimer(15000,500);
                                    Toast.makeText(MainActivity.this,"Connected to device", Toast.LENGTH_SHORT);

                                }
                            });
                            removedialoguedone=true;
                            socet_connected=true;
                            onScanBtnClicked=false;
                            DevicesListArrayAdapter.clear();
                            DevicesList.setAdapter(null);
                            try {
                                if(dialogue!=null) {
                                    dialogue.dismiss();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            if(progressDialog!=null){

                                progressDialog.cancel();
                            }
                            if(dialogue!=null) {
                                dialogue.show();
                                finish();
                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"Socet creation failed", Toast.LENGTH_SHORT);

                                }
                            });

                            }
                    }
                }
            };

    }

    private void paireddevices() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        final ArrayList<String> devices = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                devices.add(device.getName() + "\n" + device.getAddress());
            }
            Log.e(TAG, "Paired devices bringing");
            DevicesListArrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, devices);
            DevicesList.setAdapter(DevicesListArrayAdapter);
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(DevicesList);

            builder.setPositiveButton(R.string.Scan, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent scanintent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(scanintent);
                    onScanBtnClicked=true;
                }
            });
            dialogue = builder.create();
            dialogue.setCanceledOnTouchOutside(false);
            dialogue.setTitle("Paired Devices List");
            dialogue.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if(i==keyEvent.KEYCODE_BACK &&!keyEvent.isCanceled())
                    {
                        if(dialogue.isShowing()){
                            dialogue.dismiss();
                            finish();
                        }
                    }
                    return false;
                }
            });
            dialogue.show();
            }
        else
        {
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("No Paired Device found Press Scan Button to scan");
            builder.setTitle("Alert");
            builder.setPositiveButton(R.string.Scan, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent scanintent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(scanintent);

                }
            });
            dialogue = builder.create();
            dialogue = builder.create();
            dialogue.setCanceledOnTouchOutside(false);
            dialogue.setTitle("Paired Devices List");
            dialogue.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if(i==keyEvent.KEYCODE_BACK &&!keyEvent.isCanceled())
                    {
                        if(dialogue.isShowing()){
                            dialogue.dismiss();
                            finish();
                        }
                    }
                    return false;
                }
            });
            dialogue.show();

        }
        }






    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Loading");
//            progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                    if(i==keyEvent.KEYCODE_BACK&&!keyEvent.isCanceled()){
//                        if(progressDialog.isShowing()){
//                            progressDialog.dismiss();
//                            progressDialog.show();
//                        }
//                    }
//                    return false;
//                }
//            });
            if(progressDialog!=null) {
                progressDialog.show();
            }

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);
            // Spawn a new thread to avoid blocking the GUI one

            new Thread()
            {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                     }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"Socket creation failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"Socket creation failed", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                    if(fail == false) {
                        progressDialog.dismiss();
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID

    }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            // Keep listening to the InputStream until an exception occurs
            Thread thread = new Thread(new Runnable() {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()
                @Override
                public void run() {

                    while (true) {

                        try {
                            // Read from the InputStream
                            bytes = mmInStream.available();
                            if (bytes != 0) {
                                //                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                                bytes = mmInStream.available(); // how many bytes are ready to be read?
                                bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                        .sendToTarget(); // Send the obtained bytes to the UI activity
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                            break;
                        }
                    }
                }
            });thread.start();

        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }

        }
    }
    private void startCountDownTimer(long duration, long interval){
        countDownTimer = new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
            long tick=millisUntilFinished;
            }
            @Override
            public void onFinish() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    onAdShown=true;
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        };
        countDownTimer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        onCreateCalled=false;
        if(dialogue!=null){
            dialogue.dismiss();
            dialogue=null;
        }
        if(mConnectedThread!=null&&onAdShown==false) {
            mConnectedThread.cancel();
            if(mBTSocket.isConnected()){
            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.
                    }
                });
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        mAdView.setVisibility(View.VISIBLE);// Code to be executed when an ad finishes loading.
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        mAdView.setVisibility(View.GONE);
                        mAdView.loadAd(new AdRequest.Builder().build());
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.

                        mAdView.setVisibility(View.GONE);
                        mAdView.loadAd(new AdRequest.Builder().build());
                    }
                });

            }
        });
        if(!onAdShown){
            if (onCreateCalled == false) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    startActivity(new Intent(MainActivity.this, SplashActivity.class));
                    finish();
                }
                DevicesList = new ListView(this);
                SetListeners();

                paireddevices();
            }
            DevicesList.setOnItemClickListener(mDeviceClickListener);
            handler();
            //        Switch onOffSwitch = (Switch)  findViewById(R.id.on_off_switch);
            Light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        if (mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write("Z");
                    } else if (mConnectedThread != null)
                        mConnectedThread.write("z");
                }

            });
            Light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        if (mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write("Y");
                    } else if (mConnectedThread != null)
                        mConnectedThread.write("y");
                }

            });

            Light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        if (mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write("W");
                    } else if (mConnectedThread != null)
                        mConnectedThread.write("w");
                }

            });
            Light5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        if (mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write("V");
                    } else if (mConnectedThread != null)
                        mConnectedThread.write("v");
                }


            });
            Light6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        if (mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write("U");
                    } else if (mConnectedThread != null)
                        mConnectedThread.write("u");
                }

            });

            Light7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.v("Switch State=", "" + isChecked);
                    if (isChecked) {
                        if (mConnectedThread != null) //First check to make sure thread created
                            mConnectedThread.write("T");
                    } else if (mConnectedThread != null)
                        mConnectedThread.write("t");
                }

            });

        }
        onAdShown=false;



    }

    private void SetListeners() {

        Switch1Text.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
//                  builder.setView(dialogue)
//                   Set up the input
                final EditText input = new EditText(MainActivity.this);
//                   Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

//                  Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        Switch1Text.setText(m_Text);
                        saveName(Name,m_Text);
                        //                    saveDataToPreferences(context,name,m_Text);
                        Toast.makeText(MainActivity.this,"Saved Successfully", Toast.LENGTH_SHORT);
                           }

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return false;
            }
        });
        Switch2Text.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                // builder.setView(dialogue)
                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        Switch2Text.setText(m_Text);
                        saveName(name2,m_Text);
                        //saveDataToPreferences(context,name2,m_Text);
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return false;
            }
        });

        Switch4Text.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                //builder.setView(dialogue)
                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        Switch4Text.setText(m_Text);
                        saveName(name4,m_Text);
                        // saveDataToPreferences(context,name2,m_Text);
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
        Switch5Text.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                //builder.setView(dialogue)
                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Switch5Text.setText(m_Text);
                        saveName(name5,m_Text);
                        // saveDataToPreferences(context,name2,m_Text);
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
        Switch6Text.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                //builder.setView(dialogue)
                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Switch6Text.setText(m_Text);
                        saveName(name6,m_Text);
                        // saveDataToPreferences(context,name2,m_Text);
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
        Switch7Text.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");
                //builder.setView(dialogue)
                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Switch7Text.setText(m_Text);
                        saveName(name7,m_Text);
                        // saveDataToPreferences(context,name2,m_Text);
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return false;
            }
        });
    }
    //Saving name through prefrences
    public void saveName(String Key,String n){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Key,n);
        editor.commit();
        Toast.makeText(MainActivity.this,"Saved Successfully", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!onScanBtnClicked) {
            finish();
            if(dialogue!=null){
                dialogue.dismiss();
                dialogue=null;
            }

            if(progressDialog!=null){
                progressDialog.dismiss();
                progressDialog=null;
            }

            try
            {
               mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
               if(mBluetoothAdapter.isEnabled()){
                   mBluetoothAdapter.disable();
               }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
//        else{
//            mBluetoothAdapter.disable();
//
//            finish();
//        }
        onAdShown = false;
    }


        private void disabling_listeners() {

            Light1.setOnCheckedChangeListener(null);
            Light2.setOnCheckedChangeListener(null);
            Light4.setOnCheckedChangeListener(null);
            Light5.setOnCheckedChangeListener(null);
            Light6.setOnCheckedChangeListener(null);
            Light7.setOnCheckedChangeListener(null);
            Switch1Text.setOnLongClickListener(null);
            Switch2Text.setOnLongClickListener(null);
            Switch4Text.setOnLongClickListener(null);
            Switch5Text.setOnLongClickListener(null);
            Switch6Text.setOnLongClickListener(null);
            Switch7Text.setOnLongClickListener(null);
        }

    public Intent newFacebookIntent(String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = this.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    private void likeUs() {
        try {
            Intent intent = newFacebookIntent("https://www.facebook.com/Codetivelab-2065615053694754");
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void conactDev() {
        try {
            Intent intent = newFacebookIntent("https://www.facebook.com/waseem.mirza.789");
            startActivity(intent);
        } catch (Exception e) {
        }
    }
    private void more() {
        final Uri uri = Uri.parse("market://search?q=Codetivelab");
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
        if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
            startActivity(rateAppIntent);
        } else {
            Toast.makeText(this, "Your phone is not supported!", Toast.LENGTH_LONG).show();
        }
    }

    private void privacyPolicy() {
        Intent newIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://codetivelab.github.io/pricacy-policy-mob.github.io/BTAurdino.html"));
        startActivity(newIntent);
    }


}

