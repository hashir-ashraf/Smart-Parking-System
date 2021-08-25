package com.tss.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

public class GetMobileNumber extends AppCompatActivity {
    private static final String TAG = "tag";
    Button mNext;
    Intent intent;
    String name;
    String email;
    String username;
    String password;
    CountryCodePicker mCountryCodePicker;
    TextInputEditText mEditTextMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_mobile_number);
        init();
       // GetIntentValue();
        mNext.setOnClickListener(v ->
        {
          if(!mEditTextMobileNo.getText().toString().isEmpty() && mEditTextMobileNo.getText().toString().length()==10){
               String mobileNumber = "+"+mCountryCodePicker.getSelectedCountryCode()+mEditTextMobileNo.getText().toString();
              Log.d(TAG,"mobile number"+mobileNumber);
              Toast.makeText(this, "mobile"+mobileNumber, Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(GetMobileNumber.this, VerifyOtpActivity.class);
              intent.putExtra("mobileNo",mobileNumber);
              startActivity(intent);
          }
          else {
              mEditTextMobileNo.setError("Not Valid MobileNumber");
          }

        });
    }
    private void init() {
        mNext = findViewById(R.id.signup_next_button);
        mCountryCodePicker =  findViewById(R.id.country_code_picker);
        mEditTextMobileNo   = findViewById(R.id.mobilenumber);
    }
    private void GetIntentValue() {
        intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
    }
}