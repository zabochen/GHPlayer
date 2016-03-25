package ua.ck.ghplayer.utils;


import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ua.ck.ghplayer.models.ArtistInfo;

public class ArtistInfoAsyncLoader extends AsyncTask<String, Void, ArtistInfo> {
    //http://www.last.fm/api/show/artist.getInfo
    private static final String API_KEY = "&api_key=c24471232c0e5a6ca4ba598c3a5bc6bb";
    private static final String SITE_URL = "http://ws.audioscrobbler.com/2.0/?method=";
    private static final String ARTIST_GET_INFO_METHOD = "artist.getinfo&artist=";
    private static final String DATA_FORMAT = "&format=json";
    private static final String LANGUARE = "&lang=ru";
    private Context context;
    private String summary;
    private String artistArtUrl;

    public ArtistInfoAsyncLoader(Context context) {
        this.context = context;
    }

    @Override
    protected ArtistInfo doInBackground(String... params) {
        String artistName = params[0];

        String queryString = new StringBuilder()
                .append(SITE_URL)
                .append(ARTIST_GET_INFO_METHOD)
                .append(artistName)
                .append(API_KEY)
                .append(DATA_FORMAT)
                .append(LANGUARE)
                .toString();

        try {
            final URL url = new URL(queryString);
            if (InternetConnectionHelper.isNetworkConnected(context)) {

                try {
                    String jsonResponse = InternetConnectionHelper.convertStreamToString(url.openStream());
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    String rawSummary = jsonObject.getJSONObject("artist").getJSONObject("bio").getString("content");
                    summary = rawSummary.split("<a href")[0];

                    JSONArray imageArray = jsonObject.getJSONObject("artist").getJSONArray("image");

                    int imageSize = imageArray.length();
                    JSONObject imageObject = imageSize > 2 ? imageArray.getJSONObject(imageSize - 1) : imageArray.getJSONObject(0);
                    artistArtUrl = imageObject.getString("#text");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return new ArtistInfo(summary, artistArtUrl);
    }

}
