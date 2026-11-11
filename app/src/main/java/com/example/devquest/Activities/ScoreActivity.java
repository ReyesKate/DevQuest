package com.example.devquest.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.devquest.Homepage;
import com.example.devquest.R;
import com.example.devquest.databinding.ActivityScoreBinding;

import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityScoreBinding binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        int total = getIntent().getIntExtra("total", 0);
        int correct = getIntent().getIntExtra("score", 0);
        long timeMillis = getIntent().getLongExtra("time", 0);

        int percent = total == 0 ? 0 : (correct * 100) / total;

        binding.scoreText.setText(getString(R.string.fraction_format, correct, total));
        binding.percentSmall.setText(getString(R.string.percent_format, percent));
        binding.percentText.setText(getString(R.string.percent_format, percent));

        ObjectAnimator animator = ObjectAnimator.ofInt(binding.scoreProgress, "progress", 0, percent);
        animator.setDuration(800);
        animator.start();

        long totalSeconds = timeMillis / 1000;
        binding.timeText.setText(String.format(Locale.getDefault(), "%02d:%02d",
                totalSeconds / 60, totalSeconds % 60));

        binding.btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, Homepage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnRetake.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreActivity.this, SetsActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}