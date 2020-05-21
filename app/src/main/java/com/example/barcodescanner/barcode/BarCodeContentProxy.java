package com.example.barcodescanner.barcode;

import com.example.barcodescanner.callback.IBarCodeContent;

public class BarCodeContentProxy implements IBarCodeContent {

    private IBarCodeContent mIBarCodeContent;

    public BarCodeContentProxy(IBarCodeContent iBarCodeContent) {

        mIBarCodeContent = iBarCodeContent;
    }

    @Override
    public String barCodeContent(String content) {

        return mIBarCodeContent.barCodeContent(content);
    }
}