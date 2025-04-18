package com.example.fbstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etProductName, etQuantity, etPrice;
    private Switch boolSwitch;

    private TextView tvWelcome;
    private Button btnAddPrivateOrderToDB;
    private Button btnLogout;

    private OrdersAdapter adapter;
    private ArrayList<Order> myOrders;
    private FB fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initialization();
        fb = new FB(this, myOrders);

        Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            String email = intent.getStringExtra("email");
            String name = intent.getStringExtra("name");
            String imageBitmap = intent.getStringExtra("imageBitmap");
            //long signUpTime = System.currentTimeMillis();
            fb.setUserData(email, name, imageBitmap);
        }


/*        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person_96);
        String profileImage = encodeImageToBase64(bitmap);
        */
    }

    private void initialization() {
        // initialize

        etProductName = findViewById(R.id.etProductName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        boolSwitch = findViewById(R.id.switchDelivered);

        btnAddPrivateOrderToDB = findViewById(R.id.btnAddPrivateOrderToDB);
        btnAddPrivateOrderToDB.setOnClickListener(this);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // to be vertical

        myOrders = new ArrayList<>();
        adapter = new OrdersAdapter(this, myOrders);
        recyclerView.setAdapter(adapter);
    }

    public void dataChange() {
        // update the RecyclerView after change in the arraylist
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {


        if (view == btnAddPrivateOrderToDB) {
            String productName = etProductName.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            boolean isDelivered = boolSwitch.isChecked();

            // Basic validation
            if (productName.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                // Show an error message to the user (e.g., using a Toast)
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            double price;
            try {
                quantity = Integer.parseInt(quantityStr);
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                // Show an error message to the user (e.g., using a Toast)
                Toast.makeText(this, "Invalid quantity or price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the setOrder method with the validated data
            fb.setOrder(productName, quantity, price, isDelivered);
            // Optionally, show a success message
            Toast.makeText(this, "Order added successfully", Toast.LENGTH_SHORT).show();
        }

        if(view == btnLogout)
        {
            FirebaseAuth.getInstance().signOut();
            finish(); // close the activity
        }


    }

    public String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }



}