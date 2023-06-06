package com.example.citasvidasana;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdapterCitas extends FirestoreRecyclerAdapter<LlamarCitas, AdapterCitas.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterCitas(@NonNull FirestoreRecyclerOptions<LlamarCitas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull LlamarCitas model) {
        holder.tvTipoCita.setText(model.getTipoCita());
        holder.tvFecha.setText(model.getFecha());
        holder.tvHora.setText(model.getHora());
        holder.tvEspecialista.setText(model.getEspecialista());

        holder.btnActualizar.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String userId = currentUser.getUid();
            // Obtén los datos necesarios del modelo
            String tipoCita = model.getTipoCita();
            String fecha = model.getFecha();
            String hora = model.getHora();
            String especialista = model.getEspecialista();

            // Crea una referencia al documento en la colección correspondiente
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference citasRef = db.collection("Usuarios").document(userId).collection("Citas");

            // Realiza la consulta para obtener el documento correspondiente a los datos
            Query query = citasRef.whereEqualTo("TipoCita", tipoCita)
                    .whereEqualTo("Fecha", fecha)
                    .whereEqualTo("Hora", hora)
                    .whereEqualTo("Especialista", especialista);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Obtén el primer documento que cumple con los criterios de búsqueda
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                        // Obtén los datos del documento
                        String itemId = documentSnapshot.getId();
                        String tipoCitaa = documentSnapshot.getString("TipoCita");
                        String fechac = documentSnapshot.getString("Fecha");
                        String horac = documentSnapshot.getString("Hora");
                        String especialistac = documentSnapshot.getString("Especialista");

                        // Crea un Intent para iniciar la nueva actividad
                        Intent intent = new Intent(holder.itemView.getContext(), ReagendarCita.class);

                        // Puedes pasar los datos necesarios a través del Intent
                        intent.putExtra("itemId", itemId);
                        intent.putExtra("tipoCita", tipoCitaa);
                        intent.putExtra("fecha", fechac);
                        intent.putExtra("hora", horac);
                        intent.putExtra("especialista", especialistac);
                        // Agrega más extras según los datos que necesites pasar

                        // Iniciar la nueva actividad
                        holder.itemView.getContext().startActivity(intent);
                    } else {
                        // No se encontró ningún documento que coincida con los criterios de búsqueda
                        Toast.makeText(holder.itemView.getContext(), "No se encontró el documento", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Ocurrió un error al realizar la consulta
                    Toast.makeText(holder.itemView.getContext(), "Error al realizar la consulta", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.citasentry, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTipoCita;
        TextView tvFecha;
        TextView tvHora;
        TextView tvEspecialista;

        Button btnActualizar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTipoCita = itemView.findViewById(R.id.Cita);
            tvFecha = itemView.findViewById(R.id.Fecha);
            tvHora = itemView.findViewById(R.id.Hora);
            tvEspecialista = itemView.findViewById(R.id.Especialista);
            btnActualizar = itemView.findViewById(R.id.btnActualizar);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
