package com.example.citasvidasana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.citasvidasana.databinding.ActivitySubirResultadosBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SubirResultados extends DrawerBaseEspecialista {

    private static final int REQUEST_CODE_FILE_PICKER = 1;
    private String selectedFilePath;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ProgressDialog progressDialog;
    Uri uriPDF;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etComentarios;
    TextView tvArchivo;
    Spinner spnPacientes;
    Button btnEnviar, btnArchivo;
    ActivitySubirResultadosBinding activitySubirResultadosBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        activitySubirResultadosBinding = ActivitySubirResultadosBinding.inflate(getLayoutInflater());
        setContentView(activitySubirResultadosBinding.getRoot());
        etComentarios=(EditText)findViewById(R.id.etComentarios);
        spnPacientes=findViewById(R.id.spnPaciente);
        spnPacientes.setSelection(0);
        btnEnviar=(Button)findViewById(R.id.btnEnviar);
        btnArchivo=(Button)findViewById(R.id.btnArchivo);
        tvArchivo=(TextView)findViewById(R.id.tvArchivo);


        buscarPacientesFromFirestore();
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectorSpinner selectedUser = (SelectorSpinner) spnPacientes.getSelectedItem();
                if (selectedUser.getNombreCompleto().equals("Seleccione")) {
                    // El usuario seleccionado es el texto inicial "Seleccione", no realiza ninguna acción
                    Toast.makeText(SubirResultados.this, "Por favor, seleccione a su paciente.", Toast.LENGTH_SHORT).show();
                } else if (uriPDF==null){
                    Toast.makeText(SubirResultados.this, "Seleccione un archivo.", Toast.LENGTH_SHORT).show();
                }else {
                    String comentarios = etComentarios.getText().toString();
                    enviarDatos(uriPDF, selectedUser, comentarios);
                }
            }
        });

        btnArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SubirResultados.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    seleccionarPDF();
                }else{
                    ActivityCompat.requestPermissions(SubirResultados.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            seleccionarPDF();
        }else{
            Toast.makeText(this, "Asigne el permiso a la aplicacion", Toast.LENGTH_SHORT).show();

        }

    }

    private void seleccionarPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uriPDF = data.getData();
            String fileName = getFileNameFromUri(data.getData());
            tvArchivo.setText("Archivo seleccionado:\n\n" + fileName);
        } else {
            Toast.makeText(this, "Seleccione un archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }
        return fileName;
    }

    private void buscarPacientesFromFirestore() {
        CollectionReference usersRef = db.collection("Usuarios");
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
                    String nombreComp = nombre+" "+apellidop+" "+apellidom;
                    SelectorSpinner info = new SelectorSpinner(userId, nombreComp);
                    userList.add(info);
                }

                ArrayAdapter<SelectorSpinner> adapter = new ArrayAdapter<>(SubirResultados.this,
                        android.R.layout.simple_spinner_item, userList);
                spnPacientes.setAdapter(adapter);
            } else {

            }
        });
    }
    private void enviarDatos(Uri uriPDF, SelectorSpinner selectedUser, String comentarios) {
        String selectedUserId = selectedUser.getUserId();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DocumentReference userRef = db.collection("Especialistas").document(userId);

        String fechaEnvio = obtenerFechaActual();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Subiendo archivo...");
        progressDialog.setProgress(0);
        progressDialog.show();

        String fileName = getFileNameFromUri(uriPDF);
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Resultados").child(fileName).putFile(uriPDF)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            String nombreRemitente = documentSnapshot.getString("Nombre");
                                            String apellidopRemitente = documentSnapshot.getString("Apellidop");
                                            String apellidomRemitente = documentSnapshot.getString("Apellidom");

                                            String nombreCompleto = nombreRemitente + " " + apellidopRemitente + " " + apellidomRemitente;

                                            Map<String, Object> data = new HashMap<>();
                                            data.put("Comentarios", comentarios);
                                            data.put("FechaEnvio", fechaEnvio);
                                            data.put("NombreEspecialista", nombreCompleto);
                                            data.put("Archivo", url);

                                            CollectionReference userDataRef = db.collection("Usuarios").document(selectedUserId).collection("Resultados");
                                            userDataRef.add(data)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(SubirResultados.this, "Datos enviados al paciente", Toast.LENGTH_SHORT).show();
                                                            spnPacientes.setSelection(0);
                                                            etComentarios.setText("");
                                                            tvArchivo.setText("");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SubirResultados.this, "Error al enviar los datos", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(SubirResultados.this, "Error al obtener el nombre del remitente", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SubirResultados.this, "Error al obtener el nombre del remitente", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SubirResultados.this, "No se subió el archivo", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        int currentProgress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setProgress(currentProgress);
                    }
                });
    }



    private String obtenerFechaActual() {
        // Obtener la fecha actual en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
