package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);*/
                ((EditText) findViewById(R.id.t_name)).onEditorAction(EditorInfo.IME_ACTION_DONE);
                ((EditText) findViewById(R.id.t_floor)).onEditorAction(EditorInfo.IME_ACTION_DONE);
                ((EditText) findViewById(R.id.t_type)).onEditorAction(EditorInfo.IME_ACTION_DONE);
                return true;
            }
        });

        Button button = findViewById(R.id.button_send);
        Button buttonGet = findViewById(R.id.button_get);
        Button buttonTicket = findViewById(R.id.button_ticket);
        Button buttonTipo = findViewById(R.id.btn_tipo);

        buttonTipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo de ticket");

                String[] tipos = {"Soporte", "SIAF", "Impresora", "Redes", "SAPS", "Web"};
                int checkedItem = 0;
                builder.setSingleChoiceItems(tipos, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taux = "";
                        switch (which) {
                            case 0: taux = "Soporte";break;
                            case 1: taux = "SIAF";break;
                            case 2: taux = "Impresora";break;
                            case 3: taux = "Redes";break;
                            case 4: taux = "SAPS";break;
                            case 5: taux = "Web";break;
                        }
                        ((Button)findViewById(R.id.btn_tipo)).setText(taux);
                        //((Button)findViewById(R.id.btn_tipo)).setBackgroundColor(Color.parseColor("#ffffff"));
                        //((Button)findViewById(R.id.btn_tipo)).setTextColor(Color.parseColor("#000000"));
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(((Button)findViewById(R.id.btn_tipo)).getText().toString() == "Tipo"){
                            ((Button)findViewById(R.id.btn_tipo)).setText("Soporte");
                        }
                        ((Button)findViewById(R.id.btn_tipo)).setBackgroundColor(Color.parseColor("#ffffff"));
                        ((Button)findViewById(R.id.btn_tipo)).setTextColor(Color.parseColor("#000000"));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(((Button)findViewById(R.id.btn_tipo)).getCurrentTextColor() != Color.parseColor("#000000")){
                            ((Button)findViewById(R.id.btn_tipo)).setText("Tipo");
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                long datecode = System.currentTimeMillis();
                Map<String, Object> ticket = new HashMap<>();
                EditText tname = (EditText) findViewById(R.id.t_name);
                ticket.put("user", tname.getText().toString().trim());
                EditText tfloor = (EditText) findViewById(R.id.t_floor);
                ticket.put("floor", tfloor.getText().toString());
                EditText ttype = (EditText) findViewById(R.id.t_type);
                //ticket.put("type", ttype.getText().toString());
                Button btype = ((Button)findViewById(R.id.btn_tipo));
                ticket.put("type", btype.getText().toString());
                ticket.put("activo", true);

                if(!isEmpty(tname) && !isEmpty(tfloor) && btype.getText().toString() != "Tipo") {

                    db.collection("ticket").document("tk." + datecode)
                            .set(ticket)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toast.makeText(getApplicationContext(), "New ticket created!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                    tname.setText("");
                    tfloor.setText("");
                    ttype.setText("");
                    btype.setText("Tipo");
                    btype.setBackgroundColor(Color.parseColor("#F1F1F1"));
                    btype.setTextColor(Color.parseColor("#949494"));
                    tname.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    tfloor.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    ttype.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }else if(isEmpty(tname)){
                    Toast.makeText(getApplicationContext(), "Nombre de usuario vacío!", Toast.LENGTH_LONG).show();
                    tname.setText("");
                }else if(isEmpty(tfloor)){
                    Toast.makeText(getApplicationContext(), "Indicar número de piso!", Toast.LENGTH_LONG).show();
                }
                /*else if(isEmpty(ttype)){
                    Toast.makeText(getApplicationContext(), "Indicar tipo de ticket!", Toast.LENGTH_LONG).show();
                }*/
                else if(btype.getText().toString() == "Tipo"){
                    Toast.makeText(getApplicationContext(), "Indicar tipo de ticket!", Toast.LENGTH_LONG).show();
                }
            }

        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                DocumentReference docRef = db.collection("users").document("NEW_USER");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String,Object> data;
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                data = document.getData();
                                String nombre = data.get("name").toString();
                                String uid = data.get("id").toString();
                                Toast.makeText(getApplicationContext(), "Pendiente: "+ nombre +" con ID "+ uid, Toast.LENGTH_LONG).show();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
                //Toast.makeText(getApplicationContext(), "Button GET USERS", Toast.LENGTH_LONG).show();
            }

        });

        buttonTicket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                db.collection("ticket")
                        .whereEqualTo("activo", true)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                Toast.makeText(getApplicationContext(), "Button ACTIVE TICKETS", Toast.LENGTH_LONG).show();
            }

        });

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Toast.makeText(getApplicationContext(), "OK, floating button working.", Toast.LENGTH_LONG).show();

            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
