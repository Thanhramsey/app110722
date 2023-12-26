package com.vnpt.printproject;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class BarcodeEncoder {

    private final BarcodeEncoder barcodeEncoder;

    public BarcodeEncoder() {
        this.barcodeEncoder = new BarcodeEncoder();
    }

    public Bitmap createBitmap(BitMatrix bitMatrix) {
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        return barcodeEncoder.encode(contents, format, width, height);
    }
}