package com.example.fabset;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public interface RecyclerViewClickListener {
    void onClick(View view, ImageView imageView, TextView titleTextView, TextView priceTextView, int position);
}
