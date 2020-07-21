package com.example.samaritan_yourneedmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
Button btnlogout,btnaddno;
EditText edtfirst,edtsecond,edtthird;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtfirst=findViewById(R.id.edtfirstno);
        edtsecond=findViewById(R.id.edtsecondno);
        edtthird=findViewById(R.id.edtthirdno);
        btnlogout=findViewById(R.id.btn_logout);
        btnaddno=findViewById(R.id.btnaddno);
       sharedPreferences=getSharedPreferences("Your Numbers",MODE_PRIVATE);
       btnaddno.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sharedPreferences.edit().putInt("Value1", Integer.parseInt(edtfirst.getText().toString()))
                       .putInt("Value2", Integer.parseInt(edtsecond.getText().toString()))
                       .putInt("Value3", Integer.parseInt(edtthird.getText().toString())).commit();
               Toast.makeText(MainActivity.this, "Data Saved now click Refresh to apply changes", Toast.LENGTH_LONG).show();
           }
       });


        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}