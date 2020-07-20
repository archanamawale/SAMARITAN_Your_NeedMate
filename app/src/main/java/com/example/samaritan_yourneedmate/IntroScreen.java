package com.example.samaritan_yourneedmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends AppCompatActivity {
    ViewPager screenpager;
    IntroViewPagerAdapter introViewPagerAdapter;
    List<ScreenItem> mList;
    TabLayout tabIndicator;
    Button btnnext,btnGetStarted;
    int position = 0;
    Animation btnanim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);

        btnnext=findViewById(R.id.btn_next);
        btnGetStarted=findViewById(R.id.btn_GetStarted);
        btnanim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        final Intent in= new Intent(getApplicationContext(),SplashActivity.class);


        if (restorePrefData()){
            Intent intent= new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(intent);
            finish();
        }

        getSupportActionBar().hide();

        tabIndicator=findViewById(R.id.tab_indicator);

        final List<ScreenItem> mList=new ArrayList<>();
        mList.add(new ScreenItem("Your NeedMate","APP to Help you in Need",R.drawable.help_person));
        mList.add(new ScreenItem("Shake 'N' Activate","Shake your phone thrice to activate the app",
                R.drawable.shake_phone));
        mList.add(new ScreenItem("Voice Recognization","Activate app with your voice and " +
                "store audio files as you Diary",R.drawable.voice_control));

        screenpager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter=new IntroViewPagerAdapter(this,mList);
       screenpager.setAdapter(introViewPagerAdapter);

       tabIndicator.setupWithViewPager(screenpager);

       btnnext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               position= screenpager.getCurrentItem();
               if (position < mList.size()){

                   position++;
                   screenpager.setCurrentItem(position);

               }
               if (position == mList.size()){

                   loadLastScreen();

               }

           }


       });

       btnGetStarted.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               startActivity(in);

               savePrefsData();
               finish();
           }
       });

       tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               if (tab.getPosition() == mList.size()-1){
                   loadLastScreen();
               }
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });

    }

    private boolean restorePrefData() {
        SharedPreferences pref=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroScreenActivityOpenedBefore =pref.getBoolean("isIntroScreenOpened",false);
        return isIntroScreenActivityOpenedBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putBoolean("isIntroScreenOpened",true);
        editor.commit();
    }

    private void loadLastScreen() {
        btnnext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnanim);
    }
}