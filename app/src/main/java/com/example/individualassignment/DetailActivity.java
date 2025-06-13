package com.example.individualassignment;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView detailText;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailText = findViewById(R.id.detail_text);
        dbHelper = new DataHelper(this);

        int recordId = getIntent().getIntExtra("RECORD_ID", -1);

        if (recordId != -1) {
            Cursor cursor = dbHelper.getDataById(recordId);
            if (cursor.moveToFirst()) {
                String month = cursor.getString(1);
                int kwh = cursor.getInt(2);
                double rebate = cursor.getDouble(3);
                double totalCharges = cursor.getDouble(4);
                double finalCost = cursor.getDouble(5);

                String result = "Month: " + month +
                        "\nUsage: " + kwh + " kWh" +
                        "\nRebate: " + rebate + "%" +
                        "\nTotal Charges: RM" + String.format("%.2f", totalCharges) +
                        "\nFinal Cost: RM" + String.format("%.2f", finalCost);
                detailText.setText(result);
            }
        } else {
            Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
        }
    }
}
