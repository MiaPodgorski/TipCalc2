package com.example.tipcalculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView tipPercent;
    TextView total;
    EditText billInput;
    RadioGroup radioGroup;
    RadioButton Button;
    RadioButton Button2;
    EditText partySize;
    TextView splitText;
    Button settingsButton;


    private  boolean split;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        tipPercent = findViewById(R.id.tipPercent);
        total = findViewById(R.id.total);
        billInput = findViewById(R.id.billInput);
        radioGroup = findViewById(R.id.radioGroup);
        Button = findViewById(R.id.Button);
        Button2 = findViewById(R.id.Button2);
        partySize = findViewById(R.id.partySize);
        splitText = findViewById(R.id.splitText);
        settingsButton = findViewById(R.id.settingsButton);

//text has to be in form of string; error below because had just int
        tipPercent.setText("tip " + seekBar.getProgress() + "%");
        partySize.setText("1");
        //set tip percent to seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                calculateBill();
                calcSplitBill();

                String bill = billInput.getText().toString();
                double Bill = Double.parseDouble(bill);
                double tip = seekBar.getProgress();
                double dollarTip =(Bill * (tip / 100));
                tipPercent.setText("tip"+i+"% or $"+String.format("%.2f",dollarTip) );

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //when purchase price is input, auto calculate total bill
        billInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    calculateBill();
                    calcSplitBill();
                    String bill = billInput.getText().toString();
                    double Bill = Double.parseDouble(bill);
                    double tip = seekBar.getProgress();
                    double dollarTip =(Bill * (tip / 100));
                    tipPercent.setText("tip "+i+"% or $"+String.format("%.2f",dollarTip) );
                }
                return false;
            }
        });

        partySize.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==EditorInfo.IME_ACTION_DONE && Button.isChecked()){
                    calcSplitBill();
                }
                return false;
            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.Button) {
                    partySize.setHint("enter party size");
                    calculateBill();
                    calcSplitBill();

                } else if (i == R.id.Button2) {
                    partySize.setHint("1");
                    partySize.setText("1");
                    calculateBill();
                    calcSplitBill();
                }
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }

        private void calculateBill() {
            try {
                String bill = billInput.getText().toString();
                double Bill = Double.parseDouble(bill);
                double tip = seekBar.getProgress();
                double calc = (Bill + (Bill * (tip / 100)));
                total.setText("total $" + String.format("%.2f",calc));
            } catch(NumberFormatException ex) {
                total.setText("fill out all boxes");
            }
        }

        private void calcSplitBill() {
            try {
                String party = partySize.getText().toString();
                double Party = Double.parseDouble(party);
                String bill = billInput.getText().toString();
                double Bill = Double.parseDouble(bill);
                double tip = seekBar.getProgress();
                double calc = ((Bill + (Bill * (tip / 100))) / Party);
                splitText.setText("$" + String.format("%.2f",calc) + " per person");
            } catch (NumberFormatException ex) {
                total.setText("fill out all boxes");
            }
        }



    private void updateSharedPreference(){
          //Default tip setting
        int tip =seekBar.getProgress();


         //default split bill
        if (Button.isChecked()){
            split = true;
        }else if(Button2.isChecked()){
            split =false;
        }

    //default party size
        String number = partySize.getText().toString();
        size = Integer.parseInt(number);

        SharedPreferences sp= getSharedPreferences("shared",MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.putInt("tip default",tip);
        editor.putBoolean("split bill",split);
        editor.putInt("party size", size);
        editor.commit();
    }


        private void updateTip(){
            SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
           int tip =sp.getInt("tip default",15);
           seekBar.setProgress(tip);
           boolean b = sp.getBoolean("split bill", false);
           Button.setChecked(b);
           Button2.setChecked(!b);  //must set other button too
            int p = sp.getInt("party size", 1);
            partySize.setText(p+"");

        }
        @Override
        public void onResume(){
            super.onResume();
            updateTip();
        }

    }
