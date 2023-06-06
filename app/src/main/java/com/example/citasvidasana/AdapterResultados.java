package com.example.citasvidasana;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;



public class AdapterResultados extends FirestoreRecyclerAdapter<LlamarResultados, AdapterResultados.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterResultados(@NonNull FirestoreRecyclerOptions<LlamarResultados> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull LlamarResultados model) {
        String archivoUrl = model.getArchivo();
        holder.tvFecha.setText(model.getFechaEnvio());
        holder.tvEspecialistas.setText(model.getNombreEspecialista());
        holder.tvComentarios.setText(model.getComentarios());
        if (archivoUrl != null && !archivoUrl.isEmpty()) {
            String archivoTexto = "<a href='" + archivoUrl + "'>Descargar archivo</a>";
            holder.tvArchivo.setText(Html.fromHtml(archivoTexto));
            holder.tvArchivo.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            holder.tvArchivo.setText("No hay archivo adjunto");
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resultadosentry, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha;
        TextView tvEspecialistas;
        TextView tvComentarios;

        TextView tvArchivo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFecha = itemView.findViewById(R.id.Fecha);
            tvEspecialistas = itemView.findViewById(R.id.Especialista);
            tvComentarios = itemView.findViewById(R.id.Comentarios);
            tvArchivo = itemView.findViewById(R.id.Archivo);

        }
    }
}
