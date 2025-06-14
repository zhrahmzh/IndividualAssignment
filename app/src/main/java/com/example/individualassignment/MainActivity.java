package com.example.individualassignment;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText inputKwh;
    RadioGroup rebateGroup;
    TextView textResult;
    Button btnCalculate;
    Button btnHistory;
    ImageButton btnAbout;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinner_month);
        inputKwh = findViewById(R.id.input_kwh);
        rebateGroup = findViewById(R.id.rebate_group);
        textResult = findViewById(R.id.text_result);
        btnCalculate = findViewById(R.id.btn_calculate);
        btnHistory = findViewById(R.id.btn_history);
        btnAbout = findViewById(R.id.btn_about);

        dbHelper = new DataHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, R.layout.spinner_item_white);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        inputKwh.setTextColor(getResources().getColor(android.R.color.white));
        inputKwh.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        inputKwh.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));

        btnCalculate.setOnClickListener(v -> calculateBill());

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        String month = spinnerMonth.getSelectedItem().toString();
        String kwhStr = inputKwh.getText().toString().trim();

        if (kwhStr.isEmpty()) {
            showAlert("Input Error", "Please enter electricity usage.");
            return;
        }

        int selectedId = rebateGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            showAlert("Missing Input", "Please select a rebate percentage.");
            return;
        }

        RadioButton selectedRadio = findViewById(selectedId);
        String rebateStr = selectedRadio.getText().toString().replace("%", "");

        try {
            int kwh = Integer.parseInt(kwhStr);
            double rebate = Double.parseDouble(rebateStr);

            if (kwh < 0) {
                showAlert("Invalid Usage", "Electricity usage must be a positive number.");
                return;
            }

            double totalCharges = calculateCharges(kwh);
            double finalCost = totalCharges - (totalCharges * rebate / 100.0);

            DecimalFormat df = new DecimalFormat("#0.00");
            String result = String.format("Month: %s\nTotal Charges: RM%s\nFinal Cost: RM%s",
                    month, df.format(totalCharges), df.format(finalCost));
            textResult.setText(result);

            boolean inserted = dbHelper.insertData(month, kwh, rebate, totalCharges, finalCost);
            if (inserted) {
                Toast.makeText(this, "Bill saved successfully.", Toast.LENGTH_SHORT).show();
                inputKwh.setText("");
                rebateGroup.clearCheck();
                spinnerMonth.setSelection(0);
            } else {
                showAlert("Database Error", "Failed to save bill.");
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers.");
        }
    }

    private double calculateCharges(int kwh) {
        double total = 0;
        if (kwh <= 200) {
            total = kwh * 0.218;
        } else if (kwh <= 300) {
            total = 200 * 0.218 + (kwh - 200) * 0.334;
        } else if (kwh <= 600) {
            total = 200 * 0.218 + 100 * 0.334 + (kwh - 300) * 0.516;
        } else {
            total = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (kwh - 600) * 0.546;
        }
        return total;
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
