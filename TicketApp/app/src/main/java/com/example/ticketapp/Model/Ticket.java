package com.example.ticketapp.Model;

import com.google.firebase.Timestamp;

public class Ticket {
    private String ticketid;
    private String usuario;
    private String tipo;
    private String piso;
    private boolean activo;
    private Timestamp time_created;
    private Timestamp time_closed;
    private String createdby;
    private String claimedby;

    public Ticket(String ticketid, String usuario, String piso, String tipo, boolean activo, Timestamp time_created, Timestamp time_closed, String createdby, String claimedby){
        this.ticketid = ticketid;
        this.usuario = usuario;
        this.tipo = tipo;
        this.piso = piso;
        this.activo = activo;
        this.time_created = time_created;
        this.time_closed = time_closed;
        this.createdby = createdby;
        this.claimedby = claimedby;
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

    public Timestamp getTime_created(){
        return time_created;
    }

    public void setTime_created(Timestamp time_created){
        this.time_created = time_created;
    }

    public Timestamp getTime_closed(){
        return time_closed;
    }

    public void setTime_closed(Timestamp time_closed){
        this.time_closed = time_closed;
    }

    public String getCreatedby(){
        return createdby;
    }

    public void setCreatedby(String createdby){
        this.createdby = createdby;
    }

    public String getClaimedby(){
        return claimedby;
    }

    public void setClaimedby(String claimedby){
        this.claimedby = claimedby;
    }
}
