package com.example.samaritan_yourneedmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
private EditText edtname,edtphno,edtpass,edtemail;
TextView edtdob;
Button signup,slogin;
DatePickerDialog datePickerDialog;
FirebaseAuth fAuth;
ProgressBar progressBar;
DatabaseReference databaseReference;
FirebaseDatabase database;
    private String TAG="Email Verification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtname=findViewById(R.id.edt_fullname);
        edtphno=findViewById(R.id.edt_phno);
        edtdob=findViewById(R.id.edt_dob);
        edtpass=findViewById(R.id.edt_spass);
        edtemail=findViewById(R.id.edt_semail);

        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();


        slogin=findViewById(R.id.btn_slogin);
        slogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        signup=findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtemail.getText().toString().trim();
                String pass=edtpass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    edtemail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    edtpass.setError("Password is Required");
                    return;
                }
                if (pass.length()<=6){
                    edtpass.setError("Password must be more than 6 characters long");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                if (fAuth.getCurrentUser()!=null){
                    Intent in= new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            storeUserInformation();
                            Toast.makeText(SignUpActivity.this,"SignedUp Successfully \n Please Login to Continue",Toast.LENGTH_SHORT).show();
                            Intent in= new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(in);

                        }else{
                            Toast.makeText(SignUpActivity.this, "Error Occured"+task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });

        //datepicker entry
        edtdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                 int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog=new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtdob.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }
    private void storeUserInformation(){
        String name=edtname.getText().toString();
        String phno=edtphno.getText().toString();
        String dob=edtdob.getText().toString();
        String email=edtemail.getText().toString();
        String password=edtpass.getText().toString();

        UserInformation userInformation=new UserInformation();
        userInformation.setDob(dob);
        userInformation.setPhno(phno);
        userInformation.setName(name);
        userInformation.setEmail(email);
        userInformation.setPassword(password);
        //FirebaseUser user=fAuth.getCurrentUser();
        //databaseReference.child(user.getUid()).setValue(userInformation);
        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().
                getUid()).setValue(userInformation);
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

}