package com.example.samaritan_yourneedmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText edtmail,edtpass;
    Button btnlogin;
    Button btnsignup_new;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtmail=findViewById(R.id.edtmail);
        edtpass=findViewById(R.id.edtpass);
        btnlogin=findViewById(R.id.btnlogin);
        btnsignup_new=findViewById(R.id.btn_sign_up);
        intent = new Intent(LoginActivity.this,SignUpActivity.class);
        btnsignup_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=edtmail.getText().toString();
                String pass=edtpass.getText().toString();
            }
        });
    }
}
