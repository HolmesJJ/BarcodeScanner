package com.example.barcodescanner.barcode;

import android.graphics.Bitmap;

import com.example.barcodescanner.callback.IBarCodeDecoder;

public class BarCodeDecoderProxy implements IBarCodeDecoder {

    private IBarCodeDecoder mIBarCodeDecoder;

    public BarCodeDecoderProxy(IBarCodeDecoder iBarCodeDecoder) {

        mIBarCodeDecoder = iBarCodeDecoder;
    }

    @Override
    public String decodeBarCode(byte[] nv21, int w, int h) {

        return mIBarCodeDecoder.decodeBarCode(nv21, w, h);
    }

    @Override
    public String decodeBarCode(Bitmap bitmap) {

        return mIBarCodeDecoder.decodeBarCode(bitmap);
    }
}
