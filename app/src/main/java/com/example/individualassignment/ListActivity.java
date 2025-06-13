package com.example.individualassignment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    DataHelper dbHelper;
    ArrayList<String> billList;
    ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list_view);
        dbHelper = new DataHelper(this);
        billList = new ArrayList<>();
        idList = new ArrayList<>();

        loadBillData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int selectedId = idList.get(position);
            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
            intent.putExtra("RECORD_ID", selectedId);
            startActivity(intent);
        });

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadBillData() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No bill data found.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String month = cursor.getString(1);
            double finalCost = cursor.getDouble(5);

            idList.add(id);
            billList.add(month + ": RM" + String.format("%.2f", finalCost));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_white, R.id.text_view_item, billList);
        listView.setAdapter(adapter);
    }
}
