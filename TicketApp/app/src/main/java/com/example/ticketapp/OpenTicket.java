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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
    boolean available;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        db.collection("users").document(user.getDisplayName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    available = (boolean)task.getResult().get("available");
                }
            }
        });
        final String tkid = getIntent().getStringExtra("tkid");
        setContentView(R.layout.activity_open_ticket);
        context = this;
        final Button btnEnd = findViewById(R.id.btn_end);
        final Button btnClaim = findViewById(R.id.btn_claim);
        final Button btnCancel = findViewById(R.id.btn_cancel);
        final Button btnFree = findViewById(R.id.btn_free);

        DocumentReference docRef = db.collection("ticket").document(tkid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
                        Map<String,Object> ticket = document.getData();

                        ((TextView)findViewById(R.id.ot_tkid)).setText(tkid);
                        ((TextView)findViewById(R.id.ot_time_begin)).setText(
                                "INICIO: ".concat(dateFormat.format(((Timestamp)ticket.get("time_begin")).toDate())));
                        ((TextView)findViewById(R.id.ot_created_by)).setText("CREADO POR: ".concat(ticket.get("created_by").toString()));
                        ((TextView)findViewById(R.id.ot_user)).setText("USUARIO: ".concat(ticket.get("user").toString()));
                        ((TextView)findViewById(R.id.ot_floor)).setText("PISO: ".concat(ticket.get("floor").toString()));
                        if(ticket.get("type").equals("SIAF")|| ticket.get("type").equals("SAPS")){
                            findViewById(R.id.ot_type).setBackgroundColor(Color.parseColor("#FFFF00"));
                        }
                        ((TextView)findViewById(R.id.ot_type)).setText("TIPO: ".concat(ticket.get("type").toString()));
                        if(ticket.get("description") != null){
                            findViewById(R.id.ot_description).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_description)).setText("DESCRIPCIÓN: ".concat(ticket.get("description").toString()));
                        }
                        String state_val = ticket.get("state").toString();
                        String ot_state_val = "pendiente";
                        if(state_val.equals("pending")){
                            btnClaim.setVisibility(View.VISIBLE);
                            if(ticket.get("created_by").equals(user.getDisplayName())){
                                btnCancel.setVisibility(View.VISIBLE);
                            }
                        }
                        if(state_val.equals("claimed")){
                            ot_state_val = "en curso";
                            if(ticket.get("claimed_by").equals(user.getDisplayName())){
                                btnEnd.setVisibility(View.VISIBLE);
                                btnFree.setVisibility(View.VISIBLE);
                                btnCancel.setVisibility(View.VISIBLE);
                            }
                        }
                        if(state_val.equals("closed")){
                            ot_state_val = "cerrado";
                        }
                        if(state_val.equals("cancelled")){
                            ot_state_val = "cancelado";
                        }
                        ((TextView)findViewById(R.id.ot_state)).setText("ESTADO: ".concat(ot_state_val));
                        if(ticket.get("time_claimed") != null){
                            (findViewById(R.id.ot_time_claimed)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_time_claimed)).setText(
                                    "INICIO ATENCIÓN: ".concat(dateFormat.format(((Timestamp)ticket.get("time_claimed")).toDate())));
                            (findViewById(R.id.ot_claimed_by)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_claimed_by)).setText("ATENDIDO POR: ".concat(ticket.get("claimed_by").toString()));
                        }
                        if(ticket.get("time_end") != null){
                            (findViewById(R.id.ot_time_end)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_time_end)).setText(
                                    "FIN: ".concat(dateFormat.format(((Timestamp)ticket.get("time_end")).toDate())));
                        }
                        if(ticket.get("post_mortem") != null){
                            findViewById(R.id.ot_post_mortem).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.ot_post_mortem)).setText("post-mortem: ".concat(ticket.get("post_mortem").toString()));
                        }else if(ticket.get("state").equals("closed") || ticket.get("state").equals("cancelled")){
                            findViewById(R.id.btn_post_mortem).setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("msg", "No such document");
                    }
                } else {
                    Log.d("msg", "get failed with ", task.getException());
                }
            }
        });

        btnClaim.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(available){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Desea atender este incidente?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            db.collection("root").document("tk.0")
                                    .update("last", System.currentTimeMillis());
                            db.collection("users").document(user.getDisplayName())
                                    .update("available", false);
                            db.collection("ticket").document(tkid)
                                    .update("state", "claimed");
                            db.collection("ticket").document(tkid)
                                    .update("time_claimed", new Date());
                            db.collection("ticket").document(tkid)
                                    .update("claimed_by", user.getDisplayName())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    TextView textView = alert.findViewById(android.R.id.message);
                    textView.setTextSize(20);
                }
                else Toast.makeText(context, "Usted ya se encuentra atendiendo un incidente!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Desea cancelar este ticket?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        db.collection("root").document("tk.0")
                                .update("last", System.currentTimeMillis());
                        db.collection("users").document(user.getDisplayName())
                                .update("available", true);
                        db.collection("ticket").document(tkid)
                                .update("time_end", new Date());
                        db.collection("ticket").document(tkid)
                                .update("time_claimed", null);
                        db.collection("ticket").document(tkid)
                                .update("claimed_by", null);
                        db.collection("ticket").document(tkid)
                                .update("state", "cancelled")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                TextView textView = alert.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Desea cerrar este ticket?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        db.collection("root").document("tk.0")
                                .update("last", System.currentTimeMillis());
                        db.collection("users").document(user.getDisplayName())
                                .update("available", true);
                        db.collection("ticket").document(tkid)
                                .update("state", "closed");
                        db.collection("ticket").document(tkid)
                                .update("time_end", new Date())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                TextView textView = alert.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });

        btnFree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Desea liberar este ticket?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        db.collection("root").document("tk.0")
                                .update("last", System.currentTimeMillis());
                        db.collection("users").document(user.getDisplayName())
                                .update("available", true);
                        db.collection("ticket").document(tkid)
                                .update("state", "pending");
                        db.collection("ticket").document(tkid)
                                .update("time_claimed", null);
                        db.collection("ticket").document(tkid)
                                .update("claimed_by", null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                TextView textView = alert.findViewById(android.R.id.message);
                textView.setTextSize(20);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
