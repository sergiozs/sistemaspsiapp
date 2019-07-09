package com.example.ticketapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CreateTicket extends Activity {

    Context context;
    private int floorButton = -1;
    List<Button> FButtons;
    private int typeButton = -1;
    List<Button> TButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_create_ticket);
        FButtons = new ArrayList<>();
        TButtons = new ArrayList<>();
        configFloorButtons();
        configTypeButtons();
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
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(1);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(2);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(3);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(4);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(5);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(6);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(7);
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(8);
            }
        });
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFloorButtons(9);
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
            }
        });
        btnPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(1);
            }
        });
        btnSisged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(2);
            }
        });
        btnSiaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(3);
            }
        });
        btnSaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTypeButtons(4);
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
}
