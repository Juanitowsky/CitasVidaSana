package com.example.citasvidasana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.citasvidasana.databinding.ActivityCitasProgramadasBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CitasProgramadas extends DrawerBasePaciente {

    ActivityCitasProgramadasBinding activityCitasProgramadasBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerViewCitas;
    AdapterCitas mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCitasProgramadasBinding = ActivityCitasProgramadasBinding.inflate(getLayoutInflater());
        setContentView(activityCitasProgramadasBinding.getRoot());
        recyclerViewCitas = findViewById(R.id.recyclerViewCitas);
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
        mFirestore = FirebaseFirestore.getInstance();

        // Obtén los datos necesarios del modelo
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DocumentReference userRef = db.collection("Usuarios").document(userId);
        CollectionReference resultadosRef = userRef.collection("Citas");
        Query query = resultadosRef;
        FirestoreRecyclerOptions<LlamarCitas> firestoreRecyclerOptions
                = new FirestoreRecyclerOptions.Builder<LlamarCitas>().setQuery(query,
                LlamarCitas.class).build();

        mAdapter = new AdapterCitas(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerViewCitas.setAdapter(mAdapter);
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