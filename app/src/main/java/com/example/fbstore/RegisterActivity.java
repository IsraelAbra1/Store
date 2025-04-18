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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private ActivityResultLauncher<Intent> takePictureActivityResultLauncher;

    private Button btnRegister, btnTakePicture;
    private EditText etEmailAddress, etNumberPassword, etName;
    private ImageView imageView;
    private Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);

        etEmailAddress = findViewById(R.id.etEmailAddress);
        etNumberPassword = findViewById(R.id.etNumberPassword);
        etName = findViewById(R.id.etName);

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
    public void onClick(View view) {
        if (view == btnRegister) {
            String email = etEmailAddress.getText().toString().trim();
            String password = etNumberPassword.getText().toString().trim();
            boolean isValid = true;

            // Validate email
            if (email.isEmpty()) {
                etEmailAddress.setError("Email is required");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmailAddress.setError("Enter a valid email");
                isValid = false;
            } else {
                etEmailAddress.setError(null); // Clear previous error
            }

            // Validate password
            if (password.isEmpty()) {
                etNumberPassword.setError("Password is required");
                isValid = false;
            } else if (password.length() < 6) {
                etNumberPassword.setError("Password must be at least 6 characters");
                isValid = false;
            } else if (!TextUtils.isDigitsOnly(password)) {
                etNumberPassword.setError("Only digits are allowed");
                isValid = false;
            } else {
                etNumberPassword.setError(null); // Clear previous error
            }

            if (isValid) {
                createAuthentication(email, password);
                //FB fb = new FB(this);
            }
        }



        if(view == btnTakePicture)
        {
            Intent intent = new Intent(this, TakePictureActivity.class);
            takePictureActivityResultLauncher.launch(intent);
        }

    }

    private void createAuthentication(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("name", etName.getText().toString());
                            String encodeImageToBase64 = encodeImageToBase64(imageBitmap);
                            intent.putExtra("imageBitmap", encodeImageToBase64);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "fail register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}