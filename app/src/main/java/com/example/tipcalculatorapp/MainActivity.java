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
                String bill = billInput.getText().toString();
                double Bill = Double.parseDouble(bill);
                double tip = seekBar.getProgress();
                tipPercent.setText("tip " + i + "%" + "or $" + (Bill * (tip / 100)));
                total.setText("total $" + (Bill + (Bill * (tip / 100))));
                String party = partySize.getText().toString();
                double Party = Double.parseDouble(party);
                splitText.setText("$" + (Bill + (Bill * (tip / 100))) / Party + " per person");

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
                    String bill = billInput.getText().toString();
                    double Bill = Double.parseDouble(bill);
                    double tip = seekBar.getProgress();
                    //problem with sting part of this "tip" "%"
                    total.setText("total $" + (Bill + (Bill * (tip / 100))));
                }
                return false;
            }
        });

        //when split bill selected divide total by party size
        // (splitText.getText().toString().equals("")) checking if empty did not figure out
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.Button) {
                    partySize.setHint("enter party size");
                    String party = partySize.getText().toString();
                    double Party = Double.parseDouble(party);
                    String bill = billInput.getText().toString();
                    double Bill = Double.parseDouble(bill);
                    double tip = seekBar.getProgress();
                    splitText.setText("$" + (Bill + (Bill * (tip / 100))) / Party + " per person");
                } else if (i == R.id.Button2) {
                    partySize.setHint("1");
                    String bill = billInput.getText().toString();
                    double Bill = Double.parseDouble(bill);
                    double tip = seekBar.getProgress();
                    splitText.setText((Bill + (Bill * (tip / 100))) + " per person");
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

        private void updateTip(){
            SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
           int tip =sp.getInt("tip default",15);
           seekBar.setProgress(tip);
           boolean b = sp.getBoolean("button", false);
           Button.setChecked(b);
           Button2.setChecked(!b);  //must set other button too
            int p = sp.getInt("party size", 1);
            partySize.setText(p);

        }
        @Override
        public void onResume(){
            super.onResume();
            updateTip();
        }

    }
