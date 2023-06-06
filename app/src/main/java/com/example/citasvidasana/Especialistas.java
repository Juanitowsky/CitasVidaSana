package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.example.citasvidasana.databinding.ActivityEspecialistasBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class Especialistas extends DrawerBasePaciente {
    ActivityEspecialistasBinding activityEspecialistasBinding;
    RecyclerView recyclerViewEspecialistas;
    AdapterEspecialistas mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEspecialistasBinding = ActivityEspecialistasBinding.inflate(getLayoutInflater());
        setContentView(activityEspecialistasBinding.getRoot());

        recyclerViewEspecialistas = findViewById(R.id.recyclerViewEspecialistas);
        recyclerViewEspecialistas.setLayoutManager(new LinearLayoutManager(this));
        mFirestore = FirebaseFirestore.getInstance();

        Query query =  mFirestore.collection("Especialistas");

        FirestoreRecyclerOptions<LlamarEspecialistas> firestoreRecyclerOptions
                = new FirestoreRecyclerOptions.Builder<LlamarEspecialistas>().setQuery(query,
                LlamarEspecialistas.class).build();

        mAdapter = new AdapterEspecialistas(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerViewEspecialistas.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}