package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ElegirRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_registro);
    }
    public void paciente(View view){
        Intent i = new Intent(this, RegistroPaciente.class );
        startActivity(i);
    }
    public void especialista(View view){
        Intent i = new Intent(this, RegistroEspecialista.class );
        startActivity(i);
    }
}