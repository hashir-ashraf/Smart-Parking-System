package com.tss.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.tss.smartparking.SignUP.userpref;

public class SlotsReservations extends AppCompatActivity {
    int slotid;
    TextInputEditText numberplate;
    String numberplate_text;
    int driver_id;
    public  static  String pref="pref";
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots_reservations);
        numberplate = findViewById(R.id.numberplate_id);
        numberplate_text = numberplate.getText().toString();
        bt = findViewById(R.id.reseverbt);
        Intent intent = getIntent();
        slotid=intent.getIntExtra("id",1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsNotEmpty()){
                    UploadReserved();
                }
                else {
                    Toast.makeText(SlotsReservations.this, "Enter Value", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private Boolean IsNotEmpty() {
        numberplate_text= numberplate.getText().toString();

        if (numberplate_text.isEmpty()) {
            Toast.makeText(this, "Enter The Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void UploadReserved() {
        int random = ThreadLocalRandom.current().nextInt(1000, 2000);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("key",random );
        editor.apply();
        SharedPreferences sharedPreferences1 = getSharedPreferences(userpref, MODE_PRIVATE);
        driver_id = sharedPreferences1.getInt("id", 0);
        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.174.129:8000/reservation_api/"+random+"&"+numberplate_text+"&"+driver_id+"&"+slotid   , response -> {
        }, error ->

                Toast.makeText(this, "errorrr"+ error.getMessage(), Toast.LENGTH_SHORT).show());
        request.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        request.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}