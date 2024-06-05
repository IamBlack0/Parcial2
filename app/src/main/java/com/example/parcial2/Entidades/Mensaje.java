package com.example.parcial2.Entidades;

public class Mensaje {
    private String texto;
    private String hora;
    private boolean esEnviado; // true si el mensaje fue enviado por el usuario, false si fue recibido
    private int usuarioId; // ID del usuario que envió el mensaje

    public Mensaje(String texto, String hora, boolean esEnviado, int usuarioId) {
        this.texto = texto;
        this.hora = hora;
        this.esEnviado = esEnviado;
        this.usuarioId = usuarioId;
    }

    public String getTexto() {
        return texto;
    }

    public String getHora() {
        return hora;
    }

    public boolean esEnviado() {
        return esEnviado;
    }

    public int getUsuarioId() {
        return usuarioId;
    }
}
