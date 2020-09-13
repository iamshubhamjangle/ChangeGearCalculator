package com.octa.gearindexer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {

    Button buttonCalculate, buttonClear;
    EditText et_iss, et_h;
    TextView tv_tg_a, tv_tg_b, tv_answer, tv_fg_a, tv_fg_b, tv_fg_c, tv_fg_d;
    String[] line;
    float iss, h, expectedAnswer;
    NestedScrollView nsv;

    ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Input layout
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonClear = findViewById(R.id.buttonClear);
        et_iss = findViewById(R.id.et_iss);
        et_h = findViewById(R.id.et_h);
        tv_answer = findViewById(R.id.textViewAnswer);

        //Main list view
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_simple_list_view , arrayList);
        listView.setAdapter(adapter);

        //two gear textView
        tv_tg_a = findViewById(R.id.tv_tg_A);
        tv_tg_b = findViewById(R.id.tv_tg_B);

        //fourGear
        tv_fg_a = findViewById(R.id.tv_fg_A);
        tv_fg_b = findViewById(R.id.tv_fg_B);
        tv_fg_c = findViewById(R.id.tv_fg_C);
        tv_fg_d = findViewById(R.id.tv_fg_D);

        nsv = findViewById(R.id.nestedScrollView);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(et_iss.getText().toString().isEmpty() || et_h.getText().toString().isEmpty())){
                    clearArrayList();
                    getInputs();
                    calculateTwoGears();
                    calculateFourGear();
                    postProcess();
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View view) {
                et_iss.setText("");
                et_h.setText("");
                tv_answer.setText("Answer");
                tv_tg_a.setText("0");
                tv_tg_b.setText("0");

                tv_fg_a.setText("0");
                tv_fg_b.setText("0");
                tv_fg_c.setText("0");
                tv_fg_d.setText("0");

                clearArrayList();
            }
        });

    }

    private void clearArrayList() {
        arrayList.clear();
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

                    String otherAnswer = String.format("%s  %s  --  --  %s", line[0], line[1], diff);
                    adapter.add(otherAnswer);
                    Log.d("MyTag EXACTOUTPUT: ", otherAnswer);
                    gearFound = TRUE;
                } else if(diff < 0.02){
                    String otherAnswer = String.format("%s  %s  --  --  %s", line[0], line[1], diff);
                    adapter.add(otherAnswer);
                    Log.w("MyTag APPROX ANSWER: ", String.format("difference = %f", diff));
                    gearFound = TRUE;
                }
            }

            if(gearFound == FALSE){
                Toast.makeText(getApplicationContext(),"No Matching 2 Gears found",Toast.LENGTH_SHORT).show();
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

                    String otherAnswer = String.format("%s  %s  %s  %s  %s", line[0], line[1], line[2], line[3], diff);
                    adapter.add(otherAnswer);

                    Log.d("MyTag EXACTOUTPUT: ", otherAnswer);
                    gearFound = TRUE;
                }
                else if(diff < 0.02){
                    String otherAnswer = String.format("%s  %s  %s  %s  %s", line[0], line[1], line[2], line[3], diff);
                    adapter.add(otherAnswer);

                    Log.w("MyTag APPROX ANSWER: ", String.format("difference = %f", diff));
                    gearFound = TRUE;
                }

            }

            if(gearFound == FALSE){
                Toast.makeText(getApplicationContext(),"No Matching 4 Gears found",Toast.LENGTH_SHORT).show();
                Log.e("MyTag NOT FOUND", "Answer not found");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MyTag", "Error" + e);
        }
    }

    private void postProcess() {
        //hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Change the target view to answer
        nsv.post(new Runnable() {
            @Override
            public void run() {
                nsv.smoothScrollTo(0, buttonClear.getBottom());
            }
        });
    }
}