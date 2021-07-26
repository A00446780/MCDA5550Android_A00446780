package com.example.HotelAndroidApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelsListFragment extends Fragment implements ItemClickListener {

    View view;
    TextView headingTextView;
    ProgressBar progressBar;
    List<HotelListData> userListResponseData;
    SharedPreferences sp;
    String uname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if (sp.contains("guestName")) {
            uname=sp.getString("guestName", "");
        }

        headingTextView = view.findViewById(R.id.heading_text_view);
        progressBar = view.findViewById(R.id.progress_bar);

        String checkInDate = getArguments().getString("check in date");
        String checkOutDate = getArguments().getString("check out date");
        Integer numberOfGuests = getArguments().getInt("number of guests");

        //Set up the header
        headingTextView.setText("Welcome "+uname+", displaying hotel for " + numberOfGuests + " guests staying from " + checkInDate +
                " to " + checkOutDate);

        getHotelsListsData();
    }


    private void getHotelsListsData() {
        progressBar.setVisibility(View.VISIBLE);
//        GET request using Retrofit 2
        ApiInterface api = Api.getClient();
        Call<List<HotelListData>> getCall = api.getHotelsList();
        getCall.enqueue(new Callback<List<HotelListData>>() {
            @Override
            public void onResponse(Call<List<HotelListData>> call, Response<List<HotelListData>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                userListResponseData = response.body();
                setupRecyclerView();
            }

            @Override
            public void onFailure(Call<List<HotelListData>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView() {
        progressBar.setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.hotel_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HotelListAdapter hotelListAdapter = new HotelListAdapter(getActivity(), userListResponseData);
        recyclerView.setAdapter(hotelListAdapter);

        hotelListAdapter.setClickListener(this);
    }


    @Override
    public void onClick(View view, int position) {
        HotelListData hotelListData = userListResponseData.get(position);

        String hotelName = hotelListData.getHotel_name();
        String price = hotelListData.getPrice();
        String availability = hotelListData.getAvailability();

        Bundle bundle = new Bundle();
        bundle.putString("hotel name", hotelName);
        bundle.putString("hotel price", price);
        bundle.putString("hotel availability", availability);

        HotelGuestDetailsFragment hotelGuestDetailsFragment = new HotelGuestDetailsFragment();
        hotelGuestDetailsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(HotelsListFragment.this);
        fragmentTransaction.replace(R.id.main_layout, hotelGuestDetailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

    }
}
