package com.example.samaritan_yourneedmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
TextView txtvalue1,txtvalue2,txtvalue3,txtrefresh;
Button btnaddnos;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txtvalue1=findViewById(R.id.txtvalue1);
        txtvalue2=findViewById(R.id.txtvalue2);
        txtvalue3=findViewById(R.id.txtvalue3);
        txtrefresh=findViewById(R.id.txtrefresh);
        btnaddnos=findViewById(R.id.btnaddnos);
        sharedPreferences=getSharedPreferences("Your Numbers",MODE_PRIVATE);
        if (sharedPreferences.contains("Value1")){
            txtvalue1.setText(sharedPreferences.getInt("Value1",123)+"");
        }
        if (sharedPreferences.contains("Value2")){
            txtvalue2.setText(sharedPreferences.getInt("Value2",123)+"");
        }
        if (sharedPreferences.contains("Value3")){
            txtvalue3.setText(sharedPreferences.getInt("Value3",123)+"");
        }
        txtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtvalue1.setText(sharedPreferences.getInt("Value1",123)+"");
                txtvalue2.setText(sharedPreferences.getInt("Value2",123)+"");
                txtvalue3.setText(sharedPreferences.getInt("Value3",123)+"");
            }
        });
        btnaddnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(MainActivity2.this,MainActivity.class);
                startActivity(in);
            }
        });
    }
}