package com.example.travaleva.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.model.Flight;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private List<Flight> flightList;

    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.flightNumber.setText(flight.getFlightNumber());
        holder.origin.setText("Origin: " + flight.getOrigin());
        holder.destination.setText("Destination: " + flight.getDestination());
        holder.departureTime.setText("Departure: " + flight.getDepartureTime());
        holder.price.setText("$" + flight.getPrice());
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightNumber, origin, destination, departureTime, price;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumber = itemView.findViewById(R.id.flightNumber);
            origin = itemView.findViewById(R.id.origin);
            destination = itemView.findViewById(R.id.destination);
            departureTime = itemView.findViewById(R.id.departureTime);
            price = itemView.findViewById(R.id.price);
        }
    }
}