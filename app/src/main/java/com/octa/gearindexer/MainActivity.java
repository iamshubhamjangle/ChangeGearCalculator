package com.octa.gearindexer;

import androidx.appcompat.app.AppCompatActivity;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    Button buttonCalculate;
    EditText et_iss, et_h;
    TextView tv_tg_a, tv_tg_b, tv_answer, tv_fg_a, tv_fg_b, tv_fg_c, tv_fg_d;
    String[] line;
    float iss, h, expectedAnswer;

    //Two Gear
    ListView listViewTwoGear;
    private ArrayAdapter<String> adapterTwoGear;
    private ArrayList<String> arrayListTwoGear;

    //four gear
    ListView listViewFourGear;
    private ArrayAdapter<String> adapterFourGear;
    private ArrayList<String> arrayListFourGear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Input layout
        buttonCalculate = findViewById(R.id.buttonCalculate);
        et_iss = findViewById(R.id.et_iss);
        et_h = findViewById(R.id.et_h);
        tv_answer = findViewById(R.id.textViewAnswer);

        //two gear textView
        tv_tg_a = findViewById(R.id.tv_tg_A);
        tv_tg_b = findViewById(R.id.tv_tg_B);
        listViewTwoGear = findViewById(R.id.listViewTwoGear);
        arrayListTwoGear = new ArrayList<>();
        adapterTwoGear = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayListTwoGear);
        listViewTwoGear.setAdapter(adapterTwoGear);

        //fourGear
        tv_fg_a = findViewById(R.id.tv_fg_A);
        tv_fg_b = findViewById(R.id.tv_fg_B);
        tv_fg_c = findViewById(R.id.tv_fg_C);
        tv_fg_d = findViewById(R.id.tv_fg_D);
        listViewFourGear = findViewById(R.id.listViewFourGear);
        arrayListFourGear = new ArrayList<>();
        adapterFourGear = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayListTwoGear);
        listViewFourGear.setAdapter(adapterFourGear);


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(et_iss.getText().toString().isEmpty() || et_h.getText().toString().isEmpty())){
                    clearArrayList();
                    getInputs();
                    calculateTwoGears();
                    calculateFourGear();
                }
            }
        });
    }

    private void clearArrayList() {
        arrayListTwoGear.clear();
    }

    @SuppressLint("SetTextI18n")
    private void getInputs() {
        //getting input from editText
        iss = Float.parseFloat(et_iss.getText().toString());
        h = Float.parseFloat(et_h.getText().toString());
        expectedAnswer = iss / h;

        //setting answer to textView answer in Input Field
        tv_answer.setText(Float.toString(expectedAnswer));

        Log.d("MyTag INPUT", String.format("iss= %s h= %s Exp Answer= %s", iss, h, expectedAnswer));
    }

    private void calculateTwoGears() {
        try {
            boolean gearFound = FALSE;
            InputStream csvStream = getResources().openRawResource(R.raw.twogear);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);

            // throw away the header
            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                //get the difference between actual and current line answer
                float diff = Math.abs(Float.parseFloat(line[2]) - expectedAnswer);
                Log.d("MyTag", "The difference is = " + diff);

                if(Float.parseFloat(line[2]) == expectedAnswer){
                    tv_tg_a.setText(line[0]);
                    tv_tg_b.setText(line[1]);
                    String otherAnswer = String.format("A = %s  B = %s  Error = %s", line[0], line[1], diff);
                    adapterTwoGear.add(otherAnswer);
                    Log.d("MyTag EXACTOUTPUT: ", String.format("A = %s  B = %s  Error = %s", line[0], line[1], diff));
                    gearFound = TRUE;
                } else if(diff < 0.02){
                    String otherAnswer = String.format("A = %s  B = %s  Error = %s", line[0], line[1], diff);
                    adapterTwoGear.add(otherAnswer);
                    Log.w("MyTag APPROX ANSWER: ", String.format("difference = %f", diff));
                    gearFound = TRUE;
                }
            }

            if(gearFound == FALSE){
                Toast.makeText(getApplicationContext(),"No Matching 2 Gears found",Toast.LENGTH_LONG).show();
                Log.e("MyTag NOT FOUND", "Answer not found");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyTag", "Error" + e);
        }
    }

    private void calculateFourGear() {
        try {
            boolean gearFound = FALSE;
            InputStream csvStream = getResources().openRawResource(R.raw.fourgear);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);

            // throw away the header
            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                //get the difference between actual and current line answer
                float diff = Math.abs(Float.parseFloat(line[4]) - expectedAnswer);
                Log.d("MyTag", "The difference is = " + diff);

                if(Float.parseFloat(line[4]) == expectedAnswer){
                    tv_fg_a.setText(line[0]);
                    tv_fg_b.setText(line[1]);
                    tv_fg_c.setText(line[2]);
                    tv_fg_d.setText(line[3]);

                    String otherAnswer = String.format("%s  %s  %s  %s  Error = %s", line[0], line[1], line[2], line[3], diff);
                    adapterTwoGear.add(otherAnswer);
                    Log.d("MyTag EXACTOUTPUT: ", String.format("%s  %s  %s  %s  Error = %s", line[0], line[1], line[2], line[3], diff));
                    gearFound = TRUE;
                } else if(diff < 0.02){
                    String otherAnswer = String.format("%s  %s  %s  %s  Error = %s", line[0], line[1], line[2], line[3], diff);
                    adapterTwoGear.add(otherAnswer);
                    Log.w("MyTag APPROX ANSWER: ", String.format("difference = %f", diff));
                    gearFound = TRUE;
                }
            }

            if(gearFound == FALSE){
                Toast.makeText(getApplicationContext(),"No Matching 4 Gears found",Toast.LENGTH_LONG).show();
                Log.e("MyTag NOT FOUND", "Answer not found");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyTag", "Error" + e);
        }
    }
}