package ua.ck.ghplayer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.models.ArtistInfo;
import ua.ck.ghplayer.loaders.ArtistInfoLoader;

public class ArtistInfoFragment extends Fragment {
    private ArtistInfo artistInfo;
    private int artistId;
    private String artistName;

    public ArtistInfoFragment() {
        super();
    }

    public void setArtistInfo(int artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        try {
            artistInfo = new ArtistInfoLoader(getContext()).execute(artistName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TextView info = (TextView) view.findViewById(R.id.fragment_artist_info__artist_info);
        ImageView cover = (ImageView) view.findViewById(R.id.fragment_artist_info__artist_cover);

        info.setText(artistInfo.getSummary());
        Picasso.with(getContext())
                .load(artistInfo.getArtistArtUrl())
                //.placeholder(R.drawable.album_placeholder)
                .into(cover);

        Bundle bundle = new Bundle();
        String[] str = new String[]{String.valueOf(artistId)};
        bundle.putStringArray("ARTIST_ID",str);

        AlbumListFragment albumList = new AlbumListFragment();
        albumList.setArguments(bundle);
        FrameLayout artistAlbumList = (FrameLayout) view.findViewById(R.id.fragment_artist_info__album_list);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(artistAlbumList.getId(), albumList);
        fragmentTransaction.commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
