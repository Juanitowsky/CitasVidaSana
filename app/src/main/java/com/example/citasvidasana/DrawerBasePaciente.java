package com.example.citasvidasana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBasePaciente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    @Override
    public void setContentView(View view){
        drawerLayout=(DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base_paciente, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view_paciente);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view_paciente);
        navigationView.setNavigationItemSelectedListener(this);
        switch (item.getItemId()){
            case R.id.nav_perfil:
                navigationView.setCheckedItem(R.id.nav_perfil);
                startActivity(new Intent(this, PerfilPaciente.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_calendario:
                navigationView.setCheckedItem(R.id.nav_calendario);
                startActivity(new Intent(this, Calendario.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_citas_programadas:
                navigationView.setCheckedItem(R.id.nav_citas_programadas);
                startActivity(new Intent(this, CitasProgramadas.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_historial:
                navigationView.setCheckedItem(R.id.nav_historial);
                startActivity(new Intent(this, Historial.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_especialistas:
                navigationView.setCheckedItem(R.id.nav_especialistas);
                startActivity(new Intent(this, Especialistas.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_resultados_medicos:
                navigationView.setCheckedItem(R.id.nav_resultados_medicos);
                startActivity(new Intent(this, ResultadosMedicos.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_salir:
                startActivity(new Intent(this, IniciarSesion.class));
                overridePendingTransition(0,0);
                finish();
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}