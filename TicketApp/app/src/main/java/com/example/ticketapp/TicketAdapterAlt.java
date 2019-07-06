package com.example.ticketapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ticketapp.Model.Ticket;
import com.example.ticketapp.Model.TicketAlt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class TicketViewHolderAlt extends RecyclerView.ViewHolder {
    Context context;
    public View view;
    public TextView ticketid, user, type, floor, time_begin, state, claimed_by;
    public LinearLayout layout_atendido;
    public TicketViewHolderAlt(View ticketView, final Context context){
        super(ticketView);
        ticketid = ticketView.findViewById(R.id.txtTicketId);
        user = ticketView.findViewById(R.id.txtUsuario);
        type = ticketView.findViewById(R.id.txtTipo);
        floor = ticketView.findViewById(R.id.txtPiso);
        time_begin = ticketView.findViewById(R.id.txtFecha);
        state = ticketView.findViewById(R.id.txtActivo);
        claimed_by = ticketView.findViewById(R.id.txtAtendido);
        layout_atendido = ticketView.findViewById(R.id.layout_atendido);

        this.context = context;
        view = ticketView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(context, OpenTicketAlt.class);
                i.putExtra("tkid", ticketid.getText());
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}

public class TicketAdapterAlt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TicketAlt> tickets;
    private Context context;

    public TicketAdapterAlt(Context context, List<TicketAlt> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new TicketViewHolderAlt(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TicketAlt ticket = tickets.get(position);
        TicketViewHolderAlt ticketViewHolder = (TicketViewHolderAlt) holder;
        ticketViewHolder.ticketid.setText(ticket.getTicketid());
        ticketViewHolder.user.setText(ticket.getUser());
        ticketViewHolder.floor.setText(ticket.getFloor());
        ticketViewHolder.type.setText(ticket.getType());
        ticketViewHolder.type.setBackgroundResource(R.drawable.rounded_corner);
        if(ticket.getType().equals("SIAF")){
            ticketViewHolder.type.setBackgroundResource(R.drawable.rc_siaf);
        }else if(ticket.getType().equals("SAPS")){
            ticketViewHolder.type.setBackgroundResource(R.drawable.rc_saps);
        }else if(ticket.getType().equals("SISGED")){
            ticketViewHolder.type.setBackgroundResource(R.drawable.rc_sisged);
        }
        Date date = ticket.getTime_begin().toDate();
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm - dd/MM");
        ticketViewHolder.time_begin.setText(myFormat.format(date));
        String ac_color = "#000000";
        String ac_state = "pendiente";
        String state_value = ticket.getState();
        if(state_value.equals("pending")){
            ac_color = "#30E418";
        }else if(state_value.equals("claimed")){
            ac_state = "en curso";
            ac_color = "#30FFF6";
        }else if(state_value.equals("closed")){
            ac_state = "cerrado";
            ac_color = "#FF0000";
        }else if(state_value.equals("cancelled")){
            ac_state = "cancelado";
            ac_color = "#FEBE30";
        }
        ticketViewHolder.state.setText(ac_state);
        ticketViewHolder.state.setTextColor(Color.parseColor(ac_color));
        if(ticket.getState().equals("claimed")){
            ticketViewHolder.layout_atendido.setVisibility(View.VISIBLE);
            ticketViewHolder.claimed_by.setText(ticket.getClaimed_by());
        }else{
            ticketViewHolder.layout_atendido.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}