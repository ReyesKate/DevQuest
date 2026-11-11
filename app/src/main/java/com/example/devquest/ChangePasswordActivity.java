package com.example.devquest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePassword";

    private EditText etCurrent, etNew, etConfirm;
    private TextView errCurrent, errNew, errConfirm;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCurrent = findViewById(R.id.etCurrent);
        etNew = findViewById(R.id.etNew);
        etConfirm = findViewById(R.id.etConfirm);
        errCurrent = findViewById(R.id.errCurrent);
        errNew = findViewById(R.id.errNew);
        errConfirm = findViewById(R.id.errConfirm);
        btnSave = findViewById(R.id.btnSave);

        PasswordToggle.attach(etCurrent);
        PasswordToggle.attach(etNew);
        PasswordToggle.attach(etConfirm);

        clearOnType(etCurrent, errCurrent);
        clearOnType(etNew, errNew);
        clearOnType(etConfirm, errConfirm);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        clearErrors();

        String current = etCurrent.getText().toString();
        String newPass = etNew.getText().toString();
        String confirm = etConfirm.getText().toString();

        if (current.isEmpty()) {
            showError(etCurrent, errCurrent, "Enter your current password");
            return;
        }
        if (newPass.length() < 6) {
            showError(etNew, errNew, "Password must be at least 6 characters");
            return;
        }
        if (newPass.equals(current)) {
            showError(etNew, errNew, "New password must be different from the current one");
            return;
        }
        if (!newPass.equals(confirm)) {
            showError(etConfirm, errConfirm, "Passwords do not match");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.getEmail() == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), current);
        user.reauthenticate(credential).addOnCompleteListener(authTask -> {
            if (!authTask.isSuccessful()) {
                setLoading(false);
                Exception e = authTask.getException();
                Log.e(TAG, "reauthenticate failed", e);

                if (e instanceof FirebaseNetworkException) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    showError(etCurrent, errCurrent, "Too many attempts. Try again later.");
                } else {

                    showError(etCurrent, errCurrent, "Current password is incorrect");
                    Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            user.updatePassword(newPass).addOnCompleteListener(updateTask -> {
                setLoading(false);
                if (updateTask.isSuccessful()) {
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Exception e = updateTask.getException();
                    Log.e(TAG, "updatePassword failed", e);
                    String reason = e != null && e.getMessage() != null ? e.getMessage() : "Unknown error";
                    Toast.makeText(this, "Could not change password: " + reason,
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void showError(EditText field, TextView label, String message) {
        label.setText(message);
        label.setVisibility(View.VISIBLE);
        field.requestFocus();
    }

    private void clearErrors() {
        errCurrent.setVisibility(View.GONE);
        errNew.setVisibility(View.GONE);
        errConfirm.setVisibility(View.GONE);
    }

    private void clearOnType(EditText field, TextView label) {
        field.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                label.setVisibility(View.GONE);
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    private void setLoading(boolean loading) {
        btnSave.setEnabled(!loading);
        btnSave.setText(loading ? "Changing..." : "Change Password");
    }
}