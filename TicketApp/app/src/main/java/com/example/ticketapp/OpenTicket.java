package com.example.ticketapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class OpenTicket extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        final String tkid = getIntent().getStringExtra("tkid");
        setContentView(R.layout.activity_open_ticket);
        context = this;
        final Button btnTerminarTicket = findViewById(R.id.btn_terminar_ticket);
        final Button btnClaimTicket = findViewById(R.id.btn_claim_ticket);

        DocumentReference docRef = db.collection("ticket").document(tkid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> ticket = document.getData();
                        ((TextView)findViewById(R.id.ot_tkid)).setText(tkid);
                        ((TextView)findViewById(R.id.ot_usuario)).setText("USUARIO: "+ticket.get("user").toString());
                        ((TextView)findViewById(R.id.ot_piso)).setText("PISO: "+ticket.get("floor").toString());
                        TextView ot_tipo = findViewById(R.id.ot_tipo);
                        if(ticket.get("type").toString().equals("SIAF")|| ticket.get("type").toString().equals("SAPS")){
                            ot_tipo.setBackgroundColor(Color.parseColor("#FFFF00"));
                        }
                        ot_tipo.setText("TIPO: "+ticket.get("type").toString());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
                        ((TextView)findViewById(R.id.ot_time_created)).setText("FECHA DE CREACIÓN: "+dateFormat.format(((Timestamp)ticket.get("time_created")).toDate()));
                        if(ticket.get("time_closed") != null){
                            (findViewById(R.id.ot_time_closed)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_time_closed)).setText("FECHA DE CIERRE: "+dateFormat.format(((Timestamp)ticket.get("time_closed")).toDate()));
                        }
                        ((TextView)findViewById(R.id.ot_activo)).setText(((boolean)ticket.get("activo")) ? "ESTADO: pendiente": "ESTADO: cerrado");
                        ((TextView)findViewById(R.id.ot_created)).setText("CREADO POR: "+ticket.get("createdby").toString());
                        if(!ticket.get("claimedby").toString().equals("-")){
                            (findViewById(R.id.ot_claimed)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_claimed)).setText("ATENDIDO POR: "+ticket.get("claimedby").toString());
                        }
                        if((boolean)ticket.get("activo")) {
                            if(ticket.get("claimedby").toString().equals("-")) btnClaimTicket.setVisibility(View.VISIBLE);
                            else if(ticket.get("claimedby").toString().equals(user.getDisplayName())) btnTerminarTicket.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("msg", "No such document");
                    }
                } else {
                    Log.d("msg", "get failed with ", task.getException());
                }
            }
        });

        btnTerminarTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Desea cerrar este ticket?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        db.collection("ticket").document(tkid)
                                .update("activo", false)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("msg", "document updated");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("msg", "Error updating document", e);
                                    }
                                });
                        db.collection("ticket").document(tkid)
                                .update("time_closed", new Timestamp(new Date()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("msg", "Error updating document", e);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                TextView textView = (TextView) alert.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });

        btnClaimTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Desea atender este incidente?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        db.collection("ticket").document(tkid)
                                .update("claimedby", user.getDisplayName())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("msg", "Error updating document", e);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                TextView textView = (TextView) alert.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });
    }
}
