package com.example.proyecto_agz;

public class Tarea {

    private int idTarea;
    private String textoTarea;
    private int tipoTarea;
    private int estadoTarea;

    public Tarea(int idTarea, String textoTarea, int tipoTarea, int estadoTarea) {
        this.idTarea = idTarea;
        this.textoTarea = textoTarea;
        this.tipoTarea = tipoTarea;
        this.estadoTarea = estadoTarea;
    }
    public Tarea() {

    }

    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public String getTextoTarea() {
        return textoTarea;
    }

    public void setTextoTarea(String textoTarea) {
        this.textoTarea = textoTarea;
    }

    public int getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(int tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public int getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(int estadoTarea) {
        this.estadoTarea = estadoTarea;
    }
}
