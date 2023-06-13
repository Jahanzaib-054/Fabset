package com.example.fabset;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryFragment extends Fragment {
    private Button dropdownButton1;
    private ListView dropdownListView1;
    private Button dropdownButton2;
    private ListView dropdownListView2;
    private Button dropdownButton3;
    private ListView dropdownListView3;

    private boolean isDropdown1Visible = false;
    private boolean isDropdown2Visible = false;
    private boolean isDropdown3Visible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        dropdownButton1 = view.findViewById(R.id.dropdown_button1);
        dropdownListView1 = view.findViewById(R.id.dropdown_listview1);
        dropdownButton2 = view.findViewById(R.id.dropdown_button2);
        dropdownListView2 = view.findViewById(R.id.dropdown_listview2);
        dropdownButton3 = view.findViewById(R.id.dropdown_button3);
        dropdownListView3 = view.findViewById(R.id.dropdown_listview3);


        dropdownButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDropdown1Visible) {
                    dropdownListView1.setVisibility(View.GONE);
                    isDropdown1Visible = false;
                } else {
                    dropdownListView1.setVisibility(View.VISIBLE);
                    isDropdown1Visible = true;
                }
            }
        });

        dropdownButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDropdown2Visible) {
                    dropdownListView2.setVisibility(View.GONE);
                    isDropdown2Visible = false;
                } else {
                    dropdownListView2.setVisibility(View.VISIBLE);
                    isDropdown2Visible = true;
                }
            }
        });

        dropdownButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDropdown3Visible) {
                    dropdownListView3.setVisibility(View.GONE);
                    isDropdown3Visible = false;
                } else {
                    dropdownListView3.setVisibility(View.VISIBLE);
                    isDropdown3Visible = true;
                }
            }
        });

        // Example data for the dropdown ListViews
        String[] dropdownItems1 = {"Shirts", "Pants","T-Shirts"};
        String[] dropdownItems2 = {"Dresses" , "Frocks"};
        String[] dropdownItems3 = {"Shirts", "Pants", "Sleepwear"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_selectable_list_item, dropdownItems1);
        dropdownListView1.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_selectable_list_item, dropdownItems2);
        dropdownListView2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_selectable_list_item, dropdownItems3);
        dropdownListView3.setAdapter(adapter3);

        dropdownListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ProductsPage.class);
                intent.putExtra("category","Mens");
                intent.putExtra("subcategory",selectedItem);
                Toast.makeText(requireContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                // Execute your code here based on the clicked item
                // ...
            }
        });

        dropdownListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ProductsPage.class);
                intent.putExtra("category","Womens");
                intent.putExtra("subcategory",selectedItem);
                Toast.makeText(requireContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                startActivity(intent);

                // Execute your code here based on the clicked item
                // ...
            }
        });
        dropdownListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ProductsPage.class);
                intent.putExtra("category","Kids");
                intent.putExtra("subcategory",selectedItem);
                Toast.makeText(requireContext(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                startActivity(intent);

                // Execute your code here based on the clicked item
                // ...
            }
        });

        return view;
    }
}