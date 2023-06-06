package com.example.citasvidasana;

public class SelectorSpinner {
    private String userId;
    private String nombreCompleto;
    public SelectorSpinner(String userId, String nombreCompleto) {
        this.userId = userId;
        this.nombreCompleto = nombreCompleto;
    }

    public String getUserId() {
        return userId;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }



    public String toString() {
        return nombreCompleto; // Devuelve el nombre del usuario para mostrar en el Spinner
    }
}
