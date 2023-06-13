package com.example.fabset;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AdminActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 200;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    ImageView imageView;
    EditText name;
    EditText price;
    EditText descrip;
    EditText tag;
    Spinner category;
    String Cat;
    String Subcat;
    Spinner subcategory;
    Button upload;
    Button submit;
    Uri imageUri;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        imageView = findViewById(R.id.product_img);
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        descrip = findViewById(R.id.product_description);
        tag = findViewById(R.id.tag);
        category = findViewById(R.id.category);
        subcategory = findViewById(R.id.subcategory);
        upload = findViewById(R.id.upload_btn);
        submit = findViewById(R.id.submit);
        String[] cat = {"Mens", "Womens", "Kids"};
        String[] subcat = {"Shirts", "Pants", "T-shirts", "Dresses", "Frocks", "Sleepwear"};

        ArrayAdapter<String> catadapter = new ArrayAdapter<>(this,R.layout.custom_spinner,cat);
        ArrayAdapter subcatadapter = new ArrayAdapter(this, R.layout.custom_spinner,subcat);

        category.setAdapter(catadapter);
        subcategory.setAdapter(subcatadapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cat = catadapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Subcat = (String) subcatadapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadData();
            }
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        imageBitmap = loadImageFromUri(imageUri);
                        imageView.setImageBitmap(imageBitmap);

                        if (imageBitmap != null) {
                            Toast.makeText(this, "Image sucessfully found", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        upload.setOnClickListener(v -> openGallery());
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

    public void uploadData(){
        String title = name.getText().toString();
        String pri = price.getText().toString();
        String desc = descrip.getText().toString();
        String tg = tag.getText().toString();
        String cat = Cat;
        String subcat = Subcat;
        Product product = new Product(title,pri,cat, subcat, desc, tg, bitmapToBase64(imageBitmap) );
        FirebaseDatabase.getInstance().getReference("Products").child(title).setValue(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}

