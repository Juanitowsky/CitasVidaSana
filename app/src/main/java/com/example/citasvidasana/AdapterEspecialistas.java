package com.example.citasvidasana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AdapterEspecialistas extends FirestoreRecyclerAdapter<LlamarEspecialistas, AdapterEspecialistas.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterEspecialistas(@NonNull FirestoreRecyclerOptions<LlamarEspecialistas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull LlamarEspecialistas model) {
        holder.tvNombre.setText(model.getNombre());
        holder.tvApellidop.setText(model.getApellidop());
        holder.tvApellidom.setText(model.getApellidom());
        holder.tvTelefono.setText(model.getTelefono());
        holder.tvCorreo.setText(model.getCorreo());
        holder.tvEspecialidad.setText(model.getEspecialidad());
        holder.tvCedula.setText(model.getCedula());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.especialistaentry, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvApellidop;
        TextView tvApellidom;
        TextView tvTelefono;
        TextView tvCorreo;
        TextView tvEspecialidad;
        TextView tvCedula;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.Nombre);
            tvApellidop = itemView.findViewById(R.id.Apellidop);
            tvApellidom = itemView.findViewById(R.id.Apellidom);
            tvTelefono = itemView.findViewById(R.id.Telefono);
            tvCorreo = itemView.findViewById(R.id.Correo);
            tvEspecialidad = itemView.findViewById(R.id.Especialidad);
            tvCedula = itemView.findViewById(R.id.Cedula);

        }
    }
}
