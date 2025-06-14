package com.example.individualassignment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutText = findViewById(R.id.about_text);

        String info = "<b>Name:</b><br>" +
                "SITI FATIMAHTULZAHRA BINTI HAMZAH<br><br>" +
                "<b>Student ID:</b><br>" +
                "2024948097<br><br>" +
                "<b>Course:</b><br>" +
                "CDCS240 - INFORMATION TECHNOLOGY<br><br>" +
                "<b>Copyright:</b><br>" +
                "© 2025 Siti Fatimahtulzahra Binti Hamzah. All rights reserved. <br><br>" +
                "<b>GitHub Repository:</b><br>" +
                "<a href='https://github.com/zhrahmzh/IndividualAssignment.git'>https://github.com/zhrahmzh/IndividualAssignment.git</a>";

        aboutText.setText(Html.fromHtml(info));
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());


        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

    }
}
