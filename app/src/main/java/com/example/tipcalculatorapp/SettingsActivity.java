package com.example.tipcalculatorapp;

import static com.example.tipcalculatorapp.R.id.tipSeekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    EditText tipSeekbar;
    private int t;
    Switch splitBill;
    private  boolean split;
    EditText partyNumber;
    private int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



    }


    @Override
    public void onPause(){
        super.onPause();
        updateSharedPreference();
    }

    private void updateSharedPreference(){
        tipSeekbar = findViewById(R.id.tipSeekbar);   //Default tip setting
        String tip =tipSeekbar.getText().toString();
        t = Integer.parseInt(tip);

        splitBill = findViewById(R.id.splitBill);   //default split bill
        if (splitBill.isChecked()){
            split = true;
        }

        partyNumber = findViewById(R.id.partyNumber);       //default party size
        String number = partyNumber.getText().toString();
        size = Integer.parseInt(number);



        SharedPreferences sp= getSharedPreferences("shared",MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putInt("tip default",t);
        editor.putBoolean("split bill",split);
        editor.putInt("party size", size);
        editor.commit();
    }
}