package com.example.fbstore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class TakePictureActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ImageView imageView;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        // ... your other code ...
        imageView = findViewById(R.id.imageView);

        // Initialize the ActivityResultLauncher
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Handle the result of the camera intent here
                    if (result.getResultCode() == RESULT_OK) {
                        // The user took a picture
                        // You can get the image data from the result.getData()
                        // For example:
                        Bundle extras = result.getData().getExtras();
                        imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);

                        Toast.makeText(this, "Picture taken", Toast.LENGTH_SHORT).show();
                    } else {
                        // The user canceled taking a picture
                        Toast.makeText(this, "Picture canceled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        checkCameraPermissionAndOpenCamera();
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, launch the camera
            openCamera();
        } else {
            // Permission is not granted, request it
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            // Explain to the user why you need the camera permission (optional)
            // You can show a dialog or a message here
            // After explaining, request the permission again
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // No explanation needed, request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch the camera
                openCamera();
            } else {
                // Permission denied, handle it gracefully (e.g., disable camera feature)
                // You can show a message to the user here
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureLauncher.launch(cameraIntent);
    }

    public void takePicFromCameraBtn(View view) {
        Intent intent = new Intent();
        intent.putExtra("imageBitmap", imageBitmap);
        setResult(RESULT_OK, intent);
        finish();
    }
}