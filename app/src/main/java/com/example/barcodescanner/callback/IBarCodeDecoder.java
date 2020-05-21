package com.example.barcodescanner.callback;

import android.graphics.Bitmap;

public interface IBarCodeDecoder {

    /**
     * @param nv21 预览帧数据
     * @param w    预览宽度
     * @param h    预览高度
     *
     * @return 解析出的条形码串
     */
    String decodeBarCode(byte[] nv21, int w, int h);

    /**
     * @param bitmap 图片数据
     *
     * @return 解析出的条形码串
     */
    String decodeBarCode(Bitmap bitmap);
}
