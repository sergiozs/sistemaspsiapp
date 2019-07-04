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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class TicketViewHolder extends RecyclerView.ViewHolder {
    Context context;
    public View view;
    public TextView ticketid, usuario, tipo, piso, fecha, activo, atendido;
    public LinearLayout layout_atendido;
    public TicketViewHolder(View ticketView, final Context context){
        super(ticketView);
        ticketid = ticketView.findViewById(R.id.txtTicketId);
        usuario = ticketView.findViewById(R.id.txtUsuario);
        tipo = ticketView.findViewById(R.id.txtTipo);
        piso = ticketView.findViewById(R.id.txtPiso);
        fecha = ticketView.findViewById(R.id.txtFecha);
        activo = ticketView.findViewById(R.id.txtActivo);
        atendido = ticketView.findViewById(R.id.txtAtendido);
        layout_atendido = ticketView.findViewById(R.id.layout_atendido);

        this.context = context;
        view = ticketView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(context, OpenTicket.class);
                i.putExtra("tkid", ticketid.getText());
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}

public class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Ticket> tickets;
    private Context context;

    public TicketAdapter(Context context, List<Ticket> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new TicketViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        TicketViewHolder ticketViewHolder = (TicketViewHolder) holder;
        ticketViewHolder.ticketid.setText(ticket.getTicketid());
        ticketViewHolder.usuario.setText(ticket.getUsuario());
        ticketViewHolder.piso.setText(ticket.getPiso());
        ticketViewHolder.tipo.setText(ticket.getTipo());
        ticketViewHolder.tipo.setBackgroundResource(R.drawable.rounded_corner);
        if(ticket.getTipo().equals("SIAF")){
            ticketViewHolder.tipo.setBackgroundResource(R.drawable.rc_siaf);
        }else if(ticket.getTipo().equals("SAPS")){
            ticketViewHolder.tipo.setBackgroundResource(R.drawable.rc_saps);
        }else if(ticket.getTipo().equals("SISGED")){
            ticketViewHolder.tipo.setBackgroundResource(R.drawable.rc_sisged);
        }
        Date date = ticket.getTime_created().toDate();
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm - dd/MM");
        ticketViewHolder.fecha.setText(myFormat.format(date));
        String ac_color, ac_text;
        if(ticket.getActivo()){
            ac_color = "#30E418";
            ac_text = "pendiente";
        }else{
            ac_color = "#FF0000";
            ac_text = "cerrado";
        }
        ticketViewHolder.activo.setText(ac_text);
        ticketViewHolder.activo.setTextColor(Color.parseColor(ac_color));
        if(!ticket.getClaimedby().equals("-") && ticket.getActivo()){
            ticketViewHolder.layout_atendido.setVisibility(View.VISIBLE);
            ticketViewHolder.atendido.setText(ticket.getClaimedby());
        }
        if(ticket.getClaimedby().equals("-") || !ticket.getActivo()){
            ticketViewHolder.layout_atendido.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}