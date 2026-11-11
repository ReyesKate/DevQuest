package com.example.devquest;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.devquest.Activities.SetsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView greeting, initial;
    private ImageView avatarPhoto;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        greeting = view.findViewById(R.id.greetingText);
        initial = view.findViewById(R.id.avatarInitial);
        avatarPhoto = view.findViewById(R.id.avatarPhoto);

        greeting.setText(R.string.greeting_default);
        initial.setText("");

        view.findViewById(R.id.cplus).setOnClickListener(v -> openSets("C++"));
        view.findViewById(R.id.java).setOnClickListener(v -> openSets("Java"));
        view.findViewById(R.id.csharp).setOnClickListener(v -> openSets("C#"));
        view.findViewById(R.id.javascript).setOnClickListener(v -> openSets("JavaScript"));

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> confirmLogout());
    }

    @Override
    public void onResume() {
        super.onResume();

        UserRepository.loadProfile(profile -> {
            if (!isAdded()) return;
            UserRepository.updateStreak();

            String firstName = profile.fullName.trim().split("\\s+")[0];
            greeting.setText(getString(R.string.greeting_name, firstName));

            Bitmap bmp = PhotoUtil.base64ToBitmap(profile.photoBase64);
            if (bmp != null) {
                avatarPhoto.setImageBitmap(bmp);
                avatarPhoto.setVisibility(View.VISIBLE);
                initial.setVisibility(View.GONE);
            } else {
                avatarPhoto.setVisibility(View.GONE);
                initial.setVisibility(View.VISIBLE);
                initial.setText(firstName.substring(0, 1).toUpperCase(Locale.getDefault()));
            }
        });
    }

    private void openSets(String language) {
        Intent i = new Intent(requireContext(), SetsActivity.class);
        i.putExtra("language", language);
        startActivity(i);
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(requireContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}