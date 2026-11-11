package com.example.devquest;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class PhotoUtil {

    private static final int SIZE = 256;

    private PhotoUtil() {

    }

    @Nullable
    public static String uriToBase64(@NonNull Context context, @NonNull Uri uri) {
        try {
            ContentResolver cr = context.getContentResolver();

            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            try (InputStream in = cr.openInputStream(uri)) {
                if (in == null) return null;
                BitmapFactory.decodeStream(in, null, bounds);
            }

            int sample = 1;
            while (bounds.outWidth / sample > SIZE * 2 && bounds.outHeight / sample > SIZE * 2) {
                sample *= 2;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = sample;
            Bitmap bmp;
            try (InputStream in = cr.openInputStream(uri)) {
                if (in == null) return null;
                bmp = BitmapFactory.decodeStream(in, null, opts);
            }
            if (bmp == null) return null;

            int rotation = 0;
            try (InputStream in = cr.openInputStream(uri)) {
                if (in != null) {
                    ExifInterface exif = new ExifInterface(in);
                    int o = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    if (o == ExifInterface.ORIENTATION_ROTATE_90) rotation = 90;
                    else if (o == ExifInterface.ORIENTATION_ROTATE_180) rotation = 180;
                    else if (o == ExifInterface.ORIENTATION_ROTATE_270) rotation = 270;
                }
            } catch (Exception ignored) {

            }

            int w = bmp.getWidth();
            int h = bmp.getHeight();
            int side = Math.min(w, h);
            int x = (w - side) / 2;
            int y = (h - side) / 2;

            Matrix m = new Matrix();
            if (rotation != 0) m.postRotate(rotation);
            Bitmap square = Bitmap.createBitmap(bmp, x, y, side, side, m, true);
            Bitmap small = Bitmap.createScaledBitmap(square, SIZE, SIZE, true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            small.compress(Bitmap.CompressFormat.JPEG, 80, out);
            return Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);

        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static Bitmap base64ToBitmap(@Nullable String base64) {
        if (base64 == null || base64.isEmpty()) return null;
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}