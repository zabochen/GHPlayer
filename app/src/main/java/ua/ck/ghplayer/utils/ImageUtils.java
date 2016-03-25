package ua.ck.ghplayer.utils;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

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

}
