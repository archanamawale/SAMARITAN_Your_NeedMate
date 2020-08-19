package com.example.samaritan_yourneedmate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.USER_SERVICE;
import static com.example.samaritan_yourneedmate.DashboardActivity.user;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
Button btnlogout, btnresetpass;
private TextView txtview1, txtview2,txtview3,txtview4;
LottieAnimationView loadinganim;
View v;

ImageView imgview;
    private FirebaseAuth fAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_settings, container, false);
            init();
            setdata();
        fAuth=FirebaseAuth.getInstance();

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmail();
            }
        });
        
        btnresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetemail= new EditText(v.getContext());
                AlertDialog.Builder passwordrestdialog=new AlertDialog.Builder(v.getContext());
                passwordrestdialog.setTitle("Reset your Password?").setIcon(R.drawable.resetpass);
                passwordrestdialog.setMessage("Enter the Email to recieve link to reset password:");
                passwordrestdialog.setCancelable(true);
                passwordrestdialog.setView(resetemail);

                passwordrestdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail=resetemail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Reset Link sent to your Email", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                                Toast.makeText(getActivity(), "Logged Out Successfully. Please ReLogin to continue!!!", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Link not sent"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordrestdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordrestdialog.create().show();

            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
                Toast.makeText(getActivity(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void setdata() {
        loadinganim.setVisibility(View.VISIBLE);
        loadinganim.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadinganim.cancelAnimation();
                loadinganim.setVisibility(View.GONE);
                try {
                    txtview1.setText(user.getName());
                    txtview2.setText(user.getEmail());
                    txtview3.setText(user.getPhno());
                    txtview4.setText(user.getDob());
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Loading Error please try again", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2200);
    }

    private void init() {
        loadinganim=v.findViewById(R.id.loading_anim);
        imgview=v.findViewById(R.id.imgview_contactus);

        btnlogout=v.findViewById(R.id.btn_logout);
        btnresetpass=v.findViewById(R.id.btnresetpass);
        txtview1=v.findViewById(R.id.textView_name);
        txtview2=v.findViewById(R.id.textView_email);
        txtview3=v.findViewById(R.id.textView_phno);
        txtview4=v.findViewById(R.id.textView_dob);
        imgview=v.findViewById(R.id.imageView_profilepic);
    }

    private void sendmail() {

        final EditText contactus= new EditText(getContext());
        final AlertDialog.Builder calldialog=new AlertDialog.Builder(getContext());
        calldialog.setTitle("CONTACT US");
        calldialog.setMessage("Enter your Message/ Feedback/ Queries:");
        calldialog.setCancelable(true);
        calldialog.setView(contactus);
        calldialog.setPositiveButton("SEND EMAIL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String getmsg= contactus.getText().toString().trim();
                String mailto="samaritanyourneedmate5@gmail.com";
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{mailto});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Samaritan User!!!");
                intent.putExtra(Intent.EXTRA_TEXT,getmsg);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"SEND E-MAIL VIA:"));
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