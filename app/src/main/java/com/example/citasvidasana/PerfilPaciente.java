package com.example.citasvidasana;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.citasvidasana.databinding.ActivityBienvenidaPacienteBinding;
import com.example.citasvidasana.databinding.ActivityPerfilPacienteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilPaciente extends DrawerBasePaciente {

    ActivityPerfilPacienteBinding activityPerfilPacienteBinding;
    private EditText etNombre, etApellidop, etApellidom, etTelefono, etCorreo, etPass, etCpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPerfilPacienteBinding = ActivityPerfilPacienteBinding.inflate(getLayoutInflater());
        setContentView(activityPerfilPacienteBinding.getRoot());
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellidop = (EditText) findViewById(R.id.etApellidop);
        etApellidom = (EditText) findViewById(R.id.etApellidom);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etPass = (EditText) findViewById(R.id.etPass);
        etCpass = (EditText) findViewById(R.id.etCpass);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Usuarios").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // El documento del usuario existe, obtiene los datos y muestra el perfil
                    String nombre = document.getString("Nombre");
                    String apellidop = document.getString("Apellidop");
                    String apellidom = document.getString("Apellidom");
                    String tel = document.getString("Telefono");
                    String correo = document.getString("Correo");
                    String pass = document.getString("Pass");

                    // Muestra los datos en la interfaz de usuario
                    etNombre.setText(nombre);
                    etApellidop.setText(apellidop);
                    etApellidom.setText(apellidom);
                    etTelefono.setText(tel);
                    etCorreo.setText(correo);
                    etPass.setText(pass);
                } else {
                    // El documento del usuario no existe
                }
            } else {
                // Error al obtener el documento del usuario
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = etNombre.getText().toString();
                String ap = etApellidop.getText().toString();
                String am = etApellidom.getText().toString();
                String tel = etTelefono.getText().toString();
                String corr = etCorreo.getText().toString();
                String passw = etPass.getText().toString();
                String cpassw = etCpass.getText().toString();

                if (nom.equals("")) {
                    Toast.makeText(getApplicationContext(), "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
                } else if (ap.equals("")) {
                    Toast.makeText(getApplicationContext(), "El apellido paterno no puede quedar vacio", Toast.LENGTH_SHORT).show();
                } else if (am.equals("")) {
                    Toast.makeText(getApplicationContext(), "El apellido materno no puede quedar vacio", Toast.LENGTH_SHORT).show();
                } else if (tel.equals("")) {
                    Toast.makeText(getApplicationContext(), "El telefono no puede quedar vacio", Toast.LENGTH_SHORT).show();
                } else if (tel.length() < 10) {
                    Toast.makeText(getApplicationContext(), "Ingrese un número telefónico válido", Toast.LENGTH_SHORT).show();
                } else if (corr.equals("")) {
                    Toast.makeText(getApplicationContext(), "El correo no puede quedar vacio", Toast.LENGTH_SHORT).show();
                } else if (validarCorreo(corr)) {
                    if (passw.equals("")) {
                        Toast.makeText(getApplicationContext(), "La contraseña no puede quedar vacia", Toast.LENGTH_SHORT).show();
                    } else if (passw.length() < 8) {
                        Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 8 carácteres", Toast.LENGTH_SHORT).show();
                    } else if (cpassw.equals("")) {
                        Toast.makeText(getApplicationContext(), "Confirma la contraseña", Toast.LENGTH_SHORT).show();
                    } else if (cpassw.equals(passw)) {

                        DocumentReference usuarioRef = db.collection("Usuarios").document(userId);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //String newPass = cpassw;


                        usuarioRef.update("Nombre", nom, "Apellidop", ap, "Apellidom", am,
                                        "Telefono", tel, "Correo", corr, "Pass", passw)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        //Actualizar correo
                        user.updateEmail(corr)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User email address updated.");
                                        }
                                    }
                                });
                        //Actualizar contraseña
                        user.updatePassword(cpassw)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese un correo electronico valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validarCorreo(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }
}