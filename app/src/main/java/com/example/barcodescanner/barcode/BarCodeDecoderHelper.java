package com.example.barcodescanner.barcode;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

import com.example.barcodescanner.BarcodeScannerActivity;
import com.example.barcodescanner.barcode.impl.BarCodeDecoderImpl;
import com.example.barcodescanner.callback.IBarCodeContent;
import com.example.barcodescanner.callback.IBarCodeDecoder;
import com.example.barcodescanner.thread.ThreadManager;
import com.example.barcodescanner.util.ColorConvertUtil;
import com.otaliastudios.cameraview.frame.Frame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BarCodeDecoderHelper {

    private static final int ROTATION_DEGREE = 90;
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "barcode.jpg";;

    private volatile boolean vIsRGBCameraNv21Ready;
    private byte[] mNv21;
    private IBarCodeDecoder mIBarCodeDecoder;
    private IBarCodeContent mIBarCodeContent;
    private Runnable mScanBarCodeRunnable;
    private Matrix matrix = new Matrix();
    private int mPreviewWidth;
    private int mPreviewHeight;
    private volatile int vIdentifyInterval;
    private BarcodeScannerActivity mBarcodeScannerActivity;

    private BarCodeDecoderHelper() {
        this.vIdentifyInterval = 5000;
        this.vIsRGBCameraNv21Ready = true;
        this.matrix.postRotate(ROTATION_DEGREE);
        this.initBarCodeDecoder();
    }

    public static final BarCodeDecoderHelper getInstance() {
        return BarCodeDecoderHelper.SingletonHolder.INSTANCE;
    }

    private void initBarCodeDecoder() {
        this.mIBarCodeDecoder = new BarCodeDecoderProxy(new BarCodeDecoderImpl());
    }

    public void initBarCodeContent(BarcodeScannerActivity barcodeScannerActivity) {
        mBarcodeScannerActivity = barcodeScannerActivity;
        this.mIBarCodeContent = new BarCodeContentProxy(mBarcodeScannerActivity);
    }

    public int getIdentifyInterval() {
        return this.vIdentifyInterval;
    }

    public BarCodeDecoderHelper setIdentifyInterval(int identifyInterval) {
        this.vIdentifyInterval = identifyInterval;
        return this;
    }

    public void start() {
        this.vIsRGBCameraNv21Ready = false;
    }

    public void stop() {
        this.vIsRGBCameraNv21Ready = true;
    }

    public void putRGBData(Frame frame) {
        if (frame != null) {
            if (!this.vIsRGBCameraNv21Ready) {
                this.mPreviewWidth = frame.getSize().getWidth();
                this.mPreviewHeight = frame.getSize().getHeight();
                int size = (int)((float)(this.mPreviewWidth * this.mPreviewHeight) * 1.5F);
                if (this.mNv21 == null || this.mNv21.length != size) {
                    this.mNv21 = new byte[size];
                }

                byte[] cameraData = frame.getData();
                System.arraycopy(cameraData, 0, this.mNv21, 0, cameraData.length);
                this.vIsRGBCameraNv21Ready = true;
                this.scanBarCode();
            }
        }
    }

    private void scanBarCode() {
        if (this.mScanBarCodeRunnable == null) {
            this.mScanBarCodeRunnable = () -> {
                String content = "";
                try {
                    Bitmap bitmap = ColorConvertUtil.covertNV21ToBitmap(this.mNv21, this.mPreviewWidth, this.mPreviewHeight);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, this.mPreviewWidth, this.mPreviewHeight, true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    saveBarCodeImage(rotatedBitmap);
                    content = this.mIBarCodeDecoder.decodeBarCode(rotatedBitmap);
                } catch (Exception e) {
                    e.fillInStackTrace();
                }
                if (TextUtils.isEmpty(content)) {
                    this.vIsRGBCameraNv21Ready = false;
                } else {
                    this.parseBarCode(content);
                    SystemClock.sleep((long)this.vIdentifyInterval);
                    this.vIsRGBCameraNv21Ready = false;
                }
            };
        }

        ThreadManager.getThreadPoolProxy().execute(this.mScanBarCodeRunnable);
    }

    private void saveBarCodeImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        writeImageToDisk(byteArray, PATH);
    }

    private void parseBarCode(String content) {
        if (mIBarCodeContent != null) {
            mIBarCodeContent.barCodeContent(content);
        }
    }

    private static class SingletonHolder {
        private static final BarCodeDecoderHelper INSTANCE = new BarCodeDecoderHelper();

        private SingletonHolder() {
        }
    }

    public static void writeImageToDisk(byte[] img, String fileName) {
        FileOutputStream fops = null;
        try {
            File file = new File(fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fops != null) {
                try {
                    fops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
