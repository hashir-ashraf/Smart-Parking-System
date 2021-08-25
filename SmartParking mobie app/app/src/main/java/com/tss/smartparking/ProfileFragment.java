package com.tss.smartparking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import spencerstudios.com.bungeelib.Bungee;

import static android.content.Context.MODE_PRIVATE;
import static com.tss.smartparking.SignUP.userpref;

public class ProfileFragment extends Fragment {

    View view;
    Context mContext;
    String name;
    String email;
    String mLastName;
    TextView tvName;
    TextView tvtrack;
    CardView settings;
    ImageButton aprovedbtn;
    LinearLayout allrecords, getsuuportus,privacy,changelanguage,logout;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        aprovedbtn =view.findViewById(R.id.imageButton_verify);
        SharedPreferences sharedPreferences =mContext.getSharedPreferences(userpref,MODE_PRIVATE);
        email = sharedPreferences.getString("email", "email");
        name = sharedPreferences.getString("fname","fname");
        mLastName  = sharedPreferences.getString("lname","lastname");
        tvName.setText(name+" "+mLastName);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Settings.class);
                mContext.startActivity(intent);
                Bungee.inAndOut(mContext);
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
          aprovedbtn.setVisibility(View.VISIBLE);
        }
        else {
            aprovedbtn.setVisibility(View.GONE);
        }
        allrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, YourSlots.class);
                mContext.startActivity(intent);
                Bungee.inAndOut(mContext);
            }
        });
        getsuuportus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GetSupports.class);
                mContext.startActivity(intent);
                Bungee.inAndOut(mContext);
            }
        });
        changelanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChangeLanguage.class);
                mContext.startActivity(intent);
                Bungee.inAndOut(mContext);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://homebuilder.bellinointernational.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                Bungee.inAndOut(mContext);
            }
        });
       tvtrack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mContext, GetMobileNumber.class);
               mContext.startActivity(intent);
               Bungee.inAndOut(mContext);
           }
       });
        return view;
    }
    private  void init(){
        tvName = view.findViewById(R.id.username);
        settings = view.findViewById(R.id.settingmore);
        allrecords = view.findViewById(R.id.records);
        getsuuportus = view.findViewById(R.id.getsupportsus);
        privacy= view.findViewById(R.id.privacy_id);
        changelanguage = view.findViewById(R.id.changeLanguage_id);
        logout = view.findViewById(R.id.logout_id);
        tvtrack = view.findViewById(R.id.tvtrc);
    }
}