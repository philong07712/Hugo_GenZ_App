package com.example.hugo_genz_application;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    FloatingActionButton fab;
    private LottieAnimationView lottieView;
    private LottieAnimationView lottieLoadingView;
    private String mQrCode;
    private IntentIntegrator qrScan;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.scan_fab);
        lottieView = findViewById(R.id.lottie);
        lottieLoadingView = findViewById(R.id.lottie_loading);
        qrScan = new IntentIntegrator(this);

        fab.setOnClickListener(v -> {
            requestCamera();
            qrScan.setOrientationLocked(false);
            qrScan.initiateScan();
            Log.i(TAG, "onCreate: fab clicked");
//            testingTicketCheck("1414/916521700");
        });
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                Log.i(TAG, "onActivityResult: " + result.getContents());
                testingTicketCheck(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.i(TAG, "onActivityResult: " + contents);
            }
            if (resultCode == RESULT_CANCELED) {
                // cancels
            }
        }


    }

    public void testingTicketCheck(String testingMessage) {
        lottieView.setVisibility(View.INVISIBLE);
//        lottieLoadingView.setVisibility(View.VISIBLE);
        QRUtil.checkQRCode(testingMessage, new TicketListener() {
            @Override
            public void onSuccess(int statusCode) {
                lottieView.setVisibility(View.VISIBLE);
                lottieLoadingView.setVisibility(View.INVISIBLE);
                lottieView.setAnimation(R.raw.success);
                lottieView.playAnimation();
                lottieView.setSpeed(3.0f);
            }

            @Override
            public void onFailed(int statusCode, String str) {
//                toast("Check In failed: " + str);
                lottieView.setVisibility(View.VISIBLE);
                lottieLoadingView.setVisibility(View.INVISIBLE);
                lottieView.setAnimation(R.raw.failed);
                lottieView.playAnimation();
            }
        });
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        } else {
            Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (Exception e) {
                Toast.makeText(this, "Error starting camera " + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
    }

    public void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
