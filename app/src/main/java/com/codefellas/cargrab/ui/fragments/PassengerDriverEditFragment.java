package com.codefellas.cargrab.ui.fragments;

import android.accounts.Account;
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
import com.codefellas.cargrab.data.Driver;
import com.codefellas.cargrab.data.Passenger;
import com.codefellas.cargrab.databinding.FragmentPassengerDriverEditBinding;
import com.codefellas.cargrab.util.PreferenceUtil;

public class PassengerDriverEditFragment extends Fragment {

    FragmentPassengerDriverEditBinding binding;
    PreferenceUtil pref;
    Database database;

    public PassengerDriverEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentPassengerDriverEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = new PreferenceUtil(requireContext());
        database = new Database(requireContext());

        if (pref.getRole().equals("Passenger")) {
            Passenger passenger = database.getPassengerByID(pref.getAccountID());
            setPassengerDetails(passenger);
        }
        else if (pref.getRole().equals("Driver")) {
            //TODO: Dapat pala ibang fragment pag driver kase ibang laman
        }

        binding.saveButton.setOnClickListener(v -> {
            if (binding.password.getText().toString()
                    .equals(binding.confirmPassword.getText().toString()))
                savePassengerDetails();
            else Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setPassengerDetails(Passenger account) {
        binding.passengerFirstName.setText(account.getFirstName());
        binding.passengerMiddleName.setText(account.getMiddleName());
        binding.passengerLastName.setText(account.getLastName());
        binding.passengerEmail.setText(account.getEmail());
        binding.contactNum.setText(account.getPhone());
        binding.password.setText(account.getPassword());
    }

    private void savePassengerDetails() {
        String fName = binding.passengerFirstName.getText().toString();
        String mName = binding.passengerMiddleName.getText().toString();
        String lName = binding.passengerLastName.getText().toString();
        String email = binding.passengerEmail.getText().toString();
        String password = binding.password.getText().toString();
        String phone = binding.contactNum.getText().toString();
        
        database.editPassenger(pref.getAccountID(), fName, mName, lName, email, password, phone);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}