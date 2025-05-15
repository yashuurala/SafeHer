package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.PanicFragment;
import com.example.myapplication.ProfileFragment;

import com.example.myapplication.TipsFragment;
//import com.example.myapplication.MessageSender;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainpageActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    FragmentManager fragmentManager;
    private AlertDialog speedDialog;
    private CountDownTimer dialogTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        bnv = findViewById(R.id.bottomNavBar);
        fragmentManager = getSupportFragmentManager();


        showFragment(new TipsFragment());

        bnv.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_panic) {
                showFragment(new PanicFragment());
                return true;
            } else if (itemId == R.id.bottom_tips) {
                showFragment(new TipsFragment());
                return true;
            } else if (itemId == R.id.bottom_profile) {
                showFragment(new ProfileFragment());
                return true;
            }else if(itemId == R.id.bottom_location){
                showFragment(new LocationFragment());
                return true;
            }
            return false;
        });
    }
    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
