package com.example.projedeneme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;

public class BarcodeFragment extends Fragment {

    private MainActivity activity;

    public BarcodeFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_barcode, container, false);

        activity = (MainActivity) getActivity();
        AppCompatButton btnScan = rootView.findViewById(R.id.btnScan);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator barcodeScanner = new IntentIntegrator(activity);
                barcodeScanner.setBeepEnabled(false);
                barcodeScanner.setOrientationLocked(false);
                barcodeScanner.setPrompt("Align the barcode inside the view area");
                barcodeScanner.initiateScan();
            }
        });



        return rootView;
    }
}