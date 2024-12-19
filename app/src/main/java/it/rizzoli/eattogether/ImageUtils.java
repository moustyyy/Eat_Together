package it.rizzoli.eattogether;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageUtils {

    private Context context;

    // Costruttore che riceve un oggetto Context
    public ImageUtils(Context context) {
        this.context = context;
    }

    // Metodo per ottenere un Blob (byte[]) da una risorsa PNG
    public byte[] imageToByteArray(int drawableId) {
        try {
            // Ottieni l'oggetto Resources dal Context
            Resources resources = context.getResources();

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

