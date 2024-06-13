package com.example.parcial2.Entidades;

public class Contacto {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String imagenId; // Ahora es String en lugar de int

    public Contacto(int id, String nombre, String apellido, String telefono, String imagenId) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.imagenId = imagenId;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getImagenId() {
        return imagenId;
    }

    public void setImagenId(String imagenId) {
        this.imagenId = imagenId;
    }
}
