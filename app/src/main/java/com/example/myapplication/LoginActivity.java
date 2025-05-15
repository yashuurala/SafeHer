package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.UserDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etMail, etPass;
    UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMail = findViewById(R.id.etMailLogin);
        etPass = findViewById(R.id.etPass);
        dbHelper = new UserDatabaseHelper(this);
    }

    public void login(View view) {
        String email = etMail.getText().toString().trim();
        String password = etPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
        } else {
            boolean isValid = dbHelper.checkEmailPassword(email, password);
            if (isValid) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainpageActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void linkpressed(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }
}

