package com.example.samaritan_yourneedmate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import java.security.PrivateKey;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
  CardView cd1,cd2,cd3,cd4,cd5,cd6;
String name;
    private static final int REQUEST_PHONE_MSG=1;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        cd1=view.findViewById(R.id.card_sms);
        cd2=view.findViewById(R.id.card_whatsapp);
        cd3=view.findViewById(R.id.card_call);
        cd4=view.findViewById(R.id.card_locateme);
        cd5=view.findViewById(R.id.card_policest);
        cd6=view.findViewById(R.id.card_hospital);
        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              message(name);
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
        cd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ref=Uri.parse("geo:O,O?q=CURRENT LOCATION");
                Intent i= new Intent(Intent.ACTION_VIEW,ref);
                i.setPackage("com.google.android.apps.maps");
                startActivity(i);
            }
        });


        return view;
    }
    private void message(String name){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE},REQUEST_PHONE_MSG);
        }else
        {
            Intent in=new Intent(Intent.ACTION_VIEW);
            in.setData(Uri.parse("smsto:"));
            in.setType("vnd.android-dir/mms-sms");
            in.putExtra("address","8208101725");
            in.putExtra("sms_body","Hey, I am Archana. I need ur help");
            startActivity(Intent.createChooser(in,"SEND SMS VIA:"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_MSG: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    message(name);
                } else {
                    Toast.makeText(getActivity(), "SORRY, COULD NOT PROCESS YOUR REQUEST", Toast.LENGTH_LONG).show();
                }return;
            }
        }
    }


}