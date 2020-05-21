package com.example.barcodescanner.barcode.impl;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.barcodescanner.barcode.DecodeFormatManager;
import com.example.barcodescanner.callback.IBarCodeDecoder;
import com.example.barcodescanner.util.BitmapLuminanceSource;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;
import java.util.Vector;

public class BarCodeDecoderImpl implements IBarCodeDecoder {

    private static final String CHARACTER_SET = "UTF8";

    private MultiFormatReader multiFormatReader;

    public BarCodeDecoderImpl() {

        this.multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        // 这里设置可扫描的类型，我这里选择了都支持
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        hints.put(DecodeHintType.CHARACTER_SET, CHARACTER_SET);

        // 设置解析配置参数
        multiFormatReader.setHints(hints);
    }


    @Override
    public String decodeBarCode(byte[] nv21, int w, int h) {
        return null;
    }

    @Override
    public String decodeBarCode(Bitmap bitmap) {

        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult != null) {
            return rawResult.getText().trim();
        }
        return null;
    }
}
