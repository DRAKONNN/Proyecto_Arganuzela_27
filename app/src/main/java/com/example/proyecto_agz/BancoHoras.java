package com.example.proyecto_agz;

public class BancoHoras {

    private int idBanco;
    private String nombreBanco;
    private float horasBanco;

    public BancoHoras(int idBanco, String nombreBanco, float horasBanco) {
        this.idBanco = idBanco;
        this.nombreBanco = nombreBanco;
        this.horasBanco = horasBanco;
    }

    public BancoHoras() {

    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public float getHorasBanco() {
        return horasBanco;
    }

    public void setHorasBanco(float horasBanco) {
        this.horasBanco = horasBanco;
    }
}
