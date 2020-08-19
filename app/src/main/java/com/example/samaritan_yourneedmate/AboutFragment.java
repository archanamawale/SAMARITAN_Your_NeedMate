package com.example.samaritan_yourneedmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    TextView abtme;
    ImageView iv_mypic;
    View v;
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
              v= inflater.inflate(R.layout.fragment_about, container, false);
              abtme=v.findViewById(R.id.txtmyname);
              iv_mypic=v.findViewById(R.id.imgview_mypic);
              abtme.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/archana-mawale-866279176/"));
                      startActivity(browserIntent);
                  }
              });

              iv_mypic.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/archana-mawale-866279176/"));
                      startActivity(browserIntent);

                  }
              });
        return v;

    }
}