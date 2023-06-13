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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignupFragment extends Fragment {
    Boolean emailExist = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        EditText Username = view.findViewById(R.id.username);
        EditText Password = view.findViewById(R.id.password);
        EditText Phone = view.findViewById(R.id.phone);
        EditText Address = view.findViewById(R.id.address);
        EditText Email = view.findViewById(R.id.email);
        Button Submit = view.findViewById(R.id.submit);
        SharedPreferences SP;
        SP = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Username.getText().toString();
                String password = Password.getText().toString();
                String phone = Phone.getText().toString();
                String address = Address.getText().toString();
                String email = Email.getText().toString();
                Intent intent = new Intent(getActivity(), Form.class);
                if(!username.isEmpty() && !password.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !email.isEmpty())
                {
                    if (!(username.length() > 3)){
                        Toast.makeText(getActivity(), "Username must be atleast 3 characters long", Toast.LENGTH_SHORT).show();
                    } else if (!(password.length() >= 6 )) {
                        Toast.makeText(getActivity(), "Password must be 6 characters long", Toast.LENGTH_SHORT).show();
                    } else if (!(phone.length() == 11)) {
                        Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    } else if(!(email.contains("@"))){
                        Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    } else {
                        User userdata = new User(username,password,email,address,phone);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference userRef = database.getReference().child("Users");
                        Query query = userRef.orderByChild("email").equalTo(email);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Node(s) with the specified email exist(s)
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        // Access the node(s) with the specified email
                                        User user = snapshot.getValue(User.class);
                                        // Perform actions with the user data

                                    }
                                    emailExist = true;
                                } else {
                                    // No node with the specified email exists
                                    emailExist = false;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Error occurred while retrieving data
                                System.out.println("Error: " + databaseError.getMessage());
                            }
                        });
                        if(!emailExist){
                            database.getReference().child("Users").push().setValue(userdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                editor.putString("Userid",userRef.getKey());
                                                editor.putString("Name", username);
                                                editor.putString("Email", email);
                                                editor.putString("Cart", "");
                                                editor.putBoolean("isLogin", true);
                                                editor.commit();
                                                Toast.makeText(getActivity(), "Signup Successfull", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(getContext(), "Email Already used !!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}