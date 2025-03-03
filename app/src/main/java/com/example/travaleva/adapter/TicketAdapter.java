package com.example.travaleva.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travaleva.R;
import com.example.travaleva.model.Ticket;
import java.util.List;
import java.text.NumberFormat; // For currency formatting

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> ticketList;

    public TicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.bind(ticket); // Bind data to views
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView textPlaceName, placeCity, textDates, placeCost, textTicketNumber;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from item_ticket.xml
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
            placeCity = itemView.findViewById(R.id.placeCity);
            textDates = itemView.findViewById(R.id.textDates);
            placeCost = itemView.findViewById(R.id.placeCost); // Ensure this ID matches your XML
            textTicketNumber = itemView.findViewById(R.id.textTicketNumber);
        }

        public void bind(Ticket ticket) {
            // Bind data to views
            textPlaceName.setText(ticket.getPlaceName());
            placeCity.setText(ticket.getCity());
            textDates.setText(ticket.getDate());

            // Format totalAmount as currency (e.g., "₹10,000")
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0); // Remove decimal places
            String formattedAmount = format.format(ticket.getTotalAmount());
            placeCost.setText("₹" + (int) ticket.getTotalAmount());

            textTicketNumber.setText(ticket.getTicketNumber());
        }
    }
}
