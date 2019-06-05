package com.example.ticketapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ticketapp.Model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TicketViewHolder extends RecyclerView.ViewHolder {
    public TextView ticketid, usuario, tipo, piso, fecha, activo;
    public TicketViewHolder(View ticketView){
        super(ticketView);
        ticketid = (TextView) ticketView.findViewById(R.id.txtTicketId);
        usuario = (TextView) ticketView.findViewById(R.id.txtUsuario);
        tipo = (TextView) ticketView.findViewById(R.id.txtTipo);
        piso = (TextView) ticketView.findViewById(R.id.txtPiso);
        fecha = (TextView) ticketView.findViewById(R.id.txtFecha);
        activo = (TextView) ticketView.findViewById(R.id.txtActivo);

    }
}

public class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] mDataset;
    private List<Ticket> tickets;
    private Activity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    /*public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public MyViewHolder(CardView cv) {
            super(cv);
            cardView = cv;
        }
    }*/

    // Provide a suitable constructor (depends on the kind of dataset)
    public TicketAdapter(RecyclerView recyclerView, Activity activity, List<Ticket> tickets) {
        this.activity = activity;
        this.tickets = tickets;
        Log.d("INSIDE", Integer.toString(this.tickets.size()));
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new TicketViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Ticket ticket = tickets.get(position);
        TicketViewHolder ticketViewHolder = (TicketViewHolder) holder;

        ticketViewHolder.ticketid.setText(ticket.getTicketid());
        ticketViewHolder.usuario.setText(ticket.getUsuario());
        ticketViewHolder.piso.setText(ticket.getPiso());
        ticketViewHolder.tipo.setText(ticket.getTipo());
        ticketViewHolder.fecha.setText(String.valueOf(ticket.getFecha()));
        ticketViewHolder.activo.setText(String.valueOf(ticket.getActivo()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tickets.size();
    }
}