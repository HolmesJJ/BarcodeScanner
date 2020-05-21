package com.example.barcodescanner.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ColorConvertUtil {
    public ColorConvertUtil() {
    }

    @Nullable
    public static Bitmap covertNV21ToBitmap(@NonNull byte[] var0, int var1, int var2) {
        if (var0 != null && var0.length != 0) {
            Bitmap var3 = null;
            YuvImage var4 = new YuvImage(var0, 17, var1, var2, (int[])null);
            ByteArrayOutputStream var5 = new ByteArrayOutputStream();
            var4.compressToJpeg(new Rect(0, 0, var1, var2), 100, var5);
            var3 = BitmapFactory.decodeByteArray(var5.toByteArray(), 0, var5.size());

            try {
                var5.close();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            return var3;
        } else {
            return null;
        }
    }
}
