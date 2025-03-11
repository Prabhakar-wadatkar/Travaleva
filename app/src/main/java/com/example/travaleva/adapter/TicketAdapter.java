package com.example.travaleva.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travaleva.R;
import com.example.travaleva.model.Ticket;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.NumberFormat;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> ticketList;
    private String currentUserId; // To identify the user's tickets in Firebase
    private SharedPreferences sharedPreferences; // To store canceled tickets
    private Context context;

    public TicketAdapter(List<Ticket> ticketList, String currentUserId, Context context) {
        this.ticketList = ticketList;
        this.currentUserId = currentUserId;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("CanceledTickets", Context.MODE_PRIVATE);
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
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView textPlaceName, placeCity, textDates, placeCost, textTicketNumber;
        private Button buttonCancelTicket;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlaceName = itemView.findViewById(R.id.textPlaceName);
            placeCity = itemView.findViewById(R.id.placeCity);
            textDates = itemView.findViewById(R.id.textDates);
            placeCost = itemView.findViewById(R.id.placeCost);
            textTicketNumber = itemView.findViewById(R.id.textTicketNumber);
            buttonCancelTicket = itemView.findViewById(R.id.buttonCancelTicket);

            // Set click listener for the "Cancel" button
            buttonCancelTicket.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showConfirmationDialog(position);
                }
            });
        }

        private void showConfirmationDialog(int position) {
            Context context = itemView.getContext();
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Ticket")
                    .setMessage("Are you sure you want to cancel this ticket?")
                    .setPositiveButton("Yes", (dialog, which) -> cancelTicket(position))
                    .setNegativeButton("No", null)
                    .show();
        }

        private void cancelTicket(int position) {
            Ticket ticket = ticketList.get(position);
            String ticketId = ticket.getTicketNumber(); // Use ticket number as the unique ID

            // Log the Firebase path
            Log.d("FirebasePath", "Deleting ticket at: bookings/" + currentUserId + "/" + ticketId);

            // Remove the ticket from Firebase
            DatabaseReference ticketRef = FirebaseDatabase.getInstance()
                    .getReference("bookings")
                    .child(currentUserId)
                    .child(ticketId);

            ticketRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Mark the ticket as canceled in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ticketId, "canceled"); // Save ticketId with status "canceled"
                    editor.apply();

                    // Remove the ticket from the list and notify the adapter
                    ticketList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(itemView.getContext(), "Ticket canceled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(itemView.getContext(), "Failed to cancel ticket", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bind(Ticket ticket) {
            textPlaceName.setText(ticket.getPlaceName());
            placeCity.setText(ticket.getCity());
            textDates.setText(ticket.getDate());

            // Format totalAmount as currency
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            String formattedAmount = format.format(ticket.getTotalAmount());
            placeCost.setText("₹" + (int) ticket.getTotalAmount());

            textTicketNumber.setText(ticket.getTicketNumber());
        }
    }
}