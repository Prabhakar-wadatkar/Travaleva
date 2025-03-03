package com.example.travaleva.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travaleva.R;
import com.example.travaleva.model.Booking;
import com.example.travaleva.model.Place;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BookingFragment extends Fragment {

    private static final String ARG_PLACE = "place";
    private Place place;

    private MaterialButton buttonStartDate, buttonEndDate, btnPayCard, btnPayUPI, buttonOrderNow;
    private NumberPicker numberPickerAdults, numberPickerChildren;
    private ViewSwitcher paymentSwitcher;
    private RadioGroup paymentMethodGroup;
    private TextView placeTitleTextView, textTotalDays, textTotalAmount;

    private EditText editCardNumber, editExpiryDate, editCVV;

    private String startDateString, endDateString;
    private SharedPreferences sharedPreferences;

    public static BookingFragment newInstance(Place place) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE, place);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            place = (Place) getArguments().getSerializable(ARG_PLACE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupNumberPickers();
        setupPaymentSwitcher();
        setupPayButtonListeners();

        // Retrieve SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Retrieve userId and userName from SharedPreferences
        String userId = sharedPreferences.getString("userId", null);
        String userName = sharedPreferences.getString("name", "Guest");

        // Pass userId and userName to the booking setup
        setupOrderNowButtonListener(userId, userName);

        if (place != null) {
            placeTitleTextView.setText(place.getTitle());
        }
    }

    private void initializeViews(View view) {
        placeTitleTextView = view.findViewById(R.id.Place_title);
        buttonStartDate = view.findViewById(R.id.buttonStartDate);
        buttonEndDate = view.findViewById(R.id.buttonEndDate);
        textTotalDays = view.findViewById(R.id.textTotalDays);
        textTotalAmount = view.findViewById(R.id.textTotalAmount);

        numberPickerAdults = view.findViewById(R.id.numberPickerAdults);
        numberPickerChildren = view.findViewById(R.id.numberPickerChildren);
        paymentSwitcher = view.findViewById(R.id.paymentSwitcher);
        paymentMethodGroup = view.findViewById(R.id.paymentMethodGroup);

        btnPayCard = view.findViewById(R.id.btnPayCard);
        btnPayUPI = view.findViewById(R.id.btnPayUPI);

        editCardNumber = view.findViewById(R.id.editTextCardNumber);
        editExpiryDate = view.findViewById(R.id.editTextExpiryDate);
        editCVV = view.findViewById(R.id.editTextCVV);

        buttonOrderNow = view.findViewById(R.id.buttonOrderNow);

        buttonStartDate.setOnClickListener(v -> showDatePicker(buttonStartDate, true));
        buttonEndDate.setOnClickListener(v -> showDatePicker(buttonEndDate, false));

        setupCardInputFields();
    }

    private void setupNumberPickers() {
        numberPickerAdults.setMinValue(1);
        numberPickerAdults.setMaxValue(10);
        numberPickerAdults.setValue(2);

        numberPickerChildren.setMinValue(0);
        numberPickerChildren.setMaxValue(10);
        numberPickerChildren.setValue(1);
    }

    private void showDatePicker(MaterialButton button, boolean isStartDate) {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker().build();

        picker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String selectedDate = dateFormat.format(selection);
            button.setText(selectedDate);

            if (isStartDate) {
                startDateString = selectedDate; // Store as formatted string
            } else {
                endDateString = selectedDate; // Store as formatted string
            }

            calculateTotalDaysAndAmount();
        });

        picker.show(getChildFragmentManager(), picker.toString());
    }

    private void calculateTotalDaysAndAmount() {
        if (startDateString != null && endDateString != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date startDateObj = dateFormat.parse(startDateString);
                Date endDateObj = dateFormat.parse(endDateString);

                if (startDateObj != null && endDateObj != null && !endDateObj.before(startDateObj)) {
                    long diff = endDateObj.getTime() - startDateObj.getTime();
                    long days = TimeUnit.MILLISECONDS.toDays(diff) + 1;
                    textTotalDays.setText("Total Days: " + days);

                    if (place != null) {
                        double chargesPerDay = Double.parseDouble(place.getCharges());
                        int adults = numberPickerAdults.getValue();
                        int children = numberPickerChildren.getValue();

                        double totalAmount = (adults * chargesPerDay + (children * 0.5 * chargesPerDay)) * days;
                        textTotalAmount.setText("₹" + totalAmount);
                    }
                } else {
                    textTotalDays.setText("Invalid dates selected");
                    textTotalAmount.setText("₹0");
                }
            } catch (Exception e) {
                textTotalDays.setText("Error calculating days");
                textTotalAmount.setText("₹0");
            }
        }
    }

    private void setupPaymentSwitcher() {
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCard) {
                paymentSwitcher.setDisplayedChild(0);
            } else if (checkedId == R.id.radioUPI) {
                paymentSwitcher.setDisplayedChild(1);
            }
        });
    }

    private void setupPayButtonListeners() {
        btnPayCard.setOnClickListener(v -> showPaymentSuccess());
        btnPayUPI.setOnClickListener(v -> showPaymentSuccess());
    }

    private void showPaymentSuccess() {
        Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    private void setupCardInputFields() {
        editCardNumber.addTextChangedListener(new TextWatcher() {
            private boolean isFormatting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFormatting) return;

                isFormatting = true;
                String cleanInput = s.toString().replaceAll("\\s", "");
                StringBuilder formattedInput = new StringBuilder();

                for (int i = 0; i < cleanInput.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        formattedInput.append(" ");
                    }
                    formattedInput.append(cleanInput.charAt(i));
                }

                editCardNumber.setText(formattedInput);
                editCardNumber.setSelection(formattedInput.length());
                isFormatting = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editExpiryDate.addTextChangedListener(new TextWatcher() {
            private boolean isDeleting;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isDeleting = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isDeleting) return;

                String input = s.toString();
                if (input.length() == 2 && !input.contains("/")) {
                    editExpiryDate.setText(input + "/");
                    editExpiryDate.setSelection(editExpiryDate.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editCVV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3) {
                    editCVV.setText(s.subSequence(0, 3));
                    editCVV.setSelection(3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private String generateTicketNumber() {
        Random random = new Random();

        // Generate first 3 capital letters
        String firstPart = "" + (char) ('A' + random.nextInt(26)) + (char) ('A' + random.nextInt(26)) + (char) ('A' + random.nextInt(26));

        // Generate 5-digit number
        String numberPart = String.format("%05d", random.nextInt(100000));

        // Generate last 2 capital letters
        String lastPart = "" + (char) ('A' + random.nextInt(26)) + (char) ('A' + random.nextInt(26));

        return firstPart + numberPart + lastPart;
    }

    private void setupOrderNowButtonListener(String userId, String userName) {
        buttonOrderNow.setOnClickListener(v -> {
            // Ensure userId is not null
            if (userId == null) {
                Toast.makeText(getContext(), "You must be logged in to make a booking", Toast.LENGTH_SHORT).show();
                return;
            }

            String placeName = place != null ? place.getTitle() : "Unknown Place";
            double totalAmount = Double.parseDouble(textTotalAmount.getText().toString().replace("₹", ""));
            String paymentMethod = paymentMethodGroup.getCheckedRadioButtonId() == R.id.radioCard ? "Card" : "UPI";

            Booking.PaymentDetails paymentDetails = null;
            if ("Card".equals(paymentMethod)) {
                String cardNumber = editCardNumber.getText().toString();
                String expiryDate = editExpiryDate.getText().toString();
                String cvv = editCVV.getText().toString();
                paymentDetails = new Booking.PaymentDetails(cardNumber, expiryDate, cvv);
            }

            // Generate a unique ticket number
            String ticketNumber = generateTicketNumber();

            // Create a Booking object with string dates
            Booking booking = new Booking(
                    userId, // Pass the userId from SharedPreferences
                    place != null ? place.getId() : "",
                    startDateString, endDateString,  // Pass formatted date strings
                    numberPickerAdults.getValue(), numberPickerChildren.getValue(),
                    totalAmount, paymentMethod, paymentDetails, userName, placeName
            );

            // Set the generated ticket number
            booking.setTicketNumber(ticketNumber);

            // Save the booking to Firebase under the userId node
            DatabaseReference bookingRef = FirebaseDatabase.getInstance()
                    .getReference("bookings")
                    .child(userId); // Nest bookings under userId

            // Generate a unique booking ID
            String bookingId = bookingRef.push().getKey();

            // Save the booking with the generated bookingId
            bookingRef.child(bookingId).setValue(booking)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showTicketDialog(ticketNumber); // Show ticket dialog
                        } else {
                            Toast.makeText(getContext(), "Booking Failed. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void showTicketDialog(String ticketNumber) {
        // Create and show the ticket dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Booking Confirmed");
        builder.setMessage("Your booking is confirmed!\nTicket Number: " + ticketNumber);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}