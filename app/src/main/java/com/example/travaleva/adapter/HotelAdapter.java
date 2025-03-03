package com.example.travaleva.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.R;
import com.example.travaleva.model.Hotel;

import java.util.List;
public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private Context context;

    public HotelAdapter(List<Hotel> hotelList, Context context) {
        this.hotelList = hotelList;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);

        // Set hotel data
        holder.hotelName.setText(hotel.getHotelName());
        holder.hotelDescription.setText(hotel.getAddress() + ", " + hotel.getCity());
        holder.hotelPrice.setText("₹2500"); // Replace with actual price field

        // Set amenities
        StringBuilder amenities = new StringBuilder();
        if(hotel.isBreakfast()) amenities.append("• Breakfast\n");
        if(hotel.isDailyHousekeeping()) amenities.append("• Daily Housekeeping\n");
        if(hotel.isRoomService()) amenities.append("• Room Service\n");
        // Add other amenities

        holder.hotelDescription.append("\n\nAmenities:\n" + amenities.toString());

        // Load image using Glide/Picasso if you have image URLs
        Glide.with(context)
                .load(R.drawable.hotel_image_2) // Add placeholder image
                .into(holder.hotelImage);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView hotelImage;
        TextView hotelName, hotelDescription, hotelPrice;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelDescription = itemView.findViewById(R.id.hotelDescription);
            hotelPrice = itemView.findViewById(R.id.hotelPrice);
        }
    }
}