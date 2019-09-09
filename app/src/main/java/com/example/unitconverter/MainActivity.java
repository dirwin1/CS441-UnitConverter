package com.example.unitconverter;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.*;
import android.os.Handler;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText text1, text2;
    private String unit1 = "1";
    private String unit2 = "1";
    private Animation animFadeIn, animFadeOut;
    private ImageView mainImage;
    private int mInterval = 5000;
    private Handler mHandler;
    int imageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get spinners
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        //get imageview
        mainImage = (ImageView) findViewById(R.id.main_image);

        //get animations
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        //get text
        text1 = (EditText) findViewById(R.id.txtItem1);
        text2 = (EditText) findViewById(R.id.txtItem2);

        //text change listener
        text1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == text1) {
                    // is only executed if the EditText was directly changed by the user
                    Convert1to2();
                }
            }
        });

        text2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getCurrentFocus() == text2) {
                    // is only executed if the EditText was directly changed by the user
                    Convert2to1();
                }
            }
        });

        //animation listener
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageNum = (imageNum + 1) % 3;
                switch(imageNum){
                    case 0:
                        mainImage.setImageResource(R.drawable.image_0);
                        break;
                    case 1:
                        mainImage.setImageResource(R.drawable.image_1);
                        break;
                    case 2:
                        mainImage.setImageResource(R.drawable.image_2);
                        break;
                }

                animFadeIn.reset();
                mainImage.clearAnimation();
                mainImage.startAnimation(animFadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //handler
        mHandler = new Handler();
        StartRepeatingTask();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        switch(parent.getId()){
            case R.id.spinner1:
                unit1 = parent.getItemAtPosition(pos).toString();
                //text1.setText(unit1);
                break;
            case R.id.spinner2:
                unit2 = parent.getItemAtPosition(pos).toString();
                //text2.setText(unit2);
                break;
        }

        //do the conversion
        Convert1to2();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void Convert2to1(){
        if(text2.getText().length() == 0) return;

        float factor1 = getFactor(unit1);
        float factor2 = getFactor(unit2);

        float value2 = 0;

        try
        {
            value2 = Float.parseFloat(text2.getText().toString());
        }
        catch(Error e)
        {
            value2 = 0;
        }

        float value = (factor2 * value2)/factor1;

        text1.setText(Float.toString(value));
    }

    public void Convert1to2(){
        if(text1.getText().length() == 0) return;
        float factor1 = getFactor(unit1);
        float factor2 = getFactor(unit2);

        float value1 = 0; //Float.parseFloat(text1.getText().toString());

        try
        {
            value1 = Float.parseFloat(text1.getText().toString());
        }
        catch(Error e)
        {

        }

        float value = (factor1 * value1)/factor2;

        text2.setText(Float.toString(value));
    }

    public float getFactor(String unit){
        unit = unit.toLowerCase();
        switch(unit){
            case "meters":
                return 1;
            case "centimeters":
                return .01f;
            case "millimeters":
                return .001f;
            case "kilometers":
                return 1000;
            case "miles":
                return 1609.34f;
            case "inches":
                return .0254f;
            case "feet":
                return .3048f;
            case "yards":
                return .9144f;
            case "nautical miles":
                return 1852;
        }
        return 1;
    }

    public void SwapPhoto(){
        animFadeOut.reset();
        mainImage.clearAnimation();
        mainImage.startAnimation(animFadeOut);
    }

    void StartRepeatingTask() {
        mStatusChecker.run();
    }

    void StopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                SwapPhoto(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

}
