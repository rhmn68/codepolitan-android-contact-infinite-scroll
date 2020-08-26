package com.codepolitan.contactinfinitescroll;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepolitan.contactinfinitescroll.adapter.ContactAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean isScroll = true;
    private ContactAdapter contactAdapter = new ContactAdapter();
    private ArrayList<String> dataContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchData(0);
        RecyclerView rvContacts = findViewById(R.id.rvContacts);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvContacts.setLayoutManager(layoutManager);
        rvContacts.setHasFixedSize(true);
        rvContacts.setAdapter(contactAdapter);

        rvContacts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Jumlah data yang ada pada RecyclerView
                final int countItems = layoutManager.getItemCount();
                //Jumlah data yang terlihat di layar
                int currentItems = layoutManager.getChildCount();
                //Index data yang terlihat pertama kali (kalo vertical index data yang berada diatas)
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                int totalScrollItem = currentItems + firstVisiblePosition;

                Log.d("coba", "count items: "+countItems);
                Log.d("coba", "current item: "+currentItems);
                Log.d("coba", "first visible pos: "+firstVisiblePosition);

                if (isScroll && totalScrollItem >= countItems){
                    isScroll = false;
                    contactAdapter.addDataLoading();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contactAdapter.removeDataLoading();
                            fetchData(countItems);
                            isScroll = true;
                        }
                    }, 2000);
                }
            }
        });
    }

    private void fetchData(int countItems) {
        if (dataContacts.size() > 0){
            dataContacts.clear();
        }

        for (int i = countItems; i < countItems + 15; i++){
            dataContacts.add(i + 1 + ". John Doe");
        }
        contactAdapter.addDataContact(dataContacts);
    }

}