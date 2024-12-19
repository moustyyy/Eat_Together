package it.rizzoli.eattogether;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Utility {
    public static byte[] getBytesFromUri(Uri imageUri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] imageToByteArray(Resources resources, int drawableId) {
        try {
            // Ottieni un InputStream dalle risorse
            InputStream inputStream = resources.openRawResource(drawableId);

            // Decodifica l'immagine come Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Crea un ByteArrayOutputStream per scrivere i byte
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Comprime l'immagine in formato PNG
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            // Restituisci l'immagine come array di byte
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
