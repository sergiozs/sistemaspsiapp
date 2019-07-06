package com.example.ticketapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateTicketAlt extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser FBuser = mAuth.getCurrentUser();
    public void hideKeyboard(){
        ((EditText) findViewById(R.id.ct_user)).onEditorAction(EditorInfo.IME_ACTION_DONE);
        ((EditText) findViewById(R.id.ct_floor)).onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket_alt);
        final Spinner spinner = findViewById(R.id.ct_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.tipos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn_create_ticket = findViewById(R.id.btn_create_ticket);
        Button btn_create_claim_ticket = findViewById(R.id.btn_create_claim_ticket);

        findViewById(R.id.layout_create_ticket).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
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
                ticketCreator(true);
            }
        });
    }

    public void ticketCreator(boolean cc){
        hideKeyboard();
        View view = findViewById(R.id.btn_create_ticket);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        long datecode = System.currentTimeMillis();
        EditText ct_user = findViewById(R.id.ct_user);
        EditText ct_floor = findViewById(R.id.ct_floor);
        Spinner ct_type = findViewById(R.id.ct_type);
        String ticketid = "tk."+datecode;
        Map<String, Object> ticket = new HashMap<>();

        if(isEmpty(ct_user)){
            Toast.makeText(getApplicationContext(), "Nombre de usuario vacío!", Toast.LENGTH_LONG).show();
            ct_user.setText("");
        }else if(isEmpty(ct_floor)){
            Toast.makeText(getApplicationContext(), "Indicar número de piso!", Toast.LENGTH_LONG).show();
        }else if(Integer.parseInt(ct_floor.getText().toString()) <= 0 || Integer.parseInt(ct_floor.getText().toString()) >10){
            Toast.makeText(getApplicationContext(), "Ese piso no existe!", Toast.LENGTH_LONG).show();
        }else if(!isEmpty(ct_user) && !isEmpty(ct_floor)) {
            Date time_begin = new Date();
            String created_by = FBuser.getDisplayName();
            String user = ct_user.getText().toString();
            String floor = ct_floor.getText().toString();
            String type = ct_type.getSelectedItem().toString();
            String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
            String state = "pending";
            Date time_claimed = null;
            String claimed_by = null;
            if(cc){
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
            ct_floor.onEditorAction(EditorInfo.IME_ACTION_DONE);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
