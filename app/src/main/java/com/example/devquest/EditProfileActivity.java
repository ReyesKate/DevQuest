package com.example.devquest;

import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivPhoto;
    private TextView tvInitial, tvEmail;
    private EditText etName;
    private Button btnSave;

    private String newPhotoBase64 = null;

    private final ActivityResultLauncher<PickVisualMediaRequest> picker =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri == null) return;

                String base64 = PhotoUtil.uriToBase64(this, uri);
                if (base64 == null) {
                    Toast.makeText(this, "Could not read that photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                newPhotoBase64 = base64;
                showPhoto(base64);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivPhoto = findViewById(R.id.ivPhoto);
        tvInitial = findViewById(R.id.tvInitial);
        tvEmail = findViewById(R.id.tvEmail);
        etName = findViewById(R.id.etName);
        btnSave = findViewById(R.id.btnSave);

        FrameLayout avatarFrame = findViewById(R.id.avatarFrame);
        avatarFrame.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        avatarFrame.setClipToOutline(true);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        View.OnClickListener choosePhoto = v -> picker.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
        avatarFrame.setOnClickListener(choosePhoto);
        findViewById(R.id.tvChangePhoto).setOnClickListener(choosePhoto);

        btnSave.setEnabled(false);
        btnSave.setAlpha(0.5f);
        btnSave.setOnClickListener(v -> save());

        UserRepository.loadProfile(profile -> {
            if (isFinishing() || isDestroyed()) return;

            etName.setText(profile.fullName);
            tvEmail.setText(profile.email);
            showPhoto(profile.photoBase64);

            btnSave.setEnabled(true);
            btnSave.setAlpha(1f);
        });
    }

    private void showPhoto(String base64) {
        Bitmap bmp = PhotoUtil.base64ToBitmap(base64);
        if (bmp != null) {
            ivPhoto.setImageBitmap(bmp);
            ivPhoto.setVisibility(View.VISIBLE);
            tvInitial.setVisibility(View.GONE);
        } else {
            ivPhoto.setVisibility(View.GONE);
            String name = etName.getText().toString().trim();
            tvInitial.setText(name.isEmpty() ? "" : name.substring(0, 1).toUpperCase());
            tvInitial.setVisibility(View.VISIBLE);
        }
    }

    private void save() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Full name cannot be empty");
            return;
        }

        btnSave.setEnabled(false);

        UserRepository.updateProfile(name, newPhotoBase64).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                btnSave.setEnabled(true);
                Toast.makeText(this, "Could not save. Check your internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}