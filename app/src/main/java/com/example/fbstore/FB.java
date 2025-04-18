package com.example.fbstore;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// google explanations
// https://firebase.google.com/docs/database/android/lists-of-data#java_1


public class FB {
    FirebaseDatabase database;
    Context context;
    ArrayList<Order> myOrders;
    public FB(Context context, ArrayList<Order> myOrders) {
        //database = FirebaseDatabase.getInstance("https://fbrecordst-default-rtdb.firebaseio.com");
        database = FirebaseDatabase.getInstance();
        this.context = context;
        this.myOrders = myOrders;

        Query myQuery = database.getReference("user_data/" + FirebaseAuth.getInstance().getUid() + "/orders");

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOrders.clear();  // clear the array list
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    Order currentMyRecord =userSnapshot.getValue(Order.class);
                    myOrders.add(0, currentMyRecord);
                }

                ((WelcomeActivity)context).dataChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setUserData(String email, String name, String profileImage)
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String base64Image = profileImage;
        //User user = new User("Moshe Cohen", "moshe@example.com", System.currentTimeMillis(), base64Image);
        long signUpTime = System.currentTimeMillis();
        User user = new User(name, email, signUpTime, profileImage);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(uid).setValue(user);

    }

    public void setOrder(String productName, int quantity, double price, boolean delivered)
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        String orderId = ref.child("user_data").child(uid).child("orders").push().getKey();
        Order order = new Order(orderId, productName, quantity, price, delivered);

        ref.child("user_data").child(uid).child("orders").child(orderId).setValue(order);

    }
}
