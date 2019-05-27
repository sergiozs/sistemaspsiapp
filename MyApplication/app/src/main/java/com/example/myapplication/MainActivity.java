package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button = findViewById(R.id.button_send);
        Button buttonGet = findViewById(R.id.button_get);
        Button buttonTicket = findViewById(R.id.button_ticket);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /*Map<String, Object> user = new HashMap<>();
                user.put("name", "Bob Smith");
                user.put("id", 69);*/
                long datecode = System.currentTimeMillis();

                Map<String, Object> ticket = new HashMap<>();
                EditText tname = (EditText) findViewById(R.id.t_name);
                ticket.put("user", tname.getText().toString());
                EditText tfloor = (EditText) findViewById(R.id.t_floor);
                ticket.put("floor", tfloor.getText().toString());
                EditText ttype = (EditText) findViewById(R.id.t_type);
                ticket.put("type", ttype.getText().toString());
                ticket.put("activo", true);


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
                //Toast.makeText(getApplicationContext(), "Button NEW TICKET", Toast.LENGTH_LONG).show();
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
/*
    public void sendMessage(View view) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Bob Smith");
        user.put("id", "99");
        db.collection("users").document("NEW_USER")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        Toast.makeText(getApplicationContext(), "OK, writing data...", Toast.LENGTH_LONG).show();
    }

    public void getUsers(View view) {
        //Map<String,Object> data;
        String data;
        DocumentReference docRef = db.collection("users").document("NEW_USER");
        data = docRef.get().getResult().get("name").toString();
        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
    }

*/

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
