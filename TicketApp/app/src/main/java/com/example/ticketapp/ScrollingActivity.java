package com.example.ticketapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ticketapp.Model.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "LOGIN ACTIVITY";
    Context context;
    List<Ticket> tickets = new ArrayList<>();
    TicketAdapter adapter;
    int FILTER_STATE = 1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        FILTER_STATE = sharedPreferences.getInt("FILTER_STATE", 0);
        updateUI(user);
    }

    public void updateUI(FirebaseUser user){
        if (user == null) {
            setContentView(R.layout.activity_login);
            final Button btnLogin = findViewById(R.id.btn_login);
            final TextView userName = findViewById(R.id.login_user);
            final TextView password = findViewById(R.id.login_password);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userName.getText() == null || password.getText() == null){
                        Toast.makeText(context, "Ingresar Datos!", Toast.LENGTH_SHORT).show();
                    }else{
                        signIn(userName.getText().toString(), password.getText().toString());
                    }
                }
            });
        } else {
            Toast.makeText(context, "Welcome ".concat(user.getDisplayName()), Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_scrolling);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CreateTicket.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }
    }

    public void signIn(String username, String password){
        String userEmail = username.concat("@psi.gob.pe");
        mAuth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            recreate();
                        }
                        else{
                            Toast.makeText(context, "signInWithEmail:failure ".concat(task.getException().toString()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateTicketList(){
        tickets.clear();
        RecyclerView recycler = findViewById(R.id.my_recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TicketAdapter(context, tickets);
        recycler.setAdapter(adapter);

        Query docRef = null;
        switch(FILTER_STATE){
            case 0: docRef = db.collection("ticket"); break;
            case 1: docRef = db.collection("ticket").whereEqualTo("activo", true); break;
            case 2: docRef = db.collection("ticket").whereEqualTo("activo", false); break;
        }
                docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String,Object> aTicket;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                aTicket = document.getData();
                                Ticket auxT = new Ticket(document.getId(),
                                        aTicket.get("user").toString(),
                                        "Piso "+aTicket.get("floor").toString(),
                                        aTicket.get("type").toString(),
                                        (boolean) aTicket.get("activo"),
                                        (Timestamp) aTicket.get("time_created"),
                                        (Timestamp) aTicket.get("time_closed"),
                                        aTicket.get("createdby").toString(),
                                        aTicket.get("claimedby").toString());
                                tickets.add(auxT);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_all) {
            FILTER_STATE = 0;
            sharedPreferences.edit().putInt("FILTER_STATE", FILTER_STATE).apply();
            sharedPreferences.edit().apply();
            updateTicketList();
            return true;
        }else if(id == R.id.action_pending){
            FILTER_STATE = 1;
            sharedPreferences.edit().putInt("FILTER_STATE", FILTER_STATE).apply();
            updateTicketList();
            return true;
        }else if(id == R.id.action_closed){
            FILTER_STATE = 2;
            sharedPreferences.edit().putInt("FILTER_STATE", FILTER_STATE).apply();
            updateTicketList();
            return true;
        }else if(id == R.id.action_logout){
            mAuth.signOut();
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user != null) updateTicketList();
    }
}
