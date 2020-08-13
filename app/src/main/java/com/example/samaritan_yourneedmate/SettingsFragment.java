package com.example.samaritan_yourneedmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.USER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
Button btnlogout;
private TextView txtview1, txtview2,txtview3,txtview4;
ImageView imgview;
private FirebaseDatabase database;
private DatabaseReference userRef;
private static final String USER="User";
private String email,password;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_settings, container, false);
        btnlogout=v.findViewById(R.id.btn_logout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
                Toast.makeText(getActivity(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });

           // Intent intent=getActivity().getIntent();
           // email=intent.getStringExtra("email");

        txtview1=v.findViewById(R.id.textView_name);
        txtview2=v.findViewById(R.id.textView_email);
        txtview3=v.findViewById(R.id.textView_phno);
        txtview4=v.findViewById(R.id.textView_dob);
        imgview=v.findViewById(R.id.imageView_profilepic);

        Intent intent=getActivity().getIntent();
        String user_name=intent.getStringExtra("name");
        String user_email=intent.getStringExtra("email");
        String user_phno=intent.getStringExtra("phno");
        String user_dob=intent.getStringExtra("dob");

        txtview1.setText(user_name);
        txtview2.setText(user_email);
        txtview3.setText(user_phno);
        txtview4.setText(user_dob);


        return v;
    }
}