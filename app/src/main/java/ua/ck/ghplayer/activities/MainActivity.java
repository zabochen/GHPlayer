package ua.ck.ghplayer.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.fragments.AlbumListFragment;
import ua.ck.ghplayer.fragments.ArtistListFragment;
import ua.ck.ghplayer.fragments.GenreListFragment;
import ua.ck.ghplayer.fragments.PlaylistListFragment;
import ua.ck.ghplayer.fragments.TrackListFragment;
import ua.ck.ghplayer.interfaces.ItemClickFragmentSetter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ItemClickFragmentSetter {

    // Bundle
    private static final String KEY_NAVIGATION_VIEW_ITEM_SELECTED = "NavigationViewItemSelected";

    // Values
    private static int navigationViewItemSelected;

    // Views
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setInstanceState(savedInstanceState);
        setToolbar();
        setNavigationView();
        setFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NAVIGATION_VIEW_ITEM_SELECTED, navigationViewItemSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setInstanceState(Bundle bundle) {
        if (bundle == null) {
            navigationViewItemSelected = R.id.menu_navigation_view_item_tracks;
        } else {
            navigationViewItemSelected = bundle.getInt(KEY_NAVIGATION_VIEW_ITEM_SELECTED);
        }
    }

    private void setToolbar() {
        // Toolbar
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle(R.string.app_name);
        }
    }

    private void setNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.toggle_navigation_view_open, R.string.toggle_navigation_view_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_navigation_view);
        //navigationView.inflateHeaderView(R.layout.navigation_view_header);
        navigationView.inflateMenu(R.menu.menu_navigation_view);
        //navigationView.getMenu().findItem(navigationViewItemSelected).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment() {
        // Activity Container & Fragment
        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        Fragment listFragment = null;
        Fragment optionalFragment = null;
        //todo: add function for Boolean isLandscapeOrientedTablet
        boolean isLandscapeOrientedTablet = false;


        switch (navigationViewItemSelected) {
            case R.id.menu_navigation_view_item_tracks:
                TrackListFragment trackListFragment = new TrackListFragment();
                listFragment = trackListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_albums:
                AlbumListFragment albumListFragment = new AlbumListFragment();
                listFragment = albumListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_artists:
                ArtistListFragment artistListFragment = new ArtistListFragment();
                listFragment = artistListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_genres:
                GenreListFragment genreListFragment = new GenreListFragment();
                listFragment = genreListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_playlists:
                PlaylistListFragment playlistListFragment = new PlaylistListFragment();
                listFragment = playlistListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            default:
                break;
        }

        // Add Fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(activityFrameLayout.getId(), listFragment);
        if (isLandscapeOrientedTablet) {
            //fragmentTransaction.replace(, optionalFragment);
        }
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        navigationViewItemSelected = item.getItemId();
        setFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAlbumListItemClick(String album, int albumId) {
        Toast.makeText(this, album + String.valueOf(albumId), Toast.LENGTH_SHORT).show();

        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        TrackListFragment trackListFragment = new TrackListFragment();
        fragmentTransaction.addToBackStack("Album");
        fragmentTransaction.replace(activityFrameLayout.getId(), trackListFragment);

        fragmentTransaction.commit();

    }

    @Override
    public void onArtistListItemClick(int artistId) {
        Toast.makeText(this, String.valueOf(artistId), Toast.LENGTH_SHORT).show();

        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        TrackListFragment trackListFragment = new TrackListFragment();
        fragmentTransaction.addToBackStack("Artist");
        fragmentTransaction.replace(activityFrameLayout.getId(), trackListFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onPlaylistListItemClick(int playlistId) {
        Toast.makeText(this,String.valueOf(playlistId), Toast.LENGTH_SHORT).show();

        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        TrackListFragment trackListFragment = new TrackListFragment();
        fragmentTransaction.addToBackStack("Playlist");
        fragmentTransaction.replace(activityFrameLayout.getId(), trackListFragment);

        fragmentTransaction.commit();

    }
}
