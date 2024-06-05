package com.example.parcial2.Entidades;

public class Conversacion {
    private String nombre;
    private String apellido;
    private String ultimoMensaje;

    public Conversacion(String nombre, String apellido, String ultimoMensaje) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
}
