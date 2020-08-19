package com.example.samaritan_yourneedmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    EditText edtmail,edtpass;
    Button btnlogin;
    Button btnsignup;
    Intent intent;
    TextView txtfgtpass;
    ImageView imgbtn;
     FirebaseAuth fAuth;
     LottieAnimationView loadinganim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth=FirebaseAuth.getInstance();
           init();

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_n=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent_n);
                finish();
            }
        });

        //forgot password
        txtfgtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetemail= new EditText(v.getContext());
                AlertDialog.Builder passwordrestdialog=new AlertDialog.Builder(v.getContext());
                passwordrestdialog.setTitle("Forgot Password?").setIcon(R.drawable.resetpass);
                passwordrestdialog.setMessage("Enter the Email to recieve link to reset password:");
                passwordrestdialog.setView(resetemail);

                passwordrestdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           String mail=resetemail.getText().toString();
                           fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(LoginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(LoginActivity.this, "Link not sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });
                    }
                });
                passwordrestdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }); passwordrestdialog.create().show();
            }
        });

        //login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=edtmail.getText().toString();
                final String pass=edtpass.getText().toString();
                if (TextUtils.isEmpty(email)){
                    edtmail.setError("Email is Required");
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
                loadinganim.setVisibility(View.VISIBLE);
                loadinganim.playAnimation();
                if (fAuth.getCurrentUser()!=null){
                    intent= new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                loadinganim.setVisibility(View.VISIBLE);
                loadinganim.playAnimation();
                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            loadinganim.cancelAnimation();
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                            finish();
                        }else{
                            loadinganim.cancelAnimation();
                            loadinganim.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    private void init() {
        txtfgtpass=findViewById(R.id.txt_fgtpass);
        edtmail=findViewById(R.id.edtmail);
        edtpass=findViewById(R.id.edtpass);
        btnlogin=findViewById(R.id.btnlogin);
        btnsignup=findViewById(R.id.btnsignup);
        loadinganim=findViewById(R.id.loading_anim);
    }


    public void hidekeyboard(View view) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Do you really want to exit from the app?").setCancelable(false).setTitle("Exit the App?").
                setIcon(R.mipmap.app_logo).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.super.onBackPressed();
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertdialog=builder.create();
        alertdialog.show();

    }
}
