package com.example.ticketapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ticketapp.Model.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class TicketViewHolder extends RecyclerView.ViewHolder {
    public TextView ticketid, usuario, tipo, piso, fecha, activo;
    public TicketViewHolder(View ticketView){
        super(ticketView);
        ticketid = ticketView.findViewById(R.id.txtTicketId);
        usuario = ticketView.findViewById(R.id.txtUsuario);
        tipo = ticketView.findViewById(R.id.txtTipo);
        piso = ticketView.findViewById(R.id.txtPiso);
        fecha = ticketView.findViewById(R.id.txtFecha);
        activo = ticketView.findViewById(R.id.txtActivo);
    }
}

public class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Ticket> tickets;
    private Activity activity;

    public TicketAdapter(RecyclerView recyclerView, Activity activity, List<Ticket> tickets) {
        this.activity = activity;
        this.tickets = tickets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new TicketViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        TicketViewHolder ticketViewHolder = (TicketViewHolder) holder;

        ticketViewHolder.ticketid.setText(ticket.getTicketid());
        ticketViewHolder.usuario.setText(ticket.getUsuario());
        ticketViewHolder.piso.setText(ticket.getPiso());
        ticketViewHolder.tipo.setText(ticket.getTipo());
        //ticketViewHolder.fecha.setText(String.valueOf(ticket.getFecha()));
        Date date = ticket.getFecha().toDate();
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm  |  dd/MM");
        ticketViewHolder.fecha.setText(myFormat.format(date));
        ticketViewHolder.activo.setText("pendiente");
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}