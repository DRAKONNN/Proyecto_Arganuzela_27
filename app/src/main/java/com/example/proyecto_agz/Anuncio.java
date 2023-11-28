package com.example.proyecto_agz;

public class Anuncio {

    private int idAnuncio;
    private String tituloAnuncio;
    private String descripcionAnuncio;
    private String fechaAnuncio;

    public Anuncio(int idAnuncio, String tituloAnuncio, String descripcionAnuncio, String fechaAnuncio) {
        this.idAnuncio = idAnuncio;
        this.tituloAnuncio = tituloAnuncio;
        this.descripcionAnuncio = descripcionAnuncio;
        this.fechaAnuncio = fechaAnuncio;
    }

    public Anuncio() {

    }

    public int getIdAnuncio() {
        return idAnuncio;
    }

    public String getTituloAnuncio() {
        return tituloAnuncio;
    }

    public String getDescripcionAnuncio() {
        return descripcionAnuncio;
    }

    public String getFechaAnuncio() {
        return fechaAnuncio;
    }

    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public void setTituloAnuncio(String tituloAnuncio) {
        this.tituloAnuncio = tituloAnuncio;
    }

    public void setDescripcionAnuncio(String descripcionAnuncio) {
        this.descripcionAnuncio = descripcionAnuncio;
    }

    public void setFechaAnuncio(String fechaAnuncio) {
        this.fechaAnuncio = fechaAnuncio;
    }
}
