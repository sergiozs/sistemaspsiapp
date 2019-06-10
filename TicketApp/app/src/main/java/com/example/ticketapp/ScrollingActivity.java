package com.example.ticketapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ticketapp.Model.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";
    Context context;
    List<Ticket> tickets = new ArrayList<>();
    TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CreateTicket.class);
                startActivity(intent);
            }
        });

    }

    public void updateTicketList(){
        db.collection("ticket")
                .whereEqualTo("activo", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String,Object> aTicket;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                aTicket = document.getData();
                                Timestamp tFecha     = (Timestamp) aTicket.get("time");
                                Ticket auxT = new Ticket(document.getId(),
                                        aTicket.get("user").toString(),
                                        "Piso "+aTicket.get("floor").toString(),
                                        aTicket.get("type").toString(),
                                        (boolean) aTicket.get("activo"),
                                        tFecha);
                                tickets.add(auxT);
                                adapter.notifyDataSetChanged();
                                Log.d("newt", Integer.toString(tickets.size()));
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

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(context, "CARDS!", Toast.LENGTH_LONG).show();
        tickets.clear();
        RecyclerView recycler = findViewById(R.id.my_recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TicketAdapter(recycler, this, tickets);
        recycler.setAdapter(adapter);

        updateTicketList();
    }
}
