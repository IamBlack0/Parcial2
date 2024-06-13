package com.example.parcial2.Entidades;

public class Conversacion {
    private int usuarioId;
    private int contactoId;
    private int destinatarioContactoId;
    private String nombre;
    private String apellido;
    private String imagenId; // Ahora es String en lugar de int
    private String ultimoMensaje;
    private String timestamp;

    public Conversacion(int usuarioId, int contactoId, int destinatarioContactoId, String nombre, String apellido, String imagenId, String ultimoMensaje, String timestamp) {
        this.usuarioId = usuarioId;
        this.contactoId = contactoId;
        this.destinatarioContactoId = destinatarioContactoId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.imagenId = imagenId;
        this.ultimoMensaje = ultimoMensaje;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public int getUsuarioId() {
        return usuarioId;
    }

    public int getContactoId() {
        return contactoId;
    }

    public int getDestinatarioContactoId() {
        return destinatarioContactoId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getImagenId() {
        return imagenId;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setImagenId(String imagenId) {
        this.imagenId = imagenId;
    }
}
