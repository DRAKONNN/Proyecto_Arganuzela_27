package com.example.proyecto_agz;

public class Sala {

    private int idSala;
    private String nombreSala;
    private String fechaInicioSala;
    private String fechaFinalSala;

    public Sala(int id, String nombre, String fecha_inicio, String fecha_final) {
        this.idSala = id;
        this.nombreSala = nombre;
        this.fechaInicioSala = fecha_inicio;
        this.fechaFinalSala = fecha_final;
    }

    public Sala() {

    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getFechaInicioSala() {
        return fechaInicioSala;
    }

    public void setFechaInicioSala(String fechaInicioSala) {
        this.fechaInicioSala = fechaInicioSala;
    }

    public String getFechaFinalSala() {
        return fechaFinalSala;
    }

    public void setFechaFinalSala(String fechaFinalSala) {
        this.fechaFinalSala = fechaFinalSala;
    }
}
