package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button btnProfile = view.findViewById(R.id.btnAddRem);
        Button btnEmailSettings = view.findViewById(R.id.btnEmailSettings);
        Button btnPassword = view.findViewById(R.id.btnPas);
        Button btnLogout = view.findViewById(R.id.btnSet);

        // Open ProfileActivity for Add/Remove Contacts
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = requireActivity().getIntent().getIntExtra("USER_ID", -1);
                Intent intent = new Intent(requireContext(), ProfileActivity.class);
                intent.putExtra("USER_ID", userId);  // 🔥 This is key
                startActivity(intent);
            }
        });


        // Open ChangePassEmailActivity for email change
        btnEmailSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChangePassEmailActivity.class);
                intent.putExtra("mode", "email");
                startActivity(intent);
            }
        });

        // Open ChangePassEmailActivity for password change
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChangePassEmailActivity.class);
                intent.putExtra("mode", "password");
                startActivity(intent);
            }
        });

        // Logout User (Redirect to MainActivity)
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
