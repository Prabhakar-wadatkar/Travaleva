package com.example.travaleva.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.DetailsActivity;
import com.example.travaleva.R;
import com.example.travaleva.model.RecentsData;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentsViewHolder> {

    private Context context;
    private List<RecentsData> recentsDataList;

    public RecentsAdapter(Context context, List<RecentsData> recentsDataList) {
        this.context = context;
        this.recentsDataList = recentsDataList;
    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recents_row_item, parent, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder holder, int position) {
        RecentsData currentItem = recentsDataList.get(position);

        // Bind data to views
        holder.placeName.setText(currentItem.getTitle());
        holder.city.setText(currentItem.getCity());  // Correctly bind the city
        holder.price.setText("From " + currentItem.getCharges());  // Set charges

        // Use Glide to load image from URL
        Glide.with(context)
                .load(currentItem.getImageUrl())  // Load image from URL
                .into(holder.placeImage);  // Set it into ImageView
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Passing extra data to DetailsActivity
//                Intent i = new Intent(context, DetailsActivity.class);
//                i.putExtra("placeTitle", currentItem.getTitle());
//                i.putExtra("placeDescription", currentItem.getDescription());
//                context.startActivity(i);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return recentsDataList.size();
    }

    public static class RecentsViewHolder extends RecyclerView.ViewHolder {

        ImageView placeImage;
        TextView placeName, city, price;

        public RecentsViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            city = itemView.findViewById(R.id.place_location);  // Use place_location for city
            price = itemView.findViewById(R.id.placeCharges);  // Use placeCharges for price
        }
    }
}
