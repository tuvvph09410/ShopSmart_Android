package com.example.shopsmart.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopsmart.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Account extends Fragment {


    public fragment_Account() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_Account newInstance() {
        fragment_Account fragment = new fragment_Account();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}