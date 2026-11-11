package com.example.devquest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.devquest.databinding.ActivitySetsBinding;

public class SetsActivity extends AppCompatActivity {

    ActivitySetsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content), this::onApplyWindowInsets);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding.imageView.setOnClickListener(v -> finish());

        binding.cardMultiple.setOnClickListener(v -> openQuiz("Multiple Choices"));

        binding.cardIdentification.setOnClickListener(v -> openQuiz("Identification"));

        binding.cardFillBlank.setOnClickListener(v -> openQuiz("Fill in the Blank"));
    }

    private void openQuiz(String mode) {
        Intent intent = new Intent(SetsActivity.this, QuestionActivity.class);
        intent.putExtra("set", mode);
        intent.putExtra("language", getIntent().getStringExtra("language"));
        startActivity(intent);
    }

    private WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    }
}