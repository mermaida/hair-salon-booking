package com.example.aida.finalproj.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aida.finalproj.Activities.UserActivities.ConfirmationActivity;
import com.example.aida.finalproj.Adapters.ConfirmAdapter;
import com.example.aida.finalproj.Classes.DatabaseHelper;
import com.example.aida.finalproj.Classes.Service;
import com.example.aida.finalproj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aida on 21.04.2018..
 */

public class Fragment3 extends Fragment {

    RecyclerView recview;
    DatabaseHelper db;
    List<Service> list;
    TextView cart;
    double totalprice, totalduration;
    List<String> servicelist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        recview = view.findViewById(R.id.service_list);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recview.setLayoutManager(llm);

        list = new ArrayList<>();

        db = new DatabaseHelper(getActivity());
        list.addAll(db.getAllServices());

        ConfirmAdapter confirmAdapter = new ConfirmAdapter(getActivity(), list);
        recview.setAdapter(confirmAdapter);

        cart = view.findViewById(R.id.cart);

        if(llm.getItemCount() == 0 ){
            cart.setVisibility(View.VISIBLE);
        }

        List<Service> list = new ArrayList<>();
        list.addAll(db.getAllServices());

        totalprice = 0;
        totalduration = 0;
        for(int i = 0; i < list.size(); i++) {
            double price = list.get(i).getPrice();
            totalprice += price;

            double duration = list.get(i).getDuration();
            totalduration += duration;
        }

        String price = Double.toString(totalprice);
        String duration = Double.toString(totalduration);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("price", price);
        editor.putString("duration", duration);
        editor.commit();

        confirmAdapter.notifyDataSetChanged();

        return view;
    }
}
