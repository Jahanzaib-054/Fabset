package com.example.fabset;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Edit_Profile extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 200;
    private Bitmap imageBitmap;
    User userdata;
    Uri imageUri;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ImageButton top_back = findViewById(R.id.top_back);
        Button select_img = findViewById(R.id.selectImageButton);
        Button save_change = findViewById(R.id.submit);
        Button back = findViewById(R.id.go_back);
        ImageView prof_img = findViewById(R.id.pro_img);
        EditText Username = findViewById(R.id.username);
        EditText Password = findViewById(R.id.password);
        EditText Phone = findViewById(R.id.phone);
        EditText Address = findViewById(R.id.address);

        Intent intent = new Intent(Edit_Profile.this, MainActivity.class);

        SharedPreferences SP;
        SP = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        String old_user = SP.getString("Name","");
        String old_em = SP.getString("Email","");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userdata = new User();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     User user = snapshot.getValue(User.class);

                    // Apply your condition here
                    if (user.getEmail().equals(old_em)){
                        Username.setText(user.getName());
                        Password.setText(user.getPassword());
                        Phone.setText(user.getPhone());
                        Address.setText(user.getAddress());
                        if(user.getImage()==null){
                            prof_img.setImageResource(R.drawable.default_avatar);
                        }
                        else {
                            prof_img.setImageBitmap(base64ToBitmap(user.getImage()));
                        }
                        Toast.makeText(Edit_Profile.this, "User found", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Edit_Profile.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                }

                // Do something with the filtered productList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });


        save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernm = Username.getText().toString();
                String pass = Password.getText().toString();
                String ph = Phone.getText().toString();
                String at = Address.getText().toString();

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

                Query query = usersRef.orderByChild("email").equalTo(old_em);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Access the node(s) with the specified email
                                User user = snapshot.getValue(User.class);
                                // Update the desired values
                                snapshot.getRef().child("name").setValue(usernm);
                                snapshot.getRef().child("password").setValue(pass);
                                snapshot.getRef().child("phone").setValue(ph);
                                snapshot.getRef().child("address").setValue(at);
                                snapshot.getRef().child("image").setValue(bitmapToBase64(imageBitmap));
                                editor.putString("Profile_image", bitmapToBase64(imageBitmap));
                                editor.commit();
                                // Confirm the update
                                Toast.makeText(Edit_Profile.this, "updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // No node with the specified email exists
                            Toast.makeText(Edit_Profile.this, "No user found with email", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error occurred while retrieving data
                        System.out.println("Error: " + databaseError.getMessage());
                    }
                });
            }
        });

        top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Flag",true);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("Flag",true);
                startActivity(intent);
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imageBitmap = loadImageFromUri(imageUri);
                        prof_img.setImageBitmap(imageBitmap);

                        if (imageBitmap != null) {
                            Toast.makeText(this, "Image sucessfully found", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        select_img.setOnClickListener(v -> openGallery());

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private Bitmap loadImageFromUri(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


}