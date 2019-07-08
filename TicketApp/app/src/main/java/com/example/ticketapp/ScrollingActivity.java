package com.example.ticketapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import com.example.ticketapp.Model.TicketAlt;
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
    List<TicketAlt> tickets = new ArrayList<>();
    TicketAdapterAlt adapter;
    int FILTER_STATE = 1;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        sharedPreferences = getPreferences(MODE_PRIVATE);
        FILTER_STATE = sharedPreferences.getInt("FILTER_STATE", 0);
        setContentView(R.layout.activity_write);
        //updateUI(user);
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
                    Intent intent = new Intent(context, CreateTicketAlt.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            recycler = findViewById(R.id.my_recycler_view);
            recycler.setLayoutManager(new LinearLayoutManager(context));
            adapter = new TicketAdapterAlt(context, tickets);
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
        recycler.setAdapter(adapter);
        tickets.clear();
        db.collection("ticket").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String validate = document.get("state").toString();
                                switch(FILTER_STATE){
                                    case 0: {
                                        if(validate.equals("pending") || validate.equals("claimed")){
                                            TicketAlt auxT = getTicketData(document.getId(), document.getData());
                                            tickets.add(auxT);
                                        }
                                        break;
                                    }
                                    case 1: {
                                        if(validate.equals("pending")){
                                            TicketAlt auxT = getTicketData(document.getId(), document.getData());
                                            tickets.add(auxT);
                                        }
                                        break;
                                    }
                                    case 2: {
                                        if(validate.equals("claimed")){
                                            TicketAlt auxT = getTicketData(document.getId(), document.getData());
                                            tickets.add(auxT);
                                        }
                                        break;
                                    }
                                    case 3: {
                                        if(validate.equals("closed") || validate.equals("cancelled")){
                                            TicketAlt auxT = getTicketData(document.getId(), document.getData());
                                            tickets.add(auxT);
                                        }
                                        break;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                            adapter.notifyDataSetChanged();
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
            updateTicketList();
            return true;
        }else if(id == R.id.action_pending){
            FILTER_STATE = 1;
            sharedPreferences.edit().putInt("FILTER_STATE", FILTER_STATE).apply();
            updateTicketList();
            return true;
        }else if(id == R.id.action_active){
            FILTER_STATE = 2;
            sharedPreferences.edit().putInt("FILTER_STATE", FILTER_STATE).apply();
            updateTicketList();
            return true;
        }else if(id == R.id.action_closed){
            FILTER_STATE = 3;
            sharedPreferences.edit().putInt("FILTER_STATE", FILTER_STATE).apply();
            updateTicketList();
            return true;
        }else if(id == R.id.action_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("¿Desea cerrar sesión?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    mAuth.signOut();
                    recreate();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if(user != null) updateTicketList();
    }

    public TicketAlt getTicketData(String tkid, Map<String, Object> aTicket){
        String ticketid = tkid;
        Timestamp time_begin = (Timestamp)aTicket.get("time_begin");
        String created_by = aTicket.get("created_by").toString();
        String user = aTicket.get("user").toString();
        String floor = aTicket.get("floor").toString();
        String type = aTicket.get("type").toString();
        String state = aTicket.get("state").toString();
        String description = null;
        Timestamp time_claimed = null;
        String claimed_by = null;
        Timestamp time_end = null;
        String post_mortem = null;
        if(aTicket.get("description") != null){
            description = aTicket.get("description").toString();
        }
        if(aTicket.get("time_claimed") != null){
            time_claimed = (Timestamp)aTicket.get("time_claimed");
            claimed_by = aTicket.get("claimed_by").toString();
        }
        if(aTicket.get("time_end") != null){
            time_end = (Timestamp) aTicket.get("time_end");
        }
        if(aTicket.get("post_mortem") != null){
            post_mortem = aTicket.get("post_mortem").toString();
        }

        TicketAlt ticketObj = new TicketAlt(ticketid, time_begin, created_by, user, floor, type,
                                            description, state, time_claimed, claimed_by, time_end, post_mortem);

        return ticketObj;
    }

    /*public void updateUser(String newName) {
        final String mName = newName;
        String password = "1234";
        String email = mName.concat("@psi.gob.pe");
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mName).build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("USER.M", "User profile updated.");
                                    }
                                }
                            });
                }
            }
        });
    }*/
}
