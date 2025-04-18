package com.example.fbstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etRecord;
    private TextView tvWelcome;
    private Button btnAddRecordToDB, btnAddPrivateRecordToDB;
    private Button btnLogout;
    private Button btnTakePicture;

    private RecordAdapter adapter;
    private ArrayList<MyRecord> myRecords;
    private FB fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initialization();
        fb = new FB(this, myRecords);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        String imageBitmap = intent.getStringExtra("imageBitmap");
        //long signUpTime = System.currentTimeMillis();
        fb.setUserData(email, password, imageBitmap);

/*        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person_96);
        String profileImage = encodeImageToBase64(bitmap);

        fb.setUserData("","",0,profileImage);
        fb.setOrder("","",0,0,false,"");
        */
    }

    private void initialization() {
        // initialize
        etName = findViewById(R.id.etName);
        etRecord = findViewById(R.id.etScore);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnAddRecordToDB = findViewById(R.id.btnAddRecordToDB);
        btnAddRecordToDB.setOnClickListener(this);
        btnAddPrivateRecordToDB = findViewById(R.id.btnAddPrivateRecordToDB);
        btnAddPrivateRecordToDB.setOnClickListener(this);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // to be vertical

        myRecords = new ArrayList<>();
        adapter = new RecordAdapter(this, myRecords);
        recyclerView.setAdapter(adapter);
    }

    public void dataChange() {
        // update the RecyclerView after change in the arraylist
        adapter.notifyDataSetChanged();
    }

    public void userDataChange(MyRecord currentRecord) {
        tvWelcome.setText("" + currentRecord.getName() + " : " + currentRecord.getScore() + " points");
    }

    @Override
    public void onClick(View view) {
        if(view == btnAddRecordToDB)
        {
            // save the record in the firebase
            //fb.setRecord(etName.getText().toString(), Integer.parseInt(etRecord.getText().toString()));
            fb.setRecord(etName.getText().toString(), Integer.parseInt(etRecord.getText().toString()));
        }

        if(view == btnAddPrivateRecordToDB)
        {
            // save the record in the firebase
            //fb.setRecord(etName.getText().toString(), Integer.parseInt(etRecord.getText().toString()));
            fb.setPrivateRecord(etName.getText().toString(), Integer.parseInt(etRecord.getText().toString()));
        }

        if(view == btnLogout)
        {
            FirebaseAuth.getInstance().signOut();
            finish(); // close the activity
        }

        if(view == btnTakePicture)
        {
            Intent intent = new Intent(this, TakePictureActivity.class);
            startActivity(intent);
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