package com.ecoentorno.ecomobile;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecoentorno.ecomobile.model.Client;
import com.ecoentorno.ecomobile.util.CSVFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PendingsBillingActivityFragment extends Fragment {

    private List<String[]> pendingList;
    private List<Client> clientList;
    private ListView listView;

    public PendingsBillingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendings_billing, container, false);
        listView = (ListView) view.findViewById(R.id.lvPendings);
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, "04102015.csv");
            InputStream inputStream = new FileInputStream(file);
            CSVFile csvFile = new CSVFile(inputStream);
            pendingList = csvFile.read();
            BillingsItemArrayAdapter adapter = new BillingsItemArrayAdapter(view.getContext(), R.layout.item_pending_client);
            listView.setAdapter(adapter);
            clientList = new ArrayList<>();
            for(String[] p: pendingList){
                Client client = new Client(p);
                adapter.add(client);
                clientList.add(client);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), BillActivity.class);
                    intent.putExtra("CLIENT", clientList.get(position));
                    startActivity(intent);
                }
            });
        }catch (FileNotFoundException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
