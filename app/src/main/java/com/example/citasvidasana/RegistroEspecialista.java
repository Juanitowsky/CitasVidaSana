package com.example.citasvidasana;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroEspecialista extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Declaracion de variables
    private EditText etNombre, etApellidop, etApellidom, etTelefono, etCorreo, etCedula, etPass, etCpass;
    private Spinner spEspecialidad;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_especialista);
        //Variables referenciando a los objetos graficos
        etNombre=(EditText)findViewById(R.id.etNombre);
        etApellidop=(EditText)findViewById(R.id.etApellidop);
        etApellidom=(EditText)findViewById(R.id.etApellidom);
        etTelefono=(EditText)findViewById(R.id.etTelefono);
        etCorreo=(EditText)findViewById(R.id.etCorreo);
        etCedula=(EditText)findViewById(R.id.etCedula);
        etPass=(EditText)findViewById(R.id.etPass);
        etCpass=(EditText)findViewById(R.id.etCpass);
        spEspecialidad = (Spinner) findViewById(R.id.spEspecialidad);
        String[]opciones={"Seleccione","Urólogo","Hematólogo", "Microbiologo", "Metabolopatias"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opciones);
        spEspecialidad.setAdapter(adapter);
        Bundle bundle=getIntent().getExtras();

        etNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etNombre.getText().toString();
                    String capitalizedText = text.substring(0, 1).toUpperCase() + text.substring(1);
                    etNombre.setText(capitalizedText);
                }
            }
        });
        etApellidop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etApellidop.getText().toString();
                    String capitalizedText = text.substring(0, 1).toUpperCase() + text.substring(1);
                    etApellidop.setText(capitalizedText);
                }
            }
        });
        etApellidom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String text = etApellidom.getText().toString();
                    String capitalizedText = text.substring(0, 1).toUpperCase() + text.substring(1);
                    etApellidom.setText(capitalizedText);
                }
            }
        });
    }
    public void guardar(View view){
        String nom = etNombre.getText().toString();
        String ap = etApellidop.getText().toString();
        String am = etApellidom.getText().toString();
        String tel = etTelefono.getText().toString();
        String corr = etCorreo.getText().toString();
        String esp = spEspecialidad.getSelectedItem().toString();
        String ced = etCedula.getText().toString();
        String passw = etPass.getText().toString();
        String cpassw = etCpass.getText().toString();

        if(nom.equals("")){
            Toast.makeText(this, "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
        }else if (ap.equals("")) {
            Toast.makeText(this, "El apellido paterno no puede quedar vacio", Toast.LENGTH_SHORT).show();
        }else if (am.equals("")) {
            Toast.makeText(this, "El apellido materno no puede quedar vacio", Toast.LENGTH_SHORT).show();
        }else if (tel.equals("")) {
            Toast.makeText(this, "El telefono no puede quedar vacio", Toast.LENGTH_SHORT).show();
        }else if (tel.length()<10) {
            Toast.makeText(this, "Ingrese un número telefónico válido", Toast.LENGTH_SHORT).show();
        }else if (corr.equals("")) {
            Toast.makeText(this, "El correo no puede quedar vacio", Toast.LENGTH_SHORT).show();
        }else if(validarCorreo(corr)){
            if (esp.equals("Seleccione")) {
                Toast.makeText(this, "Seleccione su especialidad", Toast.LENGTH_SHORT).show();
            }else if (ced.equals("")) {
                Toast.makeText(this, "La cedula no puede quedar vacia", Toast.LENGTH_SHORT).show();
            }else if (ced.length()<10) {
                Toast.makeText(this, "Ingrese una cedula válida", Toast.LENGTH_SHORT).show();
            }else if (passw.equals("")) {
                Toast.makeText(this, "La contraseña no puede quedar vacia", Toast.LENGTH_SHORT).show();
            }else if (passw.length()<8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 carácteres", Toast.LENGTH_SHORT).show();
            }else if (cpassw.equals("")) {
                Toast.makeText(getApplicationContext(), "Confirma la contraseña", Toast.LENGTH_SHORT).show();
            }else if (cpassw.equals(passw)) {
                mAuth.createUserWithEmailAndPassword(corr,passw)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser uEspecialista = mAuth.getCurrentUser();
                                    if (uEspecialista != null) {
                                        String userId = uEspecialista.getUid();
                                        DocumentReference userRef = db.collection("Especialistas").document(userId);
                                        // Create a new user with a first and last name
                                        Map<String, Object> especialista = new HashMap<>();
                                        //especialista.put("userId", userId);
                                        especialista.put("Nombre", nom);
                                        especialista.put("Apellidop", ap);
                                        especialista.put("Apellidom", am);
                                        especialista.put("Telefono", tel);
                                        especialista.put("Correo", corr);
                                        especialista.put("Especialidad",esp);
                                        especialista.put("Cedula",ced);
                                        especialista.put("Pass", passw);

                                        userRef.set(especialista)
                                                .addOnSuccessListener(aVoid ->{

                                                })
                                                .addOnFailureListener(e -> {
                                                    // Error al guardar los datos del usuario
                                                });

                                        Toast.makeText(getApplicationContext(), "Te has registrado con éxito", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), IniciarSesion.class);
                                        startActivity(i);
                                        finish();
                                        //updateUI(user);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese un correo electronico valido", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean validarCorreo(String email){
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }
}