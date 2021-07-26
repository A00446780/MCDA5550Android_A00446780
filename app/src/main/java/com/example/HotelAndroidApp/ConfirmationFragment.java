package com.example.HotelAndroidApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Fragment that displays confirmation number
public class ConfirmationFragment extends Fragment {

    View view;
    TextView confirmationTextView;
    TextView confNumber;
    SharedPreferences sp;
    String cno;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.confirmation_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmationTextView = (TextView) view.findViewById(R.id.confirmation_textview);
        confNumber =  (TextView) view.findViewById(R.id.conf_number);
        sp = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if (sp.contains("confirmation_number")) {
            cno=sp.getString("confirmation_number", "");
        }
        confirmationTextView.setText("Success! Your reservation has been confirmed. \nThe confirmation number is: ");
        confNumber.setText(cno);
    }
}
