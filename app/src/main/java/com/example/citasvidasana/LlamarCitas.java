package com.example.citasvidasana;

public class LlamarCitas {
    private String TipoCita;
    private String Fecha;
    private String Hora;
    private String Especialista;

    public LlamarCitas(){

    }
    public LlamarCitas(String TipoCita, String Fecha, String Hora, String Especialista){
        this.TipoCita = TipoCita;
        this.Fecha = Fecha;
        this.Hora = Hora;
        this.Especialista = Especialista;
    }

    public String getTipoCita() {
        return TipoCita;
    }

    public void setTipoCita(String tipoCita) {
        TipoCita = tipoCita;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getEspecialista() {
        return Especialista;
    }
}
