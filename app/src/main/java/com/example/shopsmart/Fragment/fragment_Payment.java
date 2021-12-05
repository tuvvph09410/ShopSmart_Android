package com.example.shopsmart.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopsmart.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_Payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Payment extends Fragment {



    public fragment_Payment() {
        // Required empty public constructor
    }


    public static fragment_Payment newInstance() {
        fragment_Payment fragment = new fragment_Payment();

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
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }
}