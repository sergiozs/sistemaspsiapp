package com.example.ticketapp.Model;

import com.google.firebase.Timestamp;

public class TicketAlt {
    private String ticketid;
    private Timestamp time_begin;
    private String created_by;
    private String user;
    private String floor;
    private String type;
    private String description;
    private String state;
    private Timestamp time_claimed;
    private String claimed_by;
    private Timestamp time_end;
    private String post_mortem;

    public TicketAlt(String ticketid, Timestamp time_begin, String created_by, String user, String floor, String type,
                     String description, String state, Timestamp time_claimed, String claimed_by, Timestamp time_end, String post_mortem) {
        this.ticketid = ticketid;
        this.time_begin = time_begin;
        this.created_by = created_by;
        this.user = user;
        this.floor = floor;
        this.type = type;
        this.description = description;
        this.state = state;
        this.claimed_by = claimed_by;
        this.time_claimed = time_claimed;
        this.time_end = time_end;
        this.post_mortem = post_mortem;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public Timestamp getTime_begin() {
        return time_begin;
    }

    public void setTime_begin(Timestamp time_begin) {
        this.time_begin = time_begin;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getTime_claimed() {
        return time_claimed;
    }

    public void setTime_claimed(Timestamp time_claimed) {
        this.time_claimed = time_claimed;
    }

    public String getClaimed_by() {
        return claimed_by;
    }

    public void setClaimed_by(String claimed_by) {
        this.claimed_by = claimed_by;
    }

    public Timestamp getTime_end() {
        return time_end;
    }

    public void setTime_end(Timestamp time_end) {
        this.time_end = time_end;
    }
    public String getPost_mortem() {
        return post_mortem;
    }

    public void setPost_mortem(String post_mortem) {
        this.post_mortem = post_mortem;
    }
}
