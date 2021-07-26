package com.example.HotelAndroidApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//  This fragment displays booking information and asks for guests details inputs from the users.
public class HotelGuestDetailsFragment extends Fragment {

    View view;
    SharedPreferences sharedPreferences;
    Integer gCount=0;
    String gName="";
    String hotelName;
    String hotelPrice;
    String checkInDate="";
    String checkOutDate="";
    LinearLayout guestsDetailsLinearLayout;
    Button confirmReservationButton;
//    TextView testTextView;    for making sure that user inputs are being read correctly
    List<Guests> guestsList;
    ReservationDetails reservationDetails;
    Confirmation reservationConfirmation;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_guest_details_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guestsList=new ArrayList<Guests>();

//        Taking values from shared preferences
        sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("guestName")) {
            gName=sharedPreferences.getString("guestName", "");
        }
        if (sharedPreferences.contains("guestsCount")) {
            gCount=sharedPreferences.getInt("guestsCount", 0);
        }
        if (sharedPreferences.contains("checkInDate")) {
            checkInDate=sharedPreferences.getString("checkInDate", "");
        }
        if (sharedPreferences.contains("checkOutDate")) {
            checkOutDate=sharedPreferences.getString("checkOutDate", "");
        }
        TextView hotelRecapTextView = view.findViewById(R.id.hotel_recap_text_view);

//        Taking values from the passed bundle
        hotelName = getArguments().getString("hotel name");
        hotelPrice = getArguments().getString("hotel price");

        hotelRecapTextView.setText("Booking for: " +gName +"\nSelected Hotel: " +hotelName+ "\nPrice: $ "+hotelPrice+ " \nNumber of Guests staying: "+ gCount + "\n\nPlease provide the guests details:");

//        Generating Guests details inputs based on the number of guests
        guestsDetailsLinearLayout= view.findViewById(R.id.guests_details_linear_layout);
        for(int i =0; i < gCount; i++){
            int j = i+1;
            EditText guestName= new EditText(getActivity());
            guestName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            guestName.setHint("Guest " + j + " Name");
            guestsDetailsLinearLayout.addView(guestName);

            EditText guestGender= new EditText(getActivity());
            guestGender.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            guestGender.setHint("Guest " + j + " Gender");
            guestsDetailsLinearLayout.addView(guestGender);
        }
        confirmReservationButton=view.findViewById(R.id.confirm_reservation_button);
//        testTextView = view.findViewById(R.id.test_textView);
//        testTextView.setText("");

        confirmReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Reading from input fields
                for(int i = 0; i < 2*gCount; i+=2){
                    EditText gNameET = (EditText) guestsDetailsLinearLayout.getChildAt(i);
                    String gName = gNameET.getText().toString();
                    EditText gGenderET = (EditText) guestsDetailsLinearLayout.getChildAt(i+1);
                    String gGender = gGenderET.getText().toString();
//                    testTextView.append(gName);
//                    testTextView.append(" "+gGender+" ");
                    Guests guest = new Guests(gName, gGender);
                    guestsList.add(guest);
                }
                reservationDetails = new ReservationDetails(hotelName,checkInDate,checkOutDate,guestsList);
                ApiInterface api = Api.getClient();
                Call<Confirmation> postCall = api.getConfirmation(reservationDetails);
                postCall.enqueue(new Callback<Confirmation>() {
                    @Override
                    public void onResponse(Call<Confirmation> call, Response<Confirmation> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(getActivity(), response.code(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        reservationConfirmation=response.body();
//                        testTextView.append(" "+reservationConfirmation.getConfirmation_number()+" ");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("confirmation_number", reservationConfirmation.getConfirmation_number());
                        editor.commit();


                        ConfirmationFragment confirmationFragment = new ConfirmationFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_layout, confirmationFragment);
                        fragmentTransaction.remove(HotelGuestDetailsFragment.this);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<Confirmation> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
