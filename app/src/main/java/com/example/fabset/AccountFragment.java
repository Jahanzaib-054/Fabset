package com.example.fabset;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AccountFragment extends Fragment {

    private Button To_Form;
    private Button Logout;
    private Button Edit_profile;
    private Button Admin;
    private LinearLayout user_layout;
    private ImageView profile_img;

    private Button orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        To_Form = view.findViewById(R.id.to_form);
        user_layout = view.findViewById(R.id.user_account);
        Logout = view.findViewById(R.id.Logout);
        Edit_profile = view.findViewById(R.id.edit_prof);
        TextView greet = view.findViewById(R.id.greeting);
        TextView email = view.findViewById(R.id.email);
        orders = view.findViewById(R.id.my_orders);
        Admin = view.findViewById(R.id.admin);
        profile_img = view.findViewById(R.id.pro_img);

        SharedPreferences SP;
        SP = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        user_layout.setVisibility(View.GONE);
        greet.append(SP.getString("Name", "")+" !!!");
        email.setText(SP.getString("Email",""));

        Boolean Flag = SP.getBoolean("isLogin", false);
        if (Flag){
            To_Form.setVisibility(View.GONE);
            user_layout.setVisibility(View.VISIBLE);
            if(SP.getString("Profile_image","").isEmpty()){
                profile_img.setImageResource(R.drawable.default_avatar);
            }
            else {
                profile_img.setImageBitmap(base64ToBitmap(SP.getString("Profile_image","")));
            }
        }
        else {
            To_Form.setVisibility(View.VISIBLE);
            user_layout.setVisibility(View.GONE);
        }

        To_Form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Form.class);
                startActivity(intent);
            }
        });
        Edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Edit_Profile.class);
                startActivity(intent);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Feature Coming Soon !!!", Toast.LENGTH_SHORT).show();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference cartRef = databaseRef.child("Cart").child(SP.getString("CartId", ""));
                cartRef.removeValue();
                editor.putString("CartId","");
                editor.putBoolean("isLogin", false);
                editor.commit();
                Toast.makeText(getActivity(), "Logout Successfull", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private Bitmap base64ToBitmap(String base64String) {
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}