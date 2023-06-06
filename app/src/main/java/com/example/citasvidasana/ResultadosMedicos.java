package com.example.citasvidasana;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.citasvidasana.databinding.ActivityResultadosMedicosBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ResultadosMedicos extends DrawerBasePaciente {

    ActivityResultadosMedicosBinding activityResultadosMedicosBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerViewResultados;
    AdapterResultados mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultadosMedicosBinding = ActivityResultadosMedicosBinding.inflate(getLayoutInflater());
        setContentView(activityResultadosMedicosBinding.getRoot());

        recyclerViewResultados = findViewById(R.id.recyclerViewCitas);
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));
        mFirestore = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DocumentReference userRef = db.collection("Usuarios").document(userId);
        CollectionReference resultadosRef = userRef.collection("Resultados");
        Query query = resultadosRef;
        FirestoreRecyclerOptions<LlamarResultados> firestoreRecyclerOptions
                = new FirestoreRecyclerOptions.Builder<LlamarResultados>().setQuery(query,
                LlamarResultados.class).build();

        mAdapter = new AdapterResultados(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        recyclerViewResultados.setAdapter(mAdapter);
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