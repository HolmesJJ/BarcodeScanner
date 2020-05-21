package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcodescanner.barcode.BarCodeDecoderHelper;
import com.example.barcodescanner.callback.IBarCodeContent;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

public class BarcodeScannerActivity extends AppCompatActivity implements FrameProcessor, IBarCodeContent {

    private final static String TAG = "BarcodeScannerActivity";

    private CameraView camera;
    private TextView tvBarcodeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        tvBarcodeContent = (TextView) findViewById(R.id.tv_barcode_content);
        camera = (CameraView) findViewById(R.id.camera);
        camera.addFrameProcessor(this);

        BarCodeDecoderHelper.getInstance().initBarCodeContent(this);
        BarCodeDecoderHelper.getInstance().start();
    }

    @Override
    public void process(@NonNull Frame frame) {
        BarCodeDecoderHelper.getInstance().putRGBData(frame);
    }

    @Override
    public String barCodeContent(String content) {
        Log.i(TAG, "content: " + content);
        runOnUiThread(() -> {
            tvBarcodeContent.setText(content);
        });
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera != null && !camera.isOpened()) {
            camera.open();
        }
    }

    @Override
    protected void onPause() {
        if (camera != null && camera.isOpened()) {
            camera.close();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            if (camera.isOpened()) {
                camera.close();
            }
            camera.addFrameProcessor(null);
            camera.destroy();
        }
        super.onDestroy();
    }
}
