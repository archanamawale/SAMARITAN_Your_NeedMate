package com.example.samaritan_yourneedmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity2 extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public TextView textlatLong,textaddress;
    private ProgressBar pgbar;
    Button btnsendwhatspp;
    String name, getaddress;
    private static final int REQUEST_PHONE_MSG=1;
    String txtgetlocationtosend;
    private ResultReceiver resultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        resultReceiver=new MainActivity2.AddressResultReciever(new Handler());
        textlatLong = findViewById(R.id.txtlatLong);
        textaddress=findViewById(R.id.txtaddress);
        btnsendwhatspp=findViewById(R.id.btnsendwhtsapp);
        btnsendwhatspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whtsappmessage(name);
            }
        });

        pgbar = findViewById(R.id.pgbar);
        findViewById(R.id.btngetCurrentLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentlocation();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentlocation();
                whtsappmessage(name);
            } else {
                Toast.makeText(this, "Permission Denied!!!", Toast.LENGTH_SHORT).show();
            }

        }
    }



    private void getCurrentlocation() {
        pgbar.setVisibility(View.VISIBLE);

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity2.this).requestLocationUpdates(locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity2.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude=locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude=locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textlatLong.setText(String.format("Latitude:%s\nLongitude:%s",latitude,longitude));
                            txtgetlocationtosend=textlatLong.getText().toString().trim();

                            Location location=new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAdressfromlatLong(location);

                        }else{
                            pgbar.setVisibility(View.GONE);
                        }
                    }
                }, Looper.getMainLooper());

    }

    private void fetchAdressfromlatLong(Location location){
        Intent intent=new Intent(this,FetchAdressIntentService.class);
        intent.putExtra(Constants.RECIEVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private class AddressResultReciever extends ResultReceiver{
        public AddressResultReciever(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode== Constants.SUUCESS_RESULT){
                textaddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                getaddress=textaddress.getText().toString().trim();
            }else {
                Toast.makeText(MainActivity2.this,resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            pgbar.setVisibility(View.GONE);
        }
    }




    private void whtsappmessage(String name){
        if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.SEND_SMS},REQUEST_PHONE_MSG);
        }else
        {
            final EditText getno= new EditText(MainActivity2.this);
            final AlertDialog.Builder smsdialog=new AlertDialog.Builder(MainActivity2.this);
            smsdialog.setTitle("Send Whatsapp Alert?").setIcon(R.mipmap.whatsapp);
            smsdialog.setMessage("Enter the Mobile number to send Whatsapp alert:");
            smsdialog.setCancelable(false);
            smsdialog.setView(getno);
            smsdialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean installed=appinstalled("com.whatsapp");
                    if (installed){
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91" +getno.getText().toString().trim() +
                                "&text="+"🛑    🛑    🛑    🛑    🛑    🛑\nHey, I am in trouble. I need ur help. You could locate me from the latitude " +
                                "and longitude below\n"+txtgetlocationtosend+"\n"+"My Current location is:\n"+getaddress));
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"WHATSAPP NOT INSTALLED",Toast.LENGTH_LONG).show();
                    }
                }
            }); smsdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });smsdialog.create().show();

        }
    }
    private boolean appinstalled(String url){
        PackageManager packageManager=getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed=true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed=false;
        }return app_installed;
    }
    public boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return (check==PackageManager.PERMISSION_GRANTED);
    }


    public void hidekeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}