package com.example.devquest;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail;
    private ImageView ivAvatar;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        ivAvatar = view.findViewById(R.id.ivAvatar);

        tvName.setText("");
        tvEmail.setText("");

        ivAvatar.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View v, Outline outline) {
                outline.setOval(0, 0, v.getWidth(), v.getHeight());
            }
        });
        ivAvatar.setClipToOutline(true);

        view.findViewById(R.id.rowEdit).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), EditProfileActivity.class)));

        view.findViewById(R.id.rowPassword).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ChangePasswordActivity.class)));

        view.findViewById(R.id.rowHelp).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), HelpActivity.class)));

        view.findViewById(R.id.rowLogout).setOnClickListener(v -> confirmLogout());
    }

    @Override
    public void onResume() {
        super.onResume();
        showProfile();
    }

    private void showProfile() {
        UserRepository.loadProfile(profile -> {
            if (!isAdded()) return;
            tvName.setText(profile.fullName);
            tvEmail.setText(profile.email);
            showAvatar(profile.photoBase64);
        });
    }

    private void showAvatar(String base64) {
        Bitmap bmp = PhotoUtil.base64ToBitmap(base64);
        if (bmp != null) {
            ivAvatar.setPadding(0, 0, 0, 0);
            ivAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivAvatar.setImageBitmap(bmp);
        } else {
            int pad = Math.round(20 * getResources().getDisplayMetrics().density);
            ivAvatar.setPadding(pad, pad, pad, pad);
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivAvatar.setImageResource(R.drawable.dq_ic_profile);
        }
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Log Out", (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent i = new Intent(requireContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}

