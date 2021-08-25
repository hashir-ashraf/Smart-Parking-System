package com.tss.smartparking;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.tss.smartparking.Constatnt.MAIN;
import static com.tss.smartparking.Constatnt.SHOWALLAPP;


public class HomeFragment extends Fragment {
    Context mcontext;
    View view;
    List<SlotsItems> slotsItems;
    RecyclerView recyclerView;
    SlotsAdapter slotsAdapter;
    SliderView mSliderView;
    List<SliderItem>  sliderItemList;
    AutoSliderVIewAdapter sliderViewAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext= context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.mainrvslots);
        mSliderView=view.findViewById(R.id.imageSlider);
        LoadSlots();
        LoadSlider();
        return view;
    }
    private void LoadSlots() {
        slotsItems = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET,MAIN+SHOWALLAPP, response -> {
            Log.d("testtcode", response);
            try {
                JSONArray mMainCategory = new JSONArray(response);
                for (int i = 0; i < mMainCategory.length(); i++) {
                    JSONObject postobj = mMainCategory.getJSONObject(i);
                    int id = postobj.getInt("id");
                    int slot_number = postobj.getInt("s_id");
                    String status = postobj.getString("status");
                    slotsItems.add(new SlotsItems(id,slot_number,status));
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                slotsAdapter = new SlotsAdapter(mcontext,slotsItems);
                recyclerView.setAdapter(slotsAdapter);
                slotsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error ->
                Toast.makeText(mcontext, "Category" + error.getMessage(), Toast.LENGTH_SHORT).show());
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        request.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        requestQueue.add(request);
        //Volley.newRequestQueue(mContex).add(request);
    }
   private void  LoadSlider(){
       mSliderView.startAutoCycle();
       mSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
       mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
       mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
       mSliderView.setIndicatorSelectedColor(Color.GREEN);
       mSliderView.setIndicatorUnselectedColor(Color.GRAY);
       mSliderView.setScrollTimeInSec(4);
       sliderItemList = new ArrayList<>();
       sliderItemList.add(new SliderItem(1,R.drawable.p1));
       sliderItemList.add(new SliderItem(1,R.drawable.p2));
       sliderItemList.add(new SliderItem(1,R.drawable.p1));
       sliderViewAdapter = new AutoSliderVIewAdapter(mcontext, sliderItemList);
       mSliderView.setSliderAdapter(sliderViewAdapter);
    }

}