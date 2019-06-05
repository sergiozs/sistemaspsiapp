package com.example.ticketapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ticketapp.Interface.ILoadMore;
import com.example.ticketapp.Model.Item;
import com.example.ticketapp.Model.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScrollingActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";
    Context context;
    List<Ticket> tickets = new ArrayList<>();
    TicketAdapter adapter;

    public void updateTickets(Ticket ticket){
        this.tickets.add(ticket);
        Log.d("DATA CHECK", Integer.toString(this.tickets.size()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //random10Data();

        db.collection("ticket")
                .whereEqualTo("activo", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String,Object> aTicket = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                aTicket = document.getData();
                                String tTicketid = document.getId();
                                String tUsuario = aTicket.get("user").toString();
                                String tPiso    = aTicket.get("floor").toString();
                                String tTipo    = aTicket.get("type").toString();
                                Timestamp tFecha     = (Timestamp) aTicket.get("time");
                                boolean tActivo  = (boolean) aTicket.get("activo");
                                Ticket auxT = new Ticket(tTicketid,tUsuario,tPiso,tTipo,tActivo,tFecha);
                                updateTickets(auxT);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Ticket auxT2 = new Ticket("tk.1234567890","bob esponja","99","9",true,new Timestamp(new Date()));
        updateTickets(auxT2);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TicketAdapter(recycler, this, tickets);
        recycler.setAdapter(adapter);

        /*adapter.setLoadMore(new ILoadMore(){
            public void onLoadMore(){
                if(items.size() <= 20){
                    items.add(null);
                    adapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            adapter.notifyItemRemoved(items.size());

                            int index = items.size();
                            int end = index + 10;
                            for(int i = index; i<end; i++){
                                String name = UUID.randomUUID().toString();
                                Item item = new Item(name, name.length());
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    }, 5000);
                }else{
                    Toast.makeText(ScrollingActivity.this, "Load data completed!", Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }

/*
    public void random10Data(){
        for(int i = 0; i<10; i++){
            String name = UUID.randomUUID().toString();
            Item item = new Item(name, name.length());
            items.add(item);
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
