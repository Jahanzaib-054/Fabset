package com.example.fabset;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private CustomAdapter adapter;
    private ListView listView;
    private List<CartItem> dataList;
//    private int Total;
//    private TextView TotalView;
    private DatabaseReference databaseRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        Button Checkout = view.findViewById(R.id.Checkout);
        ListView listView = view.findViewById(R.id.ListView);
//        TotalView = view.findViewById(R.id.total);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        };

        SharedPreferences SP;
        SP = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        String node = SP.getString("CartId", "");
        Boolean Flag = SP.getBoolean("isLogin", false);

        List<CartItem> dataList = new ArrayList<>();

        // Add a ValueEventListener to retrieve data from Firebase
        if(node.isEmpty()){
            Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            databaseRef = FirebaseDatabase.getInstance().getReference(node);
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataList.clear(); // Clear the existing data in the list

                    // Iterate through the dataSnapshot to retrieve each child node
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve the data and create a MyData object
                        String text1 = snapshot.child("name").getValue(String.class);
                        String text2 = snapshot.child("price").getValue(String.class);
                        String text3 = snapshot.child("quantity").getValue(String.class);
                        String text4 = snapshot.child("size").getValue(String.class);
//                    Total = Total + Integer.parseInt(text2);
                        CartItem myData = new CartItem(text1, text2, text3, text4);

                        // Add the MyData object to the dataList
                        dataList.add(myData);
                    }

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if any
                    Log.e("CartFragment", "Firebase Database Error: " + databaseError.getMessage());
                }
            });
        }

        adapter = new CustomAdapter(getContext(), dataList);
        listView.setOnItemClickListener(itemClickListener);
        listView.setAdapter(adapter);
//        TotalView.setText("Total Price: "+Integer.toString(Total));

        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                Toast.makeText(getActivity(), "Order Placed", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        return view;
    }
}