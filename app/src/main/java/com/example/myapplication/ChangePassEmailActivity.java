package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePassEmailActivity extends AppCompatActivity {

    TextInputLayout t1, t2, t3;
    String mode;
    EditText etM, etNewM;
    Drawable icon, icon1;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_email);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        mode = getIntent().getStringExtra("mode");
        t1 = findViewById(R.id.textInputLayout);
        t2 = findViewById(R.id.textInputLayout1);
        t3 = findViewById(R.id.textInputLayout2);
        etM = t1.getEditText();
        etNewM = t2.getEditText();

        icon = getResources().getDrawable(R.drawable.password);
        icon1 = getResources().getDrawable(R.drawable.mail);

        if (mode.equals("email")) {
            t1.setStartIconDrawable(icon1);
            t2.setStartIconDrawable(icon1);
            t1.setHint("Enter Previous Email");
            t2.setHint("Enter New Email");
            t3.setHint("Enter Password");
            etNewM.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        } else if (mode.equals("password")) {
            t1.setStartIconDrawable(icon);
            t2.setStartIconDrawable(icon);
            t1.setHint("Enter Previous Password");
            t2.setHint("Enter New Password");
            t3.setVisibility(View.INVISIBLE);
            etM.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void changes(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (mode.equals("email")) {
            String currentEmail = t1.getEditText().getText().toString().trim();
            String password = t3.getEditText().getText().toString().trim();
            String newEmail = t2.getEditText().getText().toString().trim();

            String storedEmail = sharedPreferences.getString("email", "yashu@gmail.com");
            String storedPassword = sharedPreferences.getString("password", "123");

            if (currentEmail.equals(storedEmail) && password.equals(storedPassword)) {
                editor.putString("email", newEmail);
                editor.apply();
                Toast.makeText(this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassEmailActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }
        } else if (mode.equals("password")) {
            String currentPassword = t1.getEditText().getText().toString().trim();
            String newPassword = t2.getEditText().getText().toString().trim();

            String storedPassword = sharedPreferences.getString("password", "123");

            if (currentPassword.equals(storedPassword)) {
                editor.putString("password", newPassword);
                editor.apply();
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassEmailActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Incorrect current password!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
