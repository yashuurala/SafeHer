package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SafeHerUsers.db";
    private static final int DB_VERSION = 2;

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE," +
                "password TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS contacts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "name TEXT," +
                "phone_number TEXT," +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE);");

        Log.d("Database", "Tables created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    // ✅ Add a new user
    public boolean addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        long result = db.insert("users", null, cv);
        db.close();
        return result != -1;
    }

    // ✅ Add contact for a specific user
    public boolean addContact(String name, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone_number", phoneNumber);
        long result = db.insert("contacts", null, cv);
        db.close();
        return result != -1;
    }

    // ✅ Get contacts for a specific user
    // ✅ Get all contacts from the contacts table
    public ArrayList<String> getAllContacts() {
        ArrayList<String> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, phone_number FROM contacts", null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int phoneIndex = cursor.getColumnIndexOrThrow("phone_number");

            do {
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);
                contacts.add(name + " - " + phone);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contacts;
    }
    public ArrayList<String> getAllContactNumbers() {
        ArrayList<String> numbers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT phone_number FROM contacts", null);

        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(0);
                numbers.add(number);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return numbers;
    }



    // ✅ Delete contact by phone number and user ID
    public boolean deleteContactByPhone(String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("contacts", "phone_number = ?", new String[]{phoneNumber});
        return rows > 0;
    }

    // ✅ Check if email exists
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ✅ Validate login
    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // ✅ Get user ID by email
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email=?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }
}
