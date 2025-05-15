
package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<String> contactList;
    private ArrayAdapter<String> adapter;
    private ListView contactListView;
    private ImageView emptyImageView;
    private Button addContactButton, removeContactButton, backButton;
    private UserDatabaseHelper dbHelper;

    // ✅ Class-level variable to track which contact is selected
    private String selectedPhone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new UserDatabaseHelper(this);

        contactListView = findViewById(R.id.contactListView);
        emptyImageView = findViewById(R.id.ivEmptyContacts);
        addContactButton = findViewById(R.id.addContactButton);
        removeContactButton = findViewById(R.id.removeContactButton);
        backButton = findViewById(R.id.backButton);

        contactList = new ArrayList<>();

        // ✅ Use activated layout and enable single selection mode
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, contactList);
        contactListView.setAdapter(adapter);
        contactListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        loadContactsFromDB();

        addContactButton.setOnClickListener(v -> showAddContactDialog());

        // ✅ Set click listener for contact selection
        contactListView.setOnItemClickListener((parent, view, position, id) -> {
            String contact = contactList.get(position);
            String[] parts = contact.split("\\s*-\\s*"); // handles spaces around dash
            if (parts.length == 2) {
                selectedPhone = parts[1]; // Store selected phone number
                contactListView.setItemChecked(position, true); // highlight selection
            }
        });

        // ✅ Remove selected contact when button clicked
        removeContactButton.setOnClickListener(v -> {
            if (selectedPhone != null) {
                boolean deleted = dbHelper.deleteContactByPhone(selectedPhone);
                if (deleted) {
                    Toast.makeText(this, "Contact removed", Toast.LENGTH_SHORT).show();
                    selectedPhone = null;
                    loadContactsFromDB();
                } else {
                    Toast.makeText(this, "Failed to remove contact", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select a contact first", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MainpageActivity.class));
        });
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contact");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputName = new EditText(this);
        inputName.setHint("Enter Name");
        layout.addView(inputName);

        EditText inputNumber = new EditText(this);
        inputNumber.setHint("Enter 10-digit Phone Number");
        inputNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        inputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        layout.addView(inputNumber);

        builder.setView(layout);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String number = inputNumber.getText().toString().trim();

            if (!name.isEmpty() && isValidPhoneNumber(number)) {
                boolean success = dbHelper.addContact(name, number);
                if (success) {
                    Toast.makeText(this, "Contact added!", Toast.LENGTH_SHORT).show();
                    loadContactsFromDB();
                } else {
                    Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter a valid 10-digit number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void loadContactsFromDB() {
        contactList.clear();
        contactList.addAll(dbHelper.getAllContacts());
        adapter.notifyDataSetChanged();
        updateUI();
    }

    private void updateUI() {
        if (contactList.isEmpty()) {
            emptyImageView.setVisibility(View.VISIBLE);
            contactListView.setVisibility(View.GONE);
        } else {
            emptyImageView.setVisibility(View.GONE);
            contactListView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidPhoneNumber(String number) {
        return number.matches("\\d{10}");
    }
}

