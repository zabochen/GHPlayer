package ua.ck.ghplayer.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ua.ck.ghplayer.models.ArtistInfo;

public class ArtistLastfmDataGetter {
    //http://www.last.fm/api/show/artist.getInfo
    private static final String API_KEY = "$api_key=c24471232c0e5a6ca4ba598c3a5bc6bb";
    private static final String SITE_URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String ARTIST_GET_INFO_METHOD = "artist.getinfo&artist=";
    private static final String DATA_FORMAT = "&format=json";
    private static final String LANGUARE = "&lang=ru";


    public static ArtistInfo getArtistInfo(Context context, String artistName) {
        String queryString = new StringBuilder()
                .append(SITE_URL)
                .append(ARTIST_GET_INFO_METHOD)
                .append(artistName)
                .append(API_KEY)
                .append(DATA_FORMAT)
                .append(LANGUARE)
                .toString();

        final String[] result = new String[2];
        try {
            final URL url = new URL(queryString);
            if (InternetConnectionHelper.isNetworkConnected(context)) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String jsonResponse = InternetConnectionHelper.convertStreamToString(url.openStream());
                            JSONObject jsonObject = new JSONObject(jsonResponse);

                            result[0] = jsonObject.getJSONObject("artist").getJSONObject("bio").getString("summary");
                            JSONArray imageArray = jsonObject.getJSONObject("artist").getJSONArray("image");

                            int imageSize = imageArray.length();
                            JSONObject imageObject = imageSize > 2 ? imageArray.getJSONObject(imageSize - 1) : imageArray.getJSONObject(0);
                            result[1] = imageObject.getString("#text");

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ArtistInfo(result[0], result[1]);
    }

}
