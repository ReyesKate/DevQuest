package com.example.devquest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HelpActivity extends AppCompatActivity {

    private static final String SUPPORT_EMAIL = "ddevquest@gmail.com";

    private static final String[][] FAQ = {
            {"How does a quiz work?",
                    "Each quiz has 10 questions and you get 30 seconds for each one. "
                            + "Choose a language, then pick Multiple Choices, Fill in the Blank, or Identification."},
            {"How do hints work?",
                    "You get 3 hints per quiz, and only 1 hint for each question. "
                            + "Tap the hint button before you answer."},
            {"How is my streak counted?",
                    "Your streak goes up by 1 on each day you open the app. "
                            + "If you skip a day, it starts again at 1."},
            {"How is my progress computed?",
                    "For each language, progress is your total correct answers divided by your total "
                            + "questions across all finished quizzes. A quiz you leave halfway is not counted."},
            {"How do I change my name or photo?",
                    "Go to Profile, then tap Edit Profile."},
            {"I forgot my password.",
                    "Log out, then contact support using the button below and we will help you."}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        LinearLayout container = findViewById(R.id.faqContainer);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String[] item : FAQ) {
            View card = inflater.inflate(R.layout.item_faq, container, false);
            ((TextView) card.findViewById(R.id.tvQuestion)).setText(item[0]);
            ((TextView) card.findViewById(R.id.tvAnswer)).setText(item[1]);
            container.addView(card);
        }

        findViewById(R.id.btnContact).setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
            email.putExtra(Intent.EXTRA_SUBJECT, "DevQuest Support");
            try {
                startActivity(email);
            } catch (Exception e) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
