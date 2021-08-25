package com.tss.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {
    Button mVerifyOTP;
    String verificationID;
    PinView pinEnterByUser;
    String mobileNumber;
    ProgressBar mProgressBar;
    Intent mIntent;
    String code;
    String verificationCodeBysystem;
    TextView otpDescription;
    PhoneAuthProvider.ForceResendingToken token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_o_t_p);
        init();
        GetIntent();
        setData();
        mProgressBar.setVisibility(View.GONE);
        sendVerificationCodeToUser(mobileNumber);
        mVerifyOTP.setOnClickListener(v -> {
            String code = pinEnterByUser.getText().toString();

            if (code.isEmpty() || code.length() < 6) {
                Toast.makeText(VerifyOtpActivity.this, "Please Correct Pin", Toast.LENGTH_SHORT).show();
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        });

    }
    // bind the layout with id
    private void init(){
        mVerifyOTP = findViewById(R.id.verify_otp);
        pinEnterByUser = findViewById(R.id.pin_view);
        otpDescription = findViewById(R.id.otp_txt_num);
        mProgressBar = findViewById(R.id.progressBar);
    }
    // get the value from previous activity
    private void GetIntent(){
        mIntent = getIntent();
     mobileNumber =  mIntent.getStringExtra("mobileNo");

    }
    // here the set the value of text
    private void setData(){
        otpDescription.setText("Enter OTP for No"+mobileNumber);
    }
    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Toast.makeText(VerifyOtpActivity.this, "code sent", Toast.LENGTH_SHORT).show();
                    verificationCodeBysystem = s;
                }
                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                      code = phoneAuthCredential.getSmsCode();
                      if(code!=null){
                          mProgressBar.setVisibility(View.VISIBLE);
                          verifyCode(code);
                      }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOtpActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            };
    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBysystem, codeByUser);
        signInTheUserByCredentials(credential);

    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOtpActivity.this, task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(VerifyOtpActivity.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();
                       // Perform Your required action here to either let the user sign In or do something required
                        Intent intent = new Intent(getApplicationContext(), Home_Activity.class);
                        intent.putExtra("mobilenumber",mobileNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(VerifyOtpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}