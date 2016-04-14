package ua.ck.ghplayer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.models.ArtistInfo;
import ua.ck.ghplayer.utils.ArtistInfoUtils;


public class ArtistAboutFragment extends Fragment {
    private TextView content;
    private ImageView cover;
    private String artistContent = "It's nothing to show here.";
    private String artistArt;

    public ArtistAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        content = (TextView) view.findViewById(R.id.fragment_artist_about__content);
        cover = (ImageView) view.findViewById(R.id.fragment_artist_about__cover);

        String artistName = getActivity().getIntent().getExtras().getString("ARTIST_NAME");
        ArtistInfo artistInfo = ArtistInfoUtils.getArtistInfo(getContext(), artistName);

        if (artistInfo != null) {
            if ((artistInfo.getContent() != null) & (!artistInfo.getContent().isEmpty())) {
                artistContent = artistInfo.getContent();
            }


            artistArt = artistInfo.getArtistArtUrl() != null ? artistInfo.getArtistArtUrl() : "R.drawable.album_cover_default";
        }

        content.setText(artistContent);

        Picasso.with(getContext())
                .load(artistArt)
                .placeholder(R.drawable.album_cover_default)
                .error(R.drawable.album_cover_default)
                .into(cover);
    }
}
