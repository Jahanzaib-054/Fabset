package com.example.fabset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayProductActivity extends AppCompatActivity {
    String Size = "Small";
    String Qty;
    ImageButton Back_btn;
    ImageView imageView;
    TextView Pro_name;
    TextView Pro_price;
    TextView Quantity;
    String cat;
    String subcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);
        Back_btn = findViewById(R.id.back_btn);
        imageView = findViewById(R.id.pro_img);
        Pro_name = findViewById(R.id.pro_name);
        Pro_price = findViewById(R.id.pro_price);
        Quantity = findViewById(R.id.quantity);

        cat = getIntent().getStringExtra("category");
        subcat = getIntent().getStringExtra("subcategory");

//        TextView Pro_size = findViewById(R.id.pro_size);
//        TextView Pro_qty = findViewById(R.id.pro_qty);
        Button AddCart = findViewById(R.id.addcart);
        RadioGroup radioGroup = findViewById(R.id.RadioGroup);
        ImageButton Increase = findViewById(R.id.increase);
        ImageButton Decrease = findViewById(R.id.decrease);

        AddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadCartData();
            }
        });

        Intent intent = getIntent();
        byte[] byteArray = intent.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        imageView.setImageBitmap(bitmap);
        Pro_name.setText(intent.getStringExtra("Name"));
        Pro_price.setText("Rs "+intent.getStringExtra("Price"));

        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toback = new Intent(DisplayProductActivity.this, ProductsPage.class);
                intent.putExtra("category", cat);
                intent.putExtra("subcategory", subcat);
                startActivity(toback);
            }
        });

        Increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Qty = Quantity.getText().toString();
                int qty = Integer.parseInt(Qty);
                Quantity.setText(Integer.toString(qty = qty + 1));
            }
        });

        Decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Qty = Quantity.getText().toString();
                int qty = Integer.parseInt(Qty);
                if(qty != 0) {
                    Quantity.setText(Integer.toString(qty = qty - 1));
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle the selected size option here
                switch (checkedId) {
                    case R.id.radioSmall:
                        // Small size selected
                        Size = "Small";
                        break;
                    case R.id.radioMedium:
                        // Medium size selected
                        Size = "Medium";
                        break;
                    case R.id.radioLarge:
                        // Large size selected
                        Size = "Large";
                        break;
                    default:
                        Size = "Small";
                        break;
                    // Handle more size options as needed
                }
            }
        });

    }

    public void uploadCartData(){
        String title = Pro_name.getText().toString();
        String pri = Pro_price.getText().toString().substring(3);
        String qty = Quantity.getText().toString();
        CartItem cartItem = new CartItem(title, pri, qty, Size );
        SharedPreferences SP;
        SP = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        String node = "";
        node = SP.getString("CartId", "");
        if(node.isEmpty()){
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference newProductRef = databaseRef.child("Cart").push();
            newProductRef.setValue(cartItem);
            String generatedKey = newProductRef.getKey();
            editor.putString("CartId",generatedKey);
            editor.commit();
        }
        else{
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(node);
            databaseRef.push().setValue(cartItem);
        }
        Intent intent = new Intent(DisplayProductActivity.this, ProductsPage.class);
        intent.putExtra("category", cat);
        intent.putExtra("subcategory", subcat);
        startActivity(intent);

    }
}