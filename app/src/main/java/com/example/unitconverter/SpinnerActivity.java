package com.example.unitconverter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //get text
        text1 = (EditText) findViewById(R.id.txtItem1);
        text2 = (EditText) findViewById(R.id.txtItem2);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        if(id == R.id.spinner1){
            text2.setText(parent.getItemAtPosition(pos).toString());
        }
        else{
            text2.setText(parent.getItemAtPosition(pos).toString());
        }
        parent.getItemAtPosition(pos);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
