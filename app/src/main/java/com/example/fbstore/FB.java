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
    ArrayList<MyRecord> myRecords;
    public FB(Context context, ArrayList<MyRecord> myRecords) {
        //database = FirebaseDatabase.getInstance("https://fbrecordst-default-rtdb.firebaseio.com");
        database = FirebaseDatabase.getInstance();
        this.context = context;
        this.myRecords = myRecords;

        // read the records from the Firebase and order them by the record from highest to lowest
        // limit to only 8 items
        Query myQuery = database.getReference("records").orderByChild("score").limitToLast(8);

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRecords.clear();  // clear the array list
                for(DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    MyRecord currentMyRecord =userSnapshot.getValue(MyRecord.class);
                    myRecords.add(0, currentMyRecord);
                }
/*                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    users.sort(new Comparator<User>() {
                        @Override
                        public int compare(User user, User t1) {
                            return 0;
                        }
                    });
                }*/
                ((WelcomeActivity)context).dataChange();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid());
        myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid());
        //DatabaseReference myRef = database.getReference().child(FirebaseAuth.getInstance().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyRecord currentRecord =snapshot.getValue(MyRecord.class);
                if(currentRecord != null)
                {
                    ((WelcomeActivity)context).userDataChange(currentRecord);
                }
                else
                    Log.d("TAG", "onDataChange: ");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(context, "name onCancelled", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setRecord(String name, int record)
    {
        // Write a message to the database
        DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        //DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid());

        MyRecord rec = new MyRecord(name, record);
        myRef.setValue(rec);
    }

    public void setPrivateRecord(String name, int record)
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid());

        MyRecord rec = new MyRecord(name, record);
        myRef.setValue(rec);
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
