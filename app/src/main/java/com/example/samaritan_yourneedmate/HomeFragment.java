package com.example.samaritan_yourneedmate;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import java.security.PrivateKey;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
  CardView cd1,cd2,cd3,cd4,cd5,cd6;
String name;

    private static final int REQUEST_PHONE_CALL=1;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_home, container, false);
        cd1=view.findViewById(R.id.card_sms);
        cd2=view.findViewById(R.id.card_whatsapp);
        cd3=view.findViewById(R.id.card_call);
        cd4=view.findViewById(R.id.card_locateme);
        cd5=view.findViewById(R.id.card_policest);
        cd6=view.findViewById(R.id.card_hospital);
        //send sms alert
        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent in =new Intent(getActivity(),MainActivity.class);
              startActivity(in);
            }
        });

        //send whatsapp alert
        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getActivity(),MainActivity2.class);
               startActivity(intent);
            }
        });

        //emergency call
        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      phonecall(name);
            }
        });

        cd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ref=Uri.parse("geo:O,O?q=CURRENT LOCATION");
                Intent i= new Intent(Intent.ACTION_VIEW,ref);
                i.setPackage("com.google.android.apps.maps");
                startActivity(i);
            }
        });


        cd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Uri ref=Uri.parse("geo:O,O?q=POLICE STATIONS");
             Intent i= new Intent(Intent.ACTION_VIEW,ref);
             i.setPackage("com.google.android.apps.maps");
             startActivity(i);
            }
        });
        cd6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ref=Uri.parse("geo:O,O?q=HOSPITALS");
                Intent i= new Intent(Intent.ACTION_VIEW,ref);
                i.setPackage("com.google.android.apps.maps");
                startActivity(i);
            }
        });


        return view;
    }

    private boolean appinstalled(String s) {
        PackageManager packageManager=getActivity().getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            app_installed=true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed=false;
        }return app_installed;
    }
    public boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(getActivity(),permission);
        return (check==PackageManager.PERMISSION_GRANTED);
    }

    private void phonecall(String name) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }else
        {
            final EditText getno= new EditText(getContext());
            final AlertDialog.Builder calldialog=new AlertDialog.Builder(getContext());
            calldialog.setTitle("Send Alert SMS?");
            calldialog.setMessage("Enter the Mobile number to make a call to:");
            calldialog.setCancelable(true);
            calldialog.setView(getno);
            calldialog.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String phno= getno.getText().toString().trim();
                    Intent in=new Intent(Intent.ACTION_CALL);
                    in.setData(Uri.parse("tel:"+phno));
                    startActivity(in);
                }
            });
            calldialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });calldialog.create().show();
        }
    }






}