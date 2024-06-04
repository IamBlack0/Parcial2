package com.example.parcial2.Entidades;

public class Usuario {


    //atributos
    private int id;
    private String nombre,apellido, telefono;


    //CONSTRUCTOR PARA CONTACTOS
    // SE PUEDE USAR TAMBIEN CHAT
    public Usuario(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    //get y set
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







}
