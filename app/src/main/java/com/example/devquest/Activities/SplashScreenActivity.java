package com.example.devquest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.devquest.Homepage;
import com.example.devquest.MainActivity;
import com.example.devquest.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    private static final long LOGO_DURATION_MS = 900;
    private static final long TOTAL_SPLASH_MS = 3000;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable goNext = this::openNextScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        playAnimation();

        handler.postDelayed(goNext, TOTAL_SPLASH_MS);
    }

    private void playAnimation() {
        View logo = findViewById(R.id.splashLogo);

        logo.setAlpha(0f);
        logo.setScaleX(0.8f);
        logo.setScaleY(0.8f);

        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(LOGO_DURATION_MS)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();
    }

    private void openNextScreen() {
        boolean loggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;

        Class<?> target = loggedIn ? Homepage.class : MainActivity.class;
        startActivity(new Intent(SplashScreenActivity.this, target));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(goNext);
    }
}