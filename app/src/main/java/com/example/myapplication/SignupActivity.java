package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.UserDatabaseHelper;

public class SignupActivity extends AppCompatActivity {

    EditText mail, pass, rePass;
    UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mail = findViewById(R.id.etMail);
        pass = findViewById(R.id.etPassSignup);
        rePass = findViewById(R.id.etRePass);

        dbHelper = new UserDatabaseHelper(this);
    }

    public void signup(View view) {
        String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String rePassword = rePass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (dbHelper.checkEmailExists(email)) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = dbHelper.addUser(email, password);
            if (success) {
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void linkpressed(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}