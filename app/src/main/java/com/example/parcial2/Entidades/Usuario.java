package com.example.parcial2.Entidades;

import android.os.Bundle;

public class Usuario {
    // Atributos
    private int id;
    private String nombre, apellido, telefono;
    private int imagenId; // ID del recurso de la imagen

    // Constructor para contactos con imagen
    public Usuario(String nombre, String apellido, int imagenId) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.imagenId = imagenId;
    }

    public Usuario(int id, String nombre, String apellido, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putInt("id", getImagenId());
        b.putString("nombre", getNombre());
        b.putString("apellido", getApellido());
        b.putString("telefono", getTelefono());
        return b;
    }

    public static Usuario toclass(Bundle b){
        return new Usuario(
                b.getInt("id"),
                b.getString("nombre"),
                b.getString("apellido"),
                b.getString("telefono")
        );
    }
}
