package ua.ck.ghplayer.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.fragments.AlbumListFragment;
import ua.ck.ghplayer.models.ArtistInfo;
import ua.ck.ghplayer.utils.ArtistInfoUtils;

public class ArtistInfoActivity extends AppCompatActivity {
    private String artistName;
    private long artistId;
    private ArtistInfo artistInfo;
    boolean isContentShort = true;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);

        getExtraBundle();
        setToolbar();
        setAlbumInfoContentView();
        setAlbumListView();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                //finish();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExtraBundle() {
        Bundle intentBundle = getIntent().getExtras();
        artistName = intentBundle.getString("ARTIST_NAME");
        artistId = intentBundle.getLong("ARTIST_ID");
        artistInfo = ArtistInfoUtils.getArtistInfo(getApplicationContext(), artistName);
    }


    private void setToolbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.activity_artist_info__toolbar);
        setSupportActionBar(toolbar);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("About " + artistName);
        }
    }


    private void setAlbumListView() {
        Bundle bundle = new Bundle();
        bundle.putLong("ARTIST_ID", artistId);


        AlbumListFragment albumList = new AlbumListFragment();
        albumList.setArguments(bundle);

        FrameLayout artistAlbumList = (FrameLayout) findViewById(R.id.activity_artist_info__albums);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(artistAlbumList.getId(), albumList);
        fragmentTransaction.commit();
    }

    private void setAlbumInfoContentView() {
        final ScrollView info = (ScrollView) findViewById(R.id.activity_artist_info__info);

        //checking ArtistInfo availability
        if ((artistInfo == null) || (artistInfo.getContent() == null) || (artistInfo.getContent().isEmpty())) {
            info.setVisibility(View.GONE);
            return;
        }


        final TextView content = (TextView) findViewById(R.id.activity_artist_info__content);
        ImageView cover = (ImageView) findViewById(R.id.activity_artist_info__cover);
        final Button button = (Button) findViewById(R.id.activity_artist_info__read_more);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContentShort) {
                    content.setText(artistInfo.getContent());
                    button.setText("Hide");
                    isContentShort = false;
                } else {
                    content.setText(artistInfo.getSummary());
                    button.setText("Show more");
                    isContentShort = true;
                }
            }
        });

        content.setText(artistInfo.getSummary());

        Picasso.with(this)
                .load(artistInfo.getArtistArtUrl())
                .placeholder(R.drawable.album_cover_default)
                .error(R.drawable.album_cover_default)
                .into(cover);
    }
}
