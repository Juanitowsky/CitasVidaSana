package com.example.citasvidasana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.citasvidasana.databinding.ActivityCalendarioBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Calendario extends DrawerBasePaciente{



    ActivityCalendarioBinding activityCalendarioBinding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spnEspecialistas, spnTipoCita, spnHora;
    private String fechaSeleccionada, horaSeleccionada;
    String nombreComp;
    private Button btnAgendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCalendarioBinding = ActivityCalendarioBinding.inflate(getLayoutInflater());
        setContentView(activityCalendarioBinding.getRoot());
        CalendarView calendarView = findViewById(R.id.calendarView);
        btnAgendar = findViewById(R.id.btnAgendar);
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
        buscarPacientesFromFirestore();

        //Seleccionar la fecha
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
                    Toast.makeText(Calendario.this, "No puedes seleccionar una fecha anterior a la actual.", Toast.LENGTH_SHORT).show();
                    view.setDate(currentCalendar.getTimeInMillis()); // Establece la fecha actual en el CalendarView
                } else {
                    // La fecha seleccionada es válida
                    fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                }
            }
        });

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectorSpinner selectedUser = (SelectorSpinner) spnEspecialistas.getSelectedItem();
                String tipoCita = spnTipoCita.getSelectedItem().toString();
                horaSeleccionada = spnHora.getSelectedItem().toString();
                if (horaSeleccionada.equals("Seleccione")){
                    Toast.makeText(Calendario.this, "Por favor, seleccione la hora.", Toast.LENGTH_SHORT).show();
                }else if (tipoCita.equals("Seleccione")){
                    Toast.makeText(Calendario.this, "Por favor, seleccione el tipo de cita.", Toast.LENGTH_SHORT).show();
                }else if (selectedUser.getNombreCompleto().equals("Seleccione")) {
                    // El usuario seleccionado es el texto inicial "Seleccione", no realiza ninguna acción
                    Toast.makeText(Calendario.this, "Por favor, seleccione a su médico.", Toast.LENGTH_SHORT).show();
                }else {
                    enviarDatos(selectedUser, tipoCita);
                }
            }
        });
    }
    private void buscarPacientesFromFirestore() {
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

                ArrayAdapter<SelectorSpinner> adapter = new ArrayAdapter<>(Calendario.this,
                        android.R.layout.simple_spinner_item, userList);
                spnEspecialistas.setAdapter(adapter);
            } else {

            }
        });
    }

    private void enviarDatos(SelectorSpinner selectedUser, String tipoCita) {
        String selectedUserId = selectedUser.getUserId();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if (fechaSeleccionada != null) {
            // Crea un mapa de datos con la fecha y otros campos necesarios
            Map<String, Object> data = new HashMap<>();
            data.put("TipoCita", tipoCita);
            data.put("Fecha", fechaSeleccionada);
            data.put("Hora", horaSeleccionada);
            data.put("Especialista", selectedUser.getNombreCompleto());

            // Guarda los datos en Firebase
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference userDataRef = db.collection("Usuarios").document(userId).collection("Citas");
            userDataRef.add(data)
                    .addOnSuccessListener(documentReference -> {
                        // Los datos se guardaron exitosamente
                        Toast.makeText(Calendario.this, "Tu cita se ha agendado con éxito", Toast.LENGTH_SHORT).show();
                        spnEspecialistas.setSelection(0);
                        spnTipoCita.setSelection(0);
                        spnHora.setSelection(0);
                        // Limpiar la variable fechaSeleccionada después de guardar los datos
                        fechaSeleccionada = null;
                    })
                    .addOnFailureListener(e -> {
                        // Ocurrió un error al guardar los datos
                        Toast.makeText(Calendario.this, "Error al enviar los datos", Toast.LENGTH_SHORT).show();


                        // Limpiar la variable fechaSeleccionada en caso de error
                        fechaSeleccionada = null;
                    });
        }
    }
}