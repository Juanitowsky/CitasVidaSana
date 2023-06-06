package com.example.citasvidasana;

public class LlamarResultados {
    private String FechaEnvio;
    private String NombreEspecialista;
    private String Comentarios;
    private String Archivo;

    public LlamarResultados(){

    }

    public LlamarResultados(String Fecha, String Especialistas, String Comentarios, String Archivo){
        this.FechaEnvio = Fecha;
        this.NombreEspecialista = Especialistas;
        this.Comentarios = Comentarios;
        this.Archivo = Archivo;
    }

    public String getFechaEnvio() {
        return FechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        FechaEnvio = fechaEnvio;
    }

    public String getNombreEspecialista() {
        return NombreEspecialista;
    }

    public void setNombreEspecialista(String nombreEspecialista) {
        NombreEspecialista = nombreEspecialista;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public String getArchivo() {
        return Archivo;
    }

    public void setArchivo(String archivo) {
        Archivo = archivo;
    }
}

