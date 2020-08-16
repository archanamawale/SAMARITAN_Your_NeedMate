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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
    Button btnsignup_new;
    Intent intent;
    TextView txtfgtpass;
    ProgressBar pblogin;
    LoginButton btnfb; //facebook login button
    private CallbackManager mCallbackManager;
     FirebaseAuth mAuth,fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtfgtpass=findViewById(R.id.txt_fgtpass);
        edtmail=findViewById(R.id.edtmail);
        edtpass=findViewById(R.id.edtpass);
        btnlogin=findViewById(R.id.btnlogin);
        pblogin=findViewById(R.id.pb_login);
        fAuth=FirebaseAuth.getInstance();

        txtfgtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetemail= new EditText(v.getContext());
                AlertDialog.Builder passwordrestdialog=new AlertDialog.Builder(v.getContext());
                passwordrestdialog.setTitle("Forgot Password?");
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
                pblogin.setVisibility(View.VISIBLE);
                if (fAuth.getCurrentUser()!=null){
                    intent= new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                pblogin.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                            pblogin.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        btnsignup_new=findViewById(R.id.btn_sign_up);
        btnsignup_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        //facebook login :
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
         btnfb=findViewById(R.id.btn_fb);
         btnfb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,Arrays.asList("email", "public_profile"));
                    LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.d("Success", "facebook:onSuccess:" + loginResult);
                            handleFacebookAccessToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                            Log.d("cancel", "facebook:onCancel");
                            mAuth.signOut();
                            // ...
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d("error", "facebook:onError", error);
                            // ...
                        }
                    });
             }
         });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("abc", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("abc", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("abc", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser= mAuth.getCurrentUser();
    }
}
