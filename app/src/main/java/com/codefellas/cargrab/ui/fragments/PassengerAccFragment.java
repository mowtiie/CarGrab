package com.codefellas.cargrab.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codefellas.cargrab.R;
import com.codefellas.cargrab.data.Database;
import com.codefellas.cargrab.data.Passenger;
import com.codefellas.cargrab.databinding.FragmentPassengerAccBinding;
import com.codefellas.cargrab.ui.activities.LoginActivity;
import com.codefellas.cargrab.util.PreferenceUtil;

public class PassengerAccFragment extends Fragment {

    FragmentPassengerAccBinding binding;
    PreferenceUtil pref;
    Database database;

    public PassengerAccFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPassengerAccBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PreferenceUtil(requireContext());
        database = new Database(requireContext());
        System.out.println("Account ID sa pref util: " + pref.getAccountID());
        System.out.println("Check if nagana get passenger: " + database.getPassengerByID(pref.getAccountID()).getFirstName());
        setDetails(database.getPassengerByID(pref.getAccountID()));
        binding.editButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new PassengerDriverEditFragment())
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
        });
        binding.systemButton.setOnClickListener(v -> Toast.makeText(requireContext(), "This section is under maintenance!", Toast.LENGTH_SHORT).show());
        binding.logoutButton.setOnClickListener(v -> startActivity(new Intent(requireContext(), LoginActivity.class)));
    }

    public void setDetails(Passenger passenger) {
        binding.passengerName.setText(String.format("%s %s %s",
                passenger.getFirstName(), passenger.getMiddleName(), passenger.getLastName()));
        binding.passengerEmail.setText(passenger.getEmail());
        binding.contactNum.setText(passenger.getPhone());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}