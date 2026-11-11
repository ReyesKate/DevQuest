package com.example.devquest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProgressFragment extends Fragment {

    private TextView tvQuizzes, tvCorrect, tvStreak;
    private ProgressBar barCpp, barJava, barCsharp, barJs;
    private TextView tvPercentCpp, tvPercentJava, tvPercentCsharp, tvPercentJs;

    public ProgressFragment() {
        super(R.layout.fragment_progress);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQuizzes = view.findViewById(R.id.tvQuizzes);
        tvCorrect = view.findViewById(R.id.tvCorrect);
        tvStreak = view.findViewById(R.id.tvStreak);

        barCpp = view.findViewById(R.id.barCpp);
        barJava = view.findViewById(R.id.barJava);
        barCsharp = view.findViewById(R.id.barCsharp);
        barJs = view.findViewById(R.id.barJs);

        tvPercentCpp = view.findViewById(R.id.tvPercentCpp);
        tvPercentJava = view.findViewById(R.id.tvPercentJava);
        tvPercentCsharp = view.findViewById(R.id.tvPercentCsharp);
        tvPercentJs = view.findViewById(R.id.tvPercentJs);

        ImageButton back = view.findViewById(R.id.btnBack);
        back.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    @Override
    public void onResume() {
        super.onResume();

        UserRepository.loadStats(stats -> {
            if (!isAdded()) return;

            tvQuizzes.setText(String.valueOf(stats.quizzesTaken));
            tvCorrect.setText(String.valueOf(stats.correctAnswers));
            tvStreak.setText(String.valueOf(stats.streak));

            show(barCpp, tvPercentCpp, stats.percent.get("cpp"));
            show(barJava, tvPercentJava, stats.percent.get("java"));
            show(barCsharp, tvPercentCsharp, stats.percent.get("csharp"));
            show(barJs, tvPercentJs, stats.percent.get("javascript"));
        });
    }

    private void show(ProgressBar bar, TextView label, Integer percent) {
        int p = percent == null ? 0 : percent;
        bar.setProgress(p);
        label.setText(p + "%");
    }
}
