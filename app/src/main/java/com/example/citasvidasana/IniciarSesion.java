package com.example.citasvidasana;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IniciarSesion extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Declaracion de variables
    EditText etCorreo, etPass;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        //Variables referenciando a los objetos graficos
        etCorreo=(EditText)findViewById(R.id.etCorreo);
        etPass=(EditText)findViewById(R.id.etPass);
        checkBox=(CheckBox)findViewById(R.id.checkBox);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String correoGuardado = preferences.getString("correo", "");
        String contrasenaGuardada = preferences.getString("contrasena", "");
        boolean recordar = preferences.getBoolean("recordar", false);
        etCorreo.setText(correoGuardado);
        etPass.setText(contrasenaGuardada);
        checkBox.setChecked(recordar);

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }
    public void ingresar(View view){
        //Almacenamiento de datos en nuevas variables
        String c=etCorreo.getText().toString();
        String p=etPass.getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (checkBox.isChecked()) {
            editor.putString("correo", c);
            editor.putString("contrasena", p);
            editor.putBoolean("recordar", true);
        } else {
            editor.remove("correo");
            editor.remove("contrasena");
            editor.putBoolean("recordar", false);
        }
        editor.apply();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(c.equals("")){
            Toast.makeText(this, "Ingresa un correo para continuar", Toast.LENGTH_SHORT).show();
        }else if (p.equals("")) {
            Toast.makeText(this, "La contraseña no puede quedar vacia", Toast.LENGTH_SHORT).show();
        }else {

            mAuth.signInWithEmailAndPassword(c, p)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // El inicio de sesión fue exitoso, obtén el usuario actual
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Obtén el ID del usuario actual
                                String userId = user.getUid();
                                // Accede al documento del usuario en la colección correspondiente en Cloud Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Usuarios").document(userId).get().addOnCompleteListener(userTask -> {
                                    if (userTask.isSuccessful()) {
                                        DocumentSnapshot document = userTask.getResult();
                                        if (document.exists()) {
                                            // El usuario pertenece a la colección usuarios1
                                            startActivity(new Intent(getApplicationContext(), BienvenidaPaciente.class));
                                            //finish();
                                        } else {
                                            // El usuario no está en la colección usuarios1, comprobar la otra colección
                                            db.collection("Especialistas").document(userId).get().addOnCompleteListener(userTask2 -> {
                                                if (userTask2.isSuccessful()) {
                                                    DocumentSnapshot document2 = userTask2.getResult();
                                                    if (document2.exists()) {
                                                        // El usuario pertenece a la colección usuarios2
                                                        startActivity(new Intent(getApplicationContext(), BienvenidaEspecialista.class));
                                                        //finish();
                                                    } else {
                                                        // El usuario no está en ninguna de las colecciones
                                                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                                        // Manejar el caso según tus necesidades
                                                    }
                                                } else {
                                                    // Error al obtener el documento del usuario en usuarios2
                                                }
                                            });
                                        }
                                        // Cierra la actividad actual (inicio de sesión)
                                        finish();
                                    } else {
                                        // Error al obtener el documento del usuario en usuarios1
                                    }
                                });
                            }
                        } else {
                            // Error en el inicio de sesión
                            Toast.makeText(getApplicationContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            etPass.setText("");
                        }
                    });
        }
    }
    public void registro(View view){
        Intent i = new Intent(this, ElegirRegistro.class );
        startActivity(i);
    }
}