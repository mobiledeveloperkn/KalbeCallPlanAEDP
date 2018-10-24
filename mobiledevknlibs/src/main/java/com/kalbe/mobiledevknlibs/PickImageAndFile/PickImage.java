package com.kalbe.mobiledevknlibs.PickImageAndFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.kalbe.mobiledevknlibs.PermissionChecker.PermissionChecker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dewi Oktaviani on 1/8/2018.
 */

public class PickImage {

    //contextnya di isi (namaClass.this) kalo dari activity
    public static void CaptureImage(Context context, String folderName, String fileName, final int REQUEST_CODE) {
        boolean result = PermissionChecker.Utility.checkPermission(context);
        if (result){
            Uri uriImage = UriData.getOutputMediaImageUri(context, folderName, fileName);
            Intent intentCamera1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera1.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
            ((Activity)context).startActivityForResult(intentCamera1, REQUEST_CODE);
        }
    }


    private static Bitmap resizeImageForBlob(Bitmap photo){
        int width = photo.getWidth();
        int height = photo.getHeight();

        int maxHeight = 800;
        int maxWidth = 800;

        Bitmap bitmap;

        if(height > width){
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        }
        else if(height < width){
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        }
        else{
            width = maxWidth;
            height = maxHeight;
        }

        bitmap = Bitmap.createScaledBitmap(photo, width, height, true);

        return bitmap;
    }


    public static void previewCapturedImage(ImageView imageView, Bitmap bitmap, int width, int height) {
        Bitmap mybitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        imageView.setImageBitmap(mybitmap);

    }

    public static void previewCapturedImageUri(ImageView imageView, Context context, Uri uri, int width, int height) {
        Bitmap bitmap = decodeStreamReturnBitmap(context, uri);
        Bitmap mybitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        imageView.setImageBitmap(mybitmap);

    }

    public static byte[] getByteImageToSave(Context context, Uri uri){
        byte[] imgPhoto = null;
        try {
            Bitmap bitmap = decodeStreamReturnBitmap(context, uri);
            ByteArrayOutputStream output = null;
            try {
                output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, output); // bmp is your Bitmap instance
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imgPhoto = output.toByteArray();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }return imgPhoto ;
    }


    public static Bitmap decodeByteArrayReturnBitmap(byte[] image) {
        Bitmap mybitmap;
        if (image != null) {
            Bitmap photo = BitmapFactory.decodeByteArray(image, 0, image.length);
            mybitmap  = resizeImageForBlob(photo);
            return mybitmap;
        } else {
            return null;
        }
    }

    public static Bitmap decodeStreamReturnBitmap(Context context, Uri uri) {
        Bitmap photo = null;
        try {
            InputStream ims =  context.getContentResolver().openInputStream(uri);
            photo = BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = resizeImageForBlob(photo);
        return bitmap;
    }

    //uri harus di definisikan dulu
    // (uri tidak akan terbaca jika uri merupakan intentReturntData.getData().getPath().toString())
    public static Bitmap decodeFileReturnBitmap(Context context, Uri uri) {
        Bitmap photo = null;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        photo = BitmapFactory.decodeFile(uri.getPath().toString(), bitmapOptions);
        Bitmap bitmap = resizeImageForBlob(photo);
        return bitmap;
    }

    //untuk create file image temporary berdasarkan path folder tertentu (nama file tidak perlu di ikutkan)
    public static File decodeByteArraytoImageFile(byte[] imageArray, String pathFolder){
        File folder = new File(pathFolder);
        folder.mkdirs();
        File file = null;
        try {
            file = File.createTempFile("image-", ".jpg", new File(pathFolder));
            FileOutputStream out = new FileOutputStream(file);
            out.write(imageArray);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static byte[] getByteImageToSaveRotate(Context context, Uri uri) {
        byte[] imgPhoto = null;
        try {
            Bitmap bitmap = PickImage.decodeStreamReturnBitmap(context, uri);
            ExifInterface exif = null;
            String path = uri.toString();
            if (path.startsWith("file://")) {
                exif = new ExifInterface(path);
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (path.startsWith("content://")) {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    exif = new ExifInterface(inputStream);
                }
            }

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap rotatedBitmap = rotateBitmap(bitmap, orientation);

            ByteArrayOutputStream output = null;

            try {
                output = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            } catch (Exception var15) {
                var15.printStackTrace();
            } finally {
                try {
                    if(output != null) {
                        output.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }

            imgPhoto = output.toByteArray();
        } catch (NullPointerException var17) {
            var17.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgPhoto;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();

            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int Orientation (Context context, Uri uri){
        ExifInterface exif = null;
        String path = uri.toString();
        if (path.startsWith("file://")) {
            try {
                exif = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (path.startsWith("content://")) {
                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(uri);
                    exif = new ExifInterface(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        return orientation;
    }
}
