package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void iniciarS(View view){
        Intent i = new Intent(this, IniciarSesion.class );
        startActivity(i);
    }
    public void registro(View view){
        Intent i = new Intent(this, ElegirRegistro.class );
        startActivity(i);
    }
}