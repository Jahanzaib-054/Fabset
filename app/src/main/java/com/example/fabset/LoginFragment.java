package com.example.fabset;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        EditText Email = view.findViewById(R.id.email);
        EditText Password = view.findViewById(R.id.password);
        Button Submit = view.findViewById(R.id.submit);
        Intent intent = new Intent(getActivity(),Form.class);
        SharedPreferences SP;
        SP = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String pass = Password.getText().toString();
                if(!email.isEmpty() && !pass.isEmpty())
                {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);

                                // Apply your condition here
                                if (user.getEmail().equals(email)) {
                                    if(user.getPassword().equals(pass)){
                                        Toast.makeText(getContext(), "User found", Toast.LENGTH_SHORT).show();
                                        editor.putString("Name", user.getName());
                                        editor.putString("Email", user.getEmail());
                                        editor.putBoolean("isLogin", true);
                                        editor.commit();
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            // Do something with the filtered productList
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occur
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "Name or password field must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}