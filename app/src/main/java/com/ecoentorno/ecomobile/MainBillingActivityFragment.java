package com.ecoentorno.ecomobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ecoentorno.ecomobile.util.CSVFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainBillingActivityFragment extends Fragment {

    private Button btPending;

    public MainBillingActivityFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewBilling =  inflater.inflate(R.layout.fragment_main_billing, container, false);
        btPending = (Button) viewBilling.findViewById(R.id.btPending);
        btPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(), PendingsBillingActivity.class));
            }
        });
        return viewBilling;
    }
}
