package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.citasvidasana.databinding.ActivityBienvenidaPacienteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BienvenidaPaciente extends DrawerBasePaciente {
    ActivityBienvenidaPacienteBinding activityBienvenidaPacienteBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBienvenidaPacienteBinding = ActivityBienvenidaPacienteBinding.inflate(getLayoutInflater());
        setContentView(activityBienvenidaPacienteBinding.getRoot());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
    }
}