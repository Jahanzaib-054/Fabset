package com.example.fabset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Form extends AppCompatActivity {
    LoginFragment F = new LoginFragment();
    SignupFragment S = new SignupFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences SP;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Flag",true);
        SP = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean Flag = SP.getBoolean("isLogin", false);
        if (Flag){
            startActivity(intent);
        }
        setContentView(R.layout.activity_form);
        Button login = findViewById(R.id.login_btn);
        Button Signup = findViewById(R.id.signup_btn);
        loadFrag(new LoginFragment());
        int color2 = ContextCompat.getColor(this, R.color.orange_200);
        int color1 = ContextCompat.getColor(this, R.color.orange_500);
        login.setBackgroundColor(color1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup.setBackgroundColor(color2);
                login.setBackgroundColor(color1);
                loadFrag(new LoginFragment());
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setBackgroundColor(color2);
                Signup.setBackgroundColor(color1);
                loadFrag(new SignupFragment());
            }
        });
    }

    public void loadFrag(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.form_layout, fragment);
        ft.commit();
    }
}