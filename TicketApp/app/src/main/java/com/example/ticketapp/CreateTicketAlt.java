package com.example.ticketapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTicketAlt extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser FBuser = mAuth.getCurrentUser();
    boolean available;
    private int floorButton = -1;
    List<Button> FButtons;
    private int typeButton = -1;
    List<Button> TButtons;
    public void hideKeyboard(){
        ((EditText) findViewById(R.id.ct_user)).onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        db.collection("users").document(FBuser.getDisplayName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    available = (boolean)task.getResult().get("available");
                }
            }
        });
        setContentView(R.layout.activity_create_ticket_alt);

        FButtons = new ArrayList<>();
        TButtons = new ArrayList<>();
        configFloorButtons();
        configTypeButtons();

        Button btn_create_ticket = findViewById(R.id.btn_create_ticket);
        Button btn_create_claim_ticket = findViewById(R.id.btn_create_claim_ticket);

        findViewById(R.id.layout_create_ticket).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });

        btn_create_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketCreator(false);
            }
        });

        btn_create_claim_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available){
                    ticketCreator(true);
                }
                else Toast.makeText(context, "Usted ya se encuentra atendiendo un incidente!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ticketCreator(boolean cc){
        hideKeyboard();
        View view = findViewById(R.id.btn_create_ticket);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        long datecode = System.currentTimeMillis();
        EditText ct_user = findViewById(R.id.ct_user);
        String ticketid = "tk."+datecode;
        Map<String, Object> ticket = new HashMap<>();

        if(isEmpty(ct_user)){
            Toast.makeText(getApplicationContext(), "Nombre de usuario vacío!", Toast.LENGTH_LONG).show();
            ct_user.setText("");
        }else if(floorButton == -1){
            Toast.makeText(getApplicationContext(), "Indicar número de piso!", Toast.LENGTH_LONG).show();
        }else if(typeButton == -1){
            Toast.makeText(getApplicationContext(), "Indicar tipo de ticket!", Toast.LENGTH_LONG).show();
        }else{
            Date time_begin = new Date();
            String created_by = FBuser.getDisplayName();
            String user = ct_user.getText().toString();
            String floor = Integer.toString(floorButton);
            String type = getType(typeButton);
            String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            String state = "pending";
            Date time_claimed = null;
            String claimed_by = null;
            if(cc){
                db.collection("users").document(FBuser.getDisplayName()).update("available", false);
                time_claimed = new Date();
                claimed_by = FBuser.getDisplayName();
                state = "claimed";
            }
            Timestamp time_end = null;
            String post_mortem = null;

            ticket.put("time_begin", time_begin);
            ticket.put("created_by", created_by);
            ticket.put("user", user);
            ticket.put("floor", floor);
            ticket.put("type", type);
            ticket.put("description", description);
            ticket.put("state", state);
            ticket.put("time_claimed", time_claimed);
            ticket.put("claimed_by", claimed_by);
            ticket.put("time_end", time_end);
            ticket.put("post_mortem", post_mortem);

            db.collection("ticket").document(ticketid)
                    .set(ticket)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "ticket creado", Toast.LENGTH_LONG).show();
                        }
                    });
            ct_user.onEditorAction(EditorInfo.IME_ACTION_DONE);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void configFloorButtons(){
        final Button btn1 = findViewById(R.id.F1);
        final Button btn2 = findViewById(R.id.F2);
        final Button btn3 = findViewById(R.id.F3);
        final Button btn4 = findViewById(R.id.F4);
        final Button btn5 = findViewById(R.id.F5);
        final Button btn6 = findViewById(R.id.F6);
        final Button btn7 = findViewById(R.id.F7);
        final Button btn8 = findViewById(R.id.F8);
        final Button btn9 = findViewById(R.id.F9);
        final Button btn10 = findViewById(R.id.F10);
        FButtons.add(btn1);
        FButtons.add(btn2);
        FButtons.add(btn3);
        FButtons.add(btn4);
        FButtons.add(btn5);
        FButtons.add(btn6);
        FButtons.add(btn7);
        FButtons.add(btn8);
        FButtons.add(btn9);
        FButtons.add(btn10);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(0);
                hideKeyboard();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(1);
                hideKeyboard();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(2);
                hideKeyboard();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(3);
                hideKeyboard();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(4);
                hideKeyboard();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(5);
                hideKeyboard();
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(6);
                hideKeyboard();
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(7);
                hideKeyboard();
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(8);
                hideKeyboard();
            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(9);
                hideKeyboard();
            }
        });
    }

    public void selectFloorButtons(int eb) {
        floorButton = eb;
        for(int i=0; i<10;i++){
            if (floorButton!= i){
                FButtons.get(i).setSelected(false);
                FButtons.get(i).setBackground(getDrawable(R.drawable.shape_green_outline));
                FButtons.get(i).setTextColor(Color.parseColor("#5ED540"));
            }else{
                FButtons.get(i).setSelected(true);
                FButtons.get(i).setBackground(getDrawable(R.drawable.shape_green_fill));
                FButtons.get(i).setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    public void configTypeButtons(){
        final Button btnSupport = findViewById(R.id.ctt_support);
        final Button btnPrinter = findViewById(R.id.ctt_printer);
        final Button btnSisged = findViewById(R.id.ctt_sisged);
        final Button btnSiaf = findViewById(R.id.ctt_siaf);
        final Button btnSaps = findViewById(R.id.ctt_saps);
        TButtons.add(btnSupport);
        TButtons.add(btnPrinter);
        TButtons.add(btnSisged);
        TButtons.add(btnSiaf);
        TButtons.add(btnSaps);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(0);
                hideKeyboard();
            }
        });
        btnPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(1);
                hideKeyboard();
            }
        });
        btnSisged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(2);
                hideKeyboard();
            }
        });
        btnSiaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(3);
                hideKeyboard();
            }
        });
        btnSaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(4);
                hideKeyboard();
            }
        });
    }

    public void selectTypeButtons(int tb) {
        typeButton = tb;
        for(int i=0; i<5;i++){
            if (typeButton!= i){
                TButtons.get(i).setSelected(false);
                TButtons.get(i).setBackground(getDrawable(R.drawable.shape_outline));
                TButtons.get(i).setTextColor(Color.parseColor("#1CC2C2"));
            }else{
                TButtons.get(i).setSelected(true);
                TButtons.get(i).setBackground(getDrawable(R.drawable.shape_fill));
                TButtons.get(i).setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    public String getType(int t){
        String t_name = "";
        switch(t){
            case 0: t_name = "Soporte"; break;
            case 1: t_name = "Impresora"; break;
            case 2: t_name = "SISGED"; break;
            case 3: t_name = "SIAF"; break;
            case 4: t_name = "SAPS"; break;
        }
        return t_name;
    }
}
