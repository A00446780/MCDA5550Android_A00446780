package com.example.HotelAndroidApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HotelSearchFragment extends Fragment {

    View view;
    ConstraintLayout mainLayout;
    TextView titleTextView;
    EditText guestsCountEditText, nameEditText;
    Button searchButton;
    DatePicker checkInDatePicker, checkOutDatePicker;
    String checkInDate, checkOutDate, guestName;
    Integer numberOfGuests;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_search_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainLayout = view.findViewById(R.id.main_layout);
        titleTextView = view.findViewById(R.id.title_text_view);

        guestsCountEditText = view.findViewById(R.id.guests_count_edit_text);

        nameEditText = view.findViewById(R.id.name_edit_text);

        searchButton = view.findViewById(R.id.search_button);

        checkInDatePicker = view.findViewById(R.id.checkin_date_picker_view);
        checkOutDatePicker = view.findViewById(R.id.checkout_date_picker_view);

        titleTextView.setText(R.string.welcome_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInDate = getDateFromCalendar(checkInDatePicker);
                checkOutDate = getDateFromCalendar(checkOutDatePicker);
                numberOfGuests = Integer.valueOf(guestsCountEditText.getText().toString());
                guestName = nameEditText.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("check in date", checkInDate);
                bundle.putString("check out date", checkOutDate);
                bundle.putInt("number of guests", numberOfGuests);

                sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("guestName", guestName);
                editor.putInt("guestsCount", numberOfGuests);
                editor.putString("checkInDate", checkInDate);
                editor.putString("checkOutDate", checkOutDate);
                editor.commit();

                HotelsListFragment hotelsListFragment = new HotelsListFragment();
                hotelsListFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_layout, hotelsListFragment);
                fragmentTransaction.remove(HotelSearchFragment.this);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    // Function to get the formatted date from DatePicker
    private String getDateFromCalendar(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(calendar.getTime());

        return formattedDate;
    }

}
