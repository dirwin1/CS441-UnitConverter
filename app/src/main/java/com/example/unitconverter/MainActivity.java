package com.example.unitconverter;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner1, spinner2, spinner3;
    private EditText text1, text2;
    private String unitType = "length";
    private String unit1 = "1";
    private String unit2 = "1";
    private Animation animFadeIn, animFadeOut;
    private ImageView mainImage;
    private int mInterval = 5000;
    private Handler mHandler;
    private GraphView graph;
    int imageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //unit spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.length_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        //unit spinners
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter2);
        spinner3.setOnItemSelectedListener(this);


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

        //get graph
        graph = (GraphView) findViewById(R.id.graph);
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
                //do the conversion
                Convert1to2();
                break;
            case R.id.spinner2:
                unit2 = parent.getItemAtPosition(pos).toString();
                //do the conversion
                Convert1to2();
                break;
            case R.id.spinner3:
                unitType = parent.getItemAtPosition(pos).toString().toLowerCase();
                SwapUnitType();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void SwapUnitType(){
        ArrayAdapter<CharSequence> adapter = null;
        switch(unitType){
            case "length":
                //unit spinners
                adapter = ArrayAdapter.createFromResource(this, R.array.length_array, android.R.layout.simple_spinner_item);
                break;
            case "weight":
                //unit spinners
                adapter = ArrayAdapter.createFromResource(this, R.array.weight_array, android.R.layout.simple_spinner_item);
                break;
            case "time":
                //unit spinners
                adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item);
                break;
        }

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        Convert1to2();
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

        float value = value2 * factor2 /factor1;

        text1.setText(Float.toString(value));

        UpdateGraph(factor1, factor2);
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

        float value = value1 * factor1 / factor2;

        text2.setText(Float.toString(value));

        UpdateGraph(factor1, factor2);
    }

    public void UpdateGraph(float factor1, float factor2){
        graph.removeAllSeries();
        LineGraphSeries <DataPoint> series = new LineGraphSeries< >(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, factor1)
        });
        graph.addSeries(series);

        LineGraphSeries <DataPoint> series2 = new LineGraphSeries< >(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, factor2)
        });
        graph.addSeries(series2);
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

            case "kilograms":
                return 1;
            case "grams":
                return .001f;
            case "metric tons":
                return 1000;
            case "imperial tons":
                return 1016.05f;
            case "us tons":
                return 907.185f;
            case "pounds":
                return 0.453592f;
            case "ounces":
                return 0.0283495f;

            case "milliseconds":
                return .001f;
            case "seconds":
                return 1;
            case "minutes":
                return 60;
            case "hours":
                return 3600;
            case "days":
                return 86400;
            case "weeks":
                return 604800;
            case "months":
                return 2.628e+6f;
            case "years":
                return 3.154e+7f;
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
