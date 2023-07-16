package com.grupo5.pm2e1grupo5.config;

public class Contactos {
    private String id;

    private String nombres;

    private String telefono;

    private String latitud;

    private String logintud;

    private String video;

    public Contactos() {
    }

    public Contactos(String id, String nombres, String telefono, String latitud, String logintud, String video) {
        this.id = id;
        this.nombres = nombres;
        this.telefono = telefono;
        this.latitud = latitud;
        this.logintud = logintud;
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLogintud() {
        return logintud;
    }

    public void setLogintud(String logintud) {
        this.logintud = logintud;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
