package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class PanicFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private UserDatabaseHelper dbHelper;


    public PanicFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panic, container, false);

        dbHelper = new UserDatabaseHelper(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        Button panicButton = view.findViewById(R.id.btnPanic);
        panicButton.setOnClickListener(v -> {
            if (checkPermissions()) {
                sendLocationToContacts();
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, PERMISSION_REQUEST_CODE);
            }
        });

        return view;
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void sendLocationToContacts() {
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    String message = "🚨 EMERGENCY! I need help. My location is: " + lat + " ," + lon;
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT phone_number FROM contacts", null);

                    if (cursor.moveToFirst()) {
                        int count = 0;
                        do {
                            String number = cursor.getString(0);
                            if (number != null && !number.trim().isEmpty()) {
                                SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
                                Log.d("SOS", "Sent SMS to: " + number);
                                count++;
                            }
                        } while (cursor.moveToNext());

                        Toast.makeText(getContext(), "Emergency SMS sent to "+count + " contacts!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No contacts found in database.", Toast.LENGTH_SHORT).show();
                    }

                    cursor.close();
                    db.close();
                } else {
                    Toast.makeText(getContext(), "Failed to get location. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Permissions missing!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (checkPermissions()) {
                sendLocationToContacts();
            } else {
                Toast.makeText(getContext(), "Permissions denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
