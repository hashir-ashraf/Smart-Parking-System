package com.tss.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ThreadLocalRandom;

import static com.tss.smartparking.Constatnt.MAIN;

public class SignUP extends AppCompatActivity {
    public static String userpref = "USER_PREF";
    Button mSignUp;
    TextInputEditText mFirstNameET;
    TextInputEditText mLastNameET;
    TextInputEditText mPasswordEt;
    TextInputEditText mEmailEt;
    String name;
    TextInputEditText mMobile;
    String mobile;
    String email;
    String mLastName;
    String password;
    int random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p2);
        init();
        mSignUp.setOnClickListener(v ->
        {
            if (IsNotEmpty()) {
                SavedValueInSharedPref();
                User_register();
                Intent intent = new Intent(SignUP.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void init() {
        mSignUp = findViewById(R.id.btsignUp);
        mFirstNameET = findViewById(R.id.signup_fullname_id);
        mLastNameET = findViewById(R.id.sginup_username_id);
        mEmailEt = findViewById(R.id.signuemail_id);
        mPasswordEt = findViewById(R.id.signup_password_id);
        mMobile=findViewById(R.id.mobileid_get);
    }
    private Boolean IsNotEmpty() {
        name = mFirstNameET.getText().toString();
        mLastName = mLastNameET.getText().toString();
        email = mEmailEt.getText().toString();
        password = mPasswordEt.getText().toString();
        mobile = mMobile.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter The Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mLastName.isEmpty()) {
            Toast.makeText(this, "Enter LastName", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mobile.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void SavedValueInSharedPref() {
     random = ThreadLocalRandom.current().nextInt(1000, 2000);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(userpref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fname", name);
        editor.putString("lname", mLastName);
        editor.putString("email", email);
        editor.putString("pas", password);
        editor.putString("mobile", mobile);
        editor.putInt("id", random);
        editor.apply();
    }
    private void User_register() {
        StringRequest request = new StringRequest(Request.Method.GET, MAIN+"driver_api/"+random+"&"+mobile+"&"+name+"&"+email+"&"+password
                , response -> {
        }, error -> Toast.makeText(this, "errorrr", Toast.LENGTH_SHORT).show());
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