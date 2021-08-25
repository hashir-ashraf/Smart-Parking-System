package com.tss.smartparking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.tss.smartparking.Constatnt.MAIN;
import static com.tss.smartparking.Constatnt.SHOWALLAPP;
import static com.tss.smartparking.SignUP.userpref;
import static com.tss.smartparking.SlotsReservations.pref;

public class YourSlots extends AppCompatActivity {
    TextView textView;
    Button bt;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_slots);
        textView = findViewById(R.id.textViewslotsno_id);
        bt = findViewById(R.id._id_btslots_id);
        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        id = sharedPreferences.getInt("key", 0);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnReserve();
                Toast.makeText(YourSlots.this, "Now Your Unreserve", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void UnReserve() {
        StringRequest request = new StringRequest(Request.Method.GET,MAIN+"cancel_reservation_api/"+id, response -> {
            Log.d("testtcode", response);
        }, error ->
                Toast.makeText(this, "Category" + error.getMessage(), Toast.LENGTH_SHORT).show());
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        request.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        //Volley.newRequestQueue(mContex).add(request);
    }
}