package com.example.citasvidasana;

import androidx.annotation.NonNull;
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


public class DrawerBaseEspecialista extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base_especialista, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBarEsp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view_especialista);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view_especialista);
        navigationView.setNavigationItemSelectedListener(this);
        switch (item.getItemId()){
            case R.id.nav_perfil_esp:
                navigationView.setCheckedItem(R.id.nav_perfil_esp);
                startActivity(new Intent(this,PerfilEspecialista.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_subir_resultados:
                navigationView.setCheckedItem(R.id.nav_subir_resultados);
                startActivity(new Intent(this,SubirResultados.class));
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.nav_salir:
                navigationView.setCheckedItem(R.id.nav_salir);
                startActivity(new Intent(this, IniciarSesion.class));
                overridePendingTransition(0,0);
                finish();
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(titleString);
        }
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_base_especialista);
    }*/
}