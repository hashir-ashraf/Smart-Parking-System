package com.tss.smartparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import static com.tss.smartparking.SignUP.userpref;

public class LoginActivity extends AppCompatActivity {
    Button btLogin;
    Button btCreateAccount;
   EditText TDemail;
       EditText TDPas;
    String emailget, passwordget;
    String email, password;
    TextView tvforget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btLogin = findViewById(R.id.btnlogin);
        btCreateAccount= findViewById(R.id.btcreate);
        TDemail = findViewById(R.id.edtLoginMobile);
        TDPas = findViewById(R.id.edtLoginPassword);
        tvforget = findViewById(R.id.tvForgotPass);
        SharedPreferences sharedPreferences = getSharedPreferences(userpref, MODE_PRIVATE);
        emailget = sharedPreferences.getString("email", "email");
        passwordget = sharedPreferences.getString("pas", "0");
        Listner();
    }
    private void Listner(){
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsNotEmpty()) {
                    if (email.contentEquals(emailget) && password.contentEquals(passwordget)) {
                        Intent intent = new Intent(LoginActivity.this, Home_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Your Credential is Wrongs", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUP.class);
                startActivity(intent);
            }
        });
        tvforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Forgetton.class);
                startActivity(intent);
            }
        });
    }
    private Boolean IsNotEmpty() {
        email = TDemail.getText().toString();
        password = TDPas.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }}