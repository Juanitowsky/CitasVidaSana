package com.example.citasvidasana;

public class LlamarEspecialistas {
    private String userId;
    private String Nombre;
    private String Apellidop;
    private String Apellidom;
    private String Telefono;
    private String Correo;
    private String Especialidad;
    private String Cedula;

    public LlamarEspecialistas(){

    }

    public LlamarEspecialistas(String userId, String Nombre, String Apellidop, String Apellidom, String Telefono, String Correo,
                               String Especialidad, String Cedula){
        this.userId = userId;
        this.Nombre = Nombre;
        this.Apellidop = Apellidop;
        this.Apellidom = Apellidom;
        this.Telefono = Telefono;
        this.Correo = Correo;
        this.Especialidad = Especialidad;
        this.Cedula = Cedula;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidop() {
        return Apellidop;
    }

    public void setApellidop(String apellidop) {
        Apellidop = apellidop;
    }

    public String getApellidom() {
        return Apellidom;
    }

    public void setApellidom(String apellidom) {
        Apellidom = apellidom;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getEspecialidad() {
        return Especialidad;
    }

    public void setEspecialidad(String especialidad) {
        Especialidad = especialidad;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String toString() {
        return Correo; // Devuelve el nombre del usuario para mostrar en el Spinner
    }
}
