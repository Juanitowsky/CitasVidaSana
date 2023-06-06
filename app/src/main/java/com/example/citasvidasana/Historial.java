package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.citasvidasana.databinding.ActivityHistorialBinding;

public class Historial extends DrawerBasePaciente {

    ActivityHistorialBinding activityHistorialBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistorialBinding = ActivityHistorialBinding.inflate(getLayoutInflater());
        setContentView(activityHistorialBinding.getRoot());
    }
}