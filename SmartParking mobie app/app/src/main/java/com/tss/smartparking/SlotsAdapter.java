
package com.tss.smartparking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.ViewHolder > {

    private Context mContext;
    private List< SlotsItems > builderItems;

    public SlotsAdapter(Context context, List<SlotsItems> builderItems) {
        this.mContext = context;
        this.builderItems = builderItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.builder_items, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SlotsItems items = this.builderItems.get(position);
        if(items.getSlotsStatus().contentEquals("Reserved"))
        {
          // holder.btnr.setBackgroundColor(Color.RED);
           holder.btnr.setVisibility(View.INVISIBLE);

        }
        else{
          //  holder.btnr.setBackgroundColor(Color.YELLOW);
            holder.btnr.setVisibility(View.VISIBLE);
        }
        holder.btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mSlotNo.setText(String.valueOf(items.getSlotsnumber()));
        holder.mStatus.setText(items.getSlotsStatus());
        holder.btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SlotsReservations.class);
                intent.putExtra("id",items.getId());
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return builderItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSlotNo;
        public TextView mStatus;
        public Button btnr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSlotNo= itemView.findViewById(R.id.textViewslotsno);;
            mStatus = itemView.findViewById(R.id.textViewstats);
            btnr =  itemView.findViewById(R.id._id_btslots);
        }
    }


}
