package ua.ck.ghplayer.utils;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import ua.ck.ghplayer.Application;
import ua.ck.ghplayer.loaders.ArtistInfoLoader;
import ua.ck.ghplayer.loaders.ArtistListLoader;
import ua.ck.ghplayer.models.ArtistInfo;

public class ArtistInfoUtils {

    public static ArtistInfo getArtistInfo(Context context, String artistName) {
        Realm realm = Realm.getInstance(Application.realmConfiguration);
        ArtistInfo artistInfo = realm.where(ArtistInfo.class).equalTo("artistName", artistName).findFirst();

        return artistInfo;
    }

    public static void setArtistInfo(Context context, String artistName) {
        Realm realm = Realm.getInstance(Application.realmConfiguration);
        ArtistInfo artistInfo = realm.where(ArtistInfo.class).equalTo("artistName", artistName).findFirst();

        if (artistInfo == null) {
            try {
                ArtistInfo artistInfoLoaded = new ArtistInfoLoader(context).execute(artistName).get();
                if (artistInfoLoaded != null) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(artistInfoLoaded);
                    realm.commitTransaction();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    public static void setAllArtistInfo(Context context) {

        Cursor cursor = new ArtistListLoader(context).loadInBackground();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));

                setArtistInfo(context, artist);
            } while (cursor.moveToNext());
        }

    }

}
