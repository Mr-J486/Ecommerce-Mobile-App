package com.example.e_commercemobapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commercemobapp.R;
import com.example.e_commercemobapp.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnCategories = findViewById(R.id.btnCategories);
        Button btnProducts = findViewById(R.id.btnProducts);
        Button btnReports = findViewById(R.id.btnReports);
        Button btnLogout = findViewById(R.id.adminlogoutBtn);

        btnCategories.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CategoryListActivity.class))
        );

        btnProducts.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProductListActivity.class))
        );

        btnReports.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReportActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finishAffinity();
        });

        // 🔥 NEW BACK HANDLER (Modern Android)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity(); // close app
            }
        });
    }
}
