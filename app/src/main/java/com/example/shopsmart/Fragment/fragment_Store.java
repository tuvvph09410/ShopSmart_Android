package com.example.shopsmart.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopsmart.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_Store#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_Store extends Fragment {




    public fragment_Store() {
        // Required empty public constructor
    }

    public static fragment_Store newInstance() {
        fragment_Store fragment = new fragment_Store();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store, container, false);
    }
}