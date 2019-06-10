package com.example.ticketapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateTicket extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context;

    public void hideKeyboard(){
        ((EditText) findViewById(R.id.t_user)).onEditorAction(EditorInfo.IME_ACTION_DONE);
        ((EditText) findViewById(R.id.t_piso)).onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        Spinner spinner = findViewById(R.id.spinTipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.tipos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn_crear_ticket = findViewById(R.id.btn_crear_ticket);
        Spinner spinTipo = findViewById(R.id.spinTipo);

        findViewById(R.id.create_ticket_LL).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return true;
            }
        });

        spinTipo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        }) ;

        btn_crear_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                View view = findViewById(R.id.btn_crear_ticket);
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                long datecode = System.currentTimeMillis();
                Date date = new Date();
                Map<String, Object> ticket = new HashMap<>();
                EditText tname = findViewById(R.id.t_user);
                ticket.put("user", tname.getText().toString().trim());
                EditText tfloor = findViewById(R.id.t_piso);
                ticket.put("floor", tfloor.getText().toString());
                Spinner stype = findViewById(R.id.spinTipo);
                ticket.put("type", stype.getSelectedItem().toString());
                ticket.put("time", date);
                ticket.put("activo", true);

                if(isEmpty(tname)){
                    Toast.makeText(getApplicationContext(), "Nombre de usuario vacío!", Toast.LENGTH_LONG).show();
                    tname.setText("");
                }else if(isEmpty(tfloor)){
                    Toast.makeText(getApplicationContext(), "Indicar número de piso!", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(tfloor.getText().toString()) <= 0 || Integer.parseInt(tfloor.getText().toString()) >10){
                    Toast.makeText(getApplicationContext(), "Ese piso no existe!", Toast.LENGTH_LONG).show();
                }else if(stype.getSelectedItem().toString().equals("<seleccionar>")){
                    Toast.makeText(getApplicationContext(), "Indicar tipo de ticket!", Toast.LENGTH_LONG).show();
                }else if(!isEmpty(tname) && !isEmpty(tfloor) && stype.getSelectedItem().toString() != "<seleccionar>") {

                    db.collection("ticket").document("tk." + datecode)
                            .set(ticket)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("SUCCESS", "DocumentSnapshot successfully written!");
                                    Toast.makeText(getApplicationContext(), "New ticket created!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("FAILURE", "Error writing document", e);
                                }
                            });
                    tname.setText("");
                    tfloor.setText("");
                    stype.setSelection(0);
                    tname.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    tfloor.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    finish();
                }
            }
        });

    }
}
