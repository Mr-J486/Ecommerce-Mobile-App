package com.example.e_commercemobapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commercemobapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnCategories = findViewById(R.id.btnCategories);
        Button btnProducts = findViewById(R.id.btnProducts);
        Button btnReports = findViewById(R.id.btnReports);

        btnCategories.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CategoryListActivity.class))
        );

        btnProducts.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProductListActivity.class))
        );

        btnReports.setOnClickListener(v -> {
            // TODO: Create Reports activity
        });
    }
}
