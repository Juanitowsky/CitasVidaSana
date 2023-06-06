package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.citasvidasana.databinding.ActivityBienvenidaEspecialistaBinding;

public class BienvenidaEspecialista extends DrawerBaseEspecialista {

    ActivityBienvenidaEspecialistaBinding activityBienvenidaEspecialistaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBienvenidaEspecialistaBinding = ActivityBienvenidaEspecialistaBinding.inflate(getLayoutInflater());
        setContentView(activityBienvenidaEspecialistaBinding.getRoot());
        //allocateActivityTitle("Perfíl");
    }
}