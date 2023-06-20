package com.example.calculatorapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText numbersEditText;
    private TextView resultTextView;
    private List<Double> numbersList;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "CalculatorPrefs";
    private static final String PREF_KEY_NUMBERS = "numbers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        numbersEditText = findViewById(R.id.numbersEditText);
        resultTextView = findViewById(R.id.resultTextView);

        // Initialize numbers list
        numbersList = new ArrayList<>();

        // Retrieve previously entered numbers from SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String savedNumbers = sharedPreferences.getString(PREF_KEY_NUMBERS, "");
        numbersEditText.setText(savedNumbers);
        numbersList = convertStringToList(savedNumbers);

        // Set operation buttons click listeners
        Button additionButton = findViewById(R.id.additionButton);
        additionButton.setOnClickListener(v -> {
            performOperation("+");
            saveNumbers();
        });

        Button subtractionButton = findViewById(R.id.subtractionButton);
        subtractionButton.setOnClickListener(v -> {
            performOperation("-");
            saveNumbers();
        });

        Button multiplicationButton = findViewById(R.id.multiplicationButton);
        multiplicationButton.setOnClickListener(v -> {
            performOperation("*");
            saveNumbers();
        });

        Button divisionButton = findViewById(R.id.divisionButto);
        divisionButton.setOnClickListener(v -> {
            performOperation("/");
            saveNumbers();
        });

        // Save numbers when the app is closed
        saveNumbersOnExit();
    }

    private void performOperation(String operator) {
        String numbersString = numbersEditText.getText().toString();
        numbersList = convertStringToList(numbersString);

        double result = 0;

        if (numbersList.size() >= 2) {
            result = numbersList.get(0);
            for (int i = 1; i < numbersList.size(); i++) {
                double number = numbersList.get(i);
                switch (operator) {
                    case "+":
                        result += number;
                        break;
                    case "-":
                        result -= number;
                        break;
                    case "*":
                        result *= number;
                        break;
                    case "/":
                        result /= number;
                        break;
                }
            }
        }

        resultTextView.setText("Result: " + result);
    }

    private List<Double> convertStringToList(String numbersString) {
        List<Double> numbersList = new ArrayList<>();

        String[] numbersArray = numbersString.split("\\s+");

        for (String number : numbersArray) {
            try {
                double num = Double.parseDouble(number);
                numbersList.add(num);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return numbersList;
    }

    private void saveNumbers() {
        String numbers = numbersEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY_NUMBERS, numbers);
        editor.apply();
    }

    private void saveNumbersOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> saveNumbers()));
    }
}
