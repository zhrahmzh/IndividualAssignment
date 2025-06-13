// MainActivity.java
package com.example.individualassignment;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText inputKwh, inputRebate;
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
        inputRebate = findViewById(R.id.input_rebate);
        textResult = findViewById(R.id.text_result);
        btnCalculate = findViewById(R.id.btn_calculate);
        btnHistory = findViewById(R.id.btn_history);
        btnAbout = findViewById(R.id.btn_about);

        dbHelper = new DataHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, R.layout.spinner_item_white); // Custom white text layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        inputKwh.setTextColor(getResources().getColor(android.R.color.white));
        inputKwh.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        inputKwh.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));

        inputRebate.setTextColor(getResources().getColor(android.R.color.white));
        inputRebate.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        inputRebate.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));

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
        String rebateStr = inputRebate.getText().toString().trim();

        if (kwhStr.isEmpty() || rebateStr.isEmpty()) {
            Toast.makeText(this, "Please enter both kWh and rebate.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int kwh = Integer.parseInt(kwhStr);
            double rebate = Double.parseDouble(rebateStr);

            if (rebate < 0 || rebate > 5) {
                Toast.makeText(this, "Rebate must be between 0 and 5%.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (kwh < 0) {
                Toast.makeText(this, "Electricity usage must be positive.", Toast.LENGTH_SHORT).show();
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
                inputRebate.setText("");
                spinnerMonth.setSelection(0);
            } else {
                Toast.makeText(this, "Failed to save bill.", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number input.", Toast.LENGTH_SHORT).show();
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
}
