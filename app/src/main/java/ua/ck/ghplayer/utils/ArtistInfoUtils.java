package ua.ck.ghplayer.utils;


import android.content.Context;

import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import ua.ck.ghplayer.Application;
import ua.ck.ghplayer.loaders.ArtistInfoLoader;
import ua.ck.ghplayer.models.ArtistInfo;

public class ArtistInfoUtils {

    public static ArtistInfo getArtistInfo(Context context, String artistName) {
        Realm realm = Realm.getInstance(Application.realmConfiguration);
        ArtistInfo artistInfo = realm.where(ArtistInfo.class).equalTo("artistName", artistName).findFirst();

        if (artistInfo == null) {
            return setArtistInfo(context, artistName);
        }

        return artistInfo;
    }

    private static ArtistInfo setArtistInfo(Context context, String artistName) {
        Realm realm = Realm.getInstance(Application.realmConfiguration);

        try {
            ArtistInfo artistInfoLoaded = new ArtistInfoLoader(context).execute(artistName).get();
            if (artistInfoLoaded != null) {
                realm.beginTransaction();
                ArtistInfo artistInfo = realm.copyToRealmOrUpdate(artistInfoLoaded);
                realm.commitTransaction();
                return artistInfo;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }


}
