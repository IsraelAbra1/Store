package com.example.fbstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> takePictureActivityResultLauncher;

    private Button btnRegister, btnLogin, btnTakePicture;
    private EditText etEmailAddress, etNumberPassword;
    private ImageView imageView;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);

        etEmailAddress = findViewById(R.id.etEmailAddress);
        etNumberPassword = findViewById(R.id.etNumberPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        takePictureActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        imageBitmap = (Bitmap) result.getData().getExtras().get("imageBitmap");
                        imageView.setImageBitmap(imageBitmap);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        if (view == btnRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }


        if(view == btnLogin)
        {
            String email = etEmailAddress.getText().toString();
            String password = etNumberPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "login failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

        if(view == btnTakePicture)
        {
            Intent intent = new Intent(this, TakePictureActivity.class);
            takePictureActivityResultLauncher.launch(intent);
        }


    }


}