package com.example.fabset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends AppCompatActivity implements RecyclerViewClickListener {
    private RecyclerView productrecyclerView;
    private ProductAdapter productadapter;
    CardView cardView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference productsRef = database.getReference("Products");
    String cat;
    String subcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);
        ImageButton Toback = findViewById(R.id.ToBack);
        productrecyclerView = findViewById(R.id.ProductrecyclerView);
        productrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        List<Product> productList = new ArrayList<>();
        productadapter = new ProductAdapter(productList);
        productadapter.setClickListener(this);
        productrecyclerView.setAdapter(productadapter);
        cat = getIntent().getStringExtra("category");
        subcat = getIntent().getStringExtra("subcategory");
        Toback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);

                    // Apply your condition here
                    if (product.getCategory().contains(cat)) {
                        if (product.getSubcategory().contains(subcat)){
                            productList.add(product);
                        }
                    }
                }
                productadapter.notifyDataSetChanged();

                // Do something with the filtered productList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });


    }


    @Override
    public void onClick(View view, ImageView imageView, TextView titleTextView, TextView priceTextView, int position) {
        String text = titleTextView.getText().toString();
        String price = priceTextView.getText().toString().substring(3);
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        Intent intent = new Intent(ProductsPage.this, DisplayProductActivity.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("image", byteArray);
        intent.putExtra("Name", text);
        intent.putExtra("Price", price);
        intent.putExtra("category", cat);
        intent.putExtra("subcategory", subcat);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, ImageView imageView, TextView titleTextView, TextView priceTextView, int position);
    }

}