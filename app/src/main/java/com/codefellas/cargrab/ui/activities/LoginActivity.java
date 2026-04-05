package com.codefellas.cargrab.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codefellas.cargrab.R;
import com.codefellas.cargrab.data.Admin;
import com.codefellas.cargrab.data.Database;
import com.codefellas.cargrab.data.Driver;
import com.codefellas.cargrab.data.Passenger;
import com.codefellas.cargrab.databinding.ActivityLoginBinding;
import com.codefellas.cargrab.util.NotificationUtil;
import com.codefellas.cargrab.util.PreferenceUtil;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Database database;
    private PreferenceUtil pref;

    private final ActivityResultLauncher<String> requestNotificationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notification permission declined", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        database = new Database(this);
        pref = new PreferenceUtil(this);
        NotificationUtil.createChannels(this);
        requestionNotificationPermission();

        binding.login.setOnClickListener(view -> {
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all forms", Toast.LENGTH_SHORT).show();
                return;
            }

            if (binding.loginAs.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select if you're a passenger, driver or admin", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedRole = binding.loginAs.getText().toString().trim();
            switch (selectedRole) {
                case "Passenger":
                    Passenger loginPassenger = database.loginPassenger(email, password);
                    if (loginPassenger == null) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        binding.emailInput.setText("");
                        binding.passwordInput.setText("");
                        return;
                    }

                    Toast.makeText(this, "Logged in as passenger", Toast.LENGTH_SHORT).show();
                    pref.setRole(selectedRole);
                    pref.setAccountID(loginPassenger.getPassengerID());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case "Driver":
                    Driver loginDriver = database.loginDriver(email, password);
                    if (loginDriver == null) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        binding.emailInput.setText("");
                        binding.passwordInput.setText("");
                        return;
                    }

                    Toast.makeText(this, "Logged in as driver", Toast.LENGTH_SHORT).show();
                    pref.setRole(selectedRole);
                    pref.setAccountID(loginDriver.getDriverID());
                    startActivity(new Intent(LoginActivity.this, DriverActivity.class));
                    finish();
                    break;
                case "Admin":
                    if (email.equals("admin01@gmail.com") && password.equals("adminpassword")) {
                        Toast.makeText(this, "Logged in as admin", Toast.LENGTH_SHORT).show();
                        pref.setRole(selectedRole);
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        finish();
                    }
                    break;
            }
        });

        binding.registerPassenger.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterPassengerActivity.class)));
        binding.registerDriver.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterDriverActivity.class)));
    }

    private void requestionNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}