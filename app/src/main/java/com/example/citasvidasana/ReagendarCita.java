package com.example.citasvidasana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.citasvidasana.databinding.ActivityPerfilPacienteBinding;
import com.example.citasvidasana.databinding.ActivityReagendarCitaBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReagendarCita extends DrawerBasePaciente {

    ActivityReagendarCitaBinding activityReagendarCitaBinding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spnEspecialistas, spnTipoCita, spnHora;
    private String fechaSeleccionada, horaSeleccionada;
    String nombreCompleto;
    private Button btnReagendar, btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReagendarCitaBinding = ActivityReagendarCitaBinding.inflate(getLayoutInflater());
        setContentView(activityReagendarCitaBinding.getRoot());
        CalendarView calendarView = findViewById(R.id.calendarView);
        btnReagendar = findViewById(R.id.btnReagendar);
        spnTipoCita = (Spinner) findViewById(R.id.spnTipoCita);
        String[]opciones={"Seleccione","Análisis Clinicos","Muestras de Sangre", "Analisis de Orina"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opciones);
        spnTipoCita.setAdapter(adapter);
        spnHora = (Spinner) findViewById(R.id.spnHora);
        String[]horas={"Seleccione","7:00 AM","8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, horas);
        spnHora.setAdapter(adapter2);
        spnEspecialistas=findViewById(R.id.spnEspecialista);
        spnEspecialistas.setSelection(0);
        buscarEspecialistasFromFirestore();

        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        String fecha = intent.getStringExtra("fecha");

        //Establecer la fecha en el CalendarView
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(fecha);
            if (date != null) {
                long dateInMillis = date.getTime();
                calendarView.setDate(dateInMillis);
                fechaSeleccionada = sdf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Seleccionar fecha del calendario
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar currentCalendar = Calendar.getInstance();
                int currentYear = currentCalendar.get(Calendar.YEAR);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int currentDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);

                // Compara la fecha seleccionada con la fecha actual
                if (year < currentYear || (year == currentYear && month < currentMonth) || (year == currentYear && month == currentMonth && dayOfMonth < currentDayOfMonth)) {
                    // La fecha seleccionada es anterior a la fecha actual, deshabilita la selección
                    Toast.makeText(ReagendarCita.this, "No puedes seleccionar una fecha anterior a la actual.", Toast.LENGTH_SHORT).show();
                    view.setDate(currentCalendar.getTimeInMillis()); // Establece la fecha actual en el CalendarView
                } else {
                    // La fecha seleccionada es válida
                    fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                }
            }
        });
        btnReagendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectorSpinner selectedUser = (SelectorSpinner) spnEspecialistas.getSelectedItem();
                String tipoCita = spnTipoCita.getSelectedItem().toString();
                horaSeleccionada = spnHora.getSelectedItem().toString();
                if (horaSeleccionada.equals("Seleccione")){
                    Toast.makeText(ReagendarCita.this, "Por favor, seleccione la hora.", Toast.LENGTH_SHORT).show();
                }else if (tipoCita.equals("Seleccione")){
                    Toast.makeText(ReagendarCita.this, "Por favor, seleccione el tipo de cita.", Toast.LENGTH_SHORT).show();
                }else if (selectedUser.getNombreCompleto().equals("Seleccione")) {
                    // El usuario seleccionado es el texto inicial "Seleccione", no realiza ninguna acción
                    Toast.makeText(ReagendarCita.this, "Por favor, seleccione a su médico.", Toast.LENGTH_SHORT).show();
                }else {
                    enviarDatos(itemId,selectedUser, tipoCita);
                }
            }
        });
    }
    private void buscarEspecialistasFromFirestore() {
        CollectionReference usersRef = db.collection("Especialistas");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<SelectorSpinner> userList = new ArrayList<>();

                // Agregar el texto inicial "Seleccione"
                SelectorSpinner opcion = new SelectorSpinner("", "Seleccione");
                userList.add(opcion);

                //Buscar a todos los pacientes y guardarlos para mostrar
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userId = document.getId();
                    String nombre = document.getString("Nombre");
                    String apellidop = document.getString("Apellidop");
                    String apellidom = document.getString("Apellidom");
                    String nombreCompleto = nombre+" "+apellidop+" "+apellidom;
                    SelectorSpinner info = new SelectorSpinner(userId, nombreCompleto);
                    userList.add(info);
                }

                ArrayAdapter<SelectorSpinner> adapter = new ArrayAdapter<>(ReagendarCita.this,
                        android.R.layout.simple_spinner_item, userList);
                spnEspecialistas.setAdapter(adapter);
            } else {

            }
        });
    }

    private void enviarDatos(String documentId, SelectorSpinner selectedUser, String tipoCita) {
        String selectedUserId = selectedUser.getUserId();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DocumentReference citaRef = db.collection("Usuarios").document(userId).collection("Citas").document(documentId);

        if (fechaSeleccionada != null) {
            // Crea un mapa de datos con la fecha y otros campos necesarios
            Map<String, Object> data = new HashMap<>();
            data.put("TipoCita", tipoCita);
            data.put("Fecha", fechaSeleccionada);
            data.put("Hora", horaSeleccionada);
            data.put("Especialista", selectedUser.getNombreCompleto());

            // Guarda los datos en Firebase
            //FirebaseFirestore db = FirebaseFirestore.getInstance();
            //CollectionReference userDataRef = db.collection("Usuarios").document(userId).collection("Citas");
            citaRef.update(data)
                    .addOnSuccessListener(documentReference -> {
                        // Los datos se guardaron exitosamente
                        Toast.makeText(ReagendarCita.this, "¡Tu cita se ha reagendado!", Toast.LENGTH_SHORT).show();
                        spnEspecialistas.setSelection(0);
                        spnTipoCita.setSelection(0);
                        spnHora.setSelection(0);
                        // Limpiar la variable fechaSeleccionada después de guardar los datos
                        fechaSeleccionada = null;
                    })
                    .addOnFailureListener(e -> {
                        // Ocurrió un error al guardar los datos
                        Toast.makeText(ReagendarCita.this, "Hubo un problema al reagendar la cita", Toast.LENGTH_SHORT).show();


                        // Limpiar la variable fechaSeleccionada en caso de error
                        fechaSeleccionada = null;
                    });
        }
    }
}