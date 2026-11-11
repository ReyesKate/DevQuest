package com.example.devquest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupName, signupEmail, signupPassword, signupConfirm;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_pass);
        signupConfirm = findViewById(R.id.signup_confirm_pass);
        signupButton = findViewById(R.id.signup_button);
        TextView loginRedirectText = findViewById(R.id.loginRedirectText);

        PasswordToggle.attach(signupPassword);
        PasswordToggle.attach(signupConfirm);

        signupButton.setOnClickListener(v -> signUp());

        loginRedirectText.setOnClickListener(v ->
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void signUp() {
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String pass = signupPassword.getText().toString().trim();
        String confirm = signupConfirm.getText().toString().trim();

        if (name.isEmpty()) {
            signupName.setError("Full name cannot be empty");
            return;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Please enter a valid email");
            return;
        }
        if (pass.length() < 6) {
            signupPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!pass.equals(confirm)) {
            signupConfirm.setError("Passwords do not match");
            return;
        }

        signupButton.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                signupButton.setEnabled(true);
                String reason = task.getException() != null ? task.getException().getMessage() : "";
                Toast.makeText(SignUpActivity.this, "SignUp Failed: " + reason, Toast.LENGTH_SHORT).show();
                return;
            }

            UserRepository.createProfile(name, email).addOnCompleteListener(saveTask -> {
                if (saveTask.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Account created, but profile was not saved", Toast.LENGTH_SHORT).show();
                }

                auth.signOut();

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            });
        });
    }
}
