package ua.ck.ghplayer.utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class ImageUtils {

    public static Uri getAlbumArt(Context context, long albumId) {

        Uri contentUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(contentUri, albumId);

        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(albumArtUri, "r");
            if (parcelFileDescriptor != null) {
                return albumArtUri;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap getBitmapAlbumart(Context context, long albumId) {
        Bitmap bitmapAlbumArt = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(albumArtUri, albumId);

        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmapAlbumArt = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
                parcelFileDescriptor = null;
                fileDescriptor = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return bitmapAlbumArt;
    }

}
