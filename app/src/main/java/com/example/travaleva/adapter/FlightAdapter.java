package com.example.travaleva.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.model.Flight;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {

    private List<Flight> flightList;

    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        holder.airlineLogo.setImageResource(flight.getAirlineLogo());
        holder.airlineName.setText(flight.getAirlineName());
        holder.departureTime.setText(flight.getDepartureTime());
        holder.arrivalTime.setText(flight.getArrivalTime());
        holder.duration.setText(flight.getDuration());
        holder.price.setText(flight.getPrice());
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView airlineLogo;
        TextView airlineName;
        TextView departureTime;
        TextView arrivalTime;
        TextView duration;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            airlineLogo = itemView.findViewById(R.id.airlineLogo);
            airlineName = itemView.findViewById(R.id.airlineName);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            duration = itemView.findViewById(R.id.duration);
            price = itemView.findViewById(R.id.price);
        }
    }
}