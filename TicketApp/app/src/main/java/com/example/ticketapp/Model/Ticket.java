package com.example.ticketapp.Model;

import com.google.firebase.Timestamp;

public class Ticket {
    private String ticketid;
    private String usuario;
    private String tipo;
    private String piso;
    private boolean activo;
    private Timestamp fecha;

    public Ticket(String ticketid, String usuario, String piso, String tipo, boolean activo, Timestamp fecha){
        this.ticketid = ticketid;
        this.usuario = usuario;
        this.tipo = tipo;
        this.piso = piso;
        this.activo = activo;
        this.fecha = fecha;
    }

    public String getTicketid(){
        return ticketid;
    }

    public void setTicketid(String ticketid){
        this.ticketid = ticketid;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getPiso(){
        return piso;
    }

    public void setPiso(String piso){
        this.piso = piso;
    }

    public boolean getActivo(){
        return activo;
    }

    public void setActivo(boolean activo){
        this.activo = activo;
    }

    public Timestamp getFecha(){
        return fecha;
    }

    public void setFecha(Timestamp fecha){
        this.fecha = fecha;
    }
}
