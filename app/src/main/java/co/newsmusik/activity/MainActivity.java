package co.newsmusik.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.newsmusik.FeedItem;
import co.newsmusik.HttpHandler;
import co.newsmusik.R;
import co.newsmusik.adapter.MyRecyclerAdapter;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private static final String TAG = "NewsMusik";
    List<FeedItem> feedItemList = new ArrayList<FeedItem>();
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int PAGE_SIZE = 50;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    ProgressDialog pd;
    private static final String DATA = "data";
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "created";
    private static final String TAG_INTROTEXT = "introtext";
    private static final String TAG_CATEGORY = "name";
    private static final String TAG_IMAGECREDITS = "image_credits";
    private static final String TAG_SHARELINK = "image_caption";
    private static String url = "http://api.newsmusik.co/articles";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Initialize recyclerview */
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        /*Downloading data from below url*/
        final String url = "http://api.newsmusik.co/articles";
        new AsyncHttpTask().execute(url);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Warning")
                    .setMessage("Please check your internet connection")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .show();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Yakin keluar aplikasi ?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Cari Artikel...");
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(feedItemList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<FeedItem> filteredModelList = filter(feedItemList, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<FeedItem> filter(List<FeedItem> models, String query) {
        query = query.toLowerCase();

        final List<FeedItem> filteredModelList = new ArrayList<>();
        for (FeedItem model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.news) {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.legend) {
            Intent intent = new Intent(MainActivity.this, LegendActivity.class);
            startActivity(intent);
        }

        if (id == R.id.interview) {
            Intent intent = new Intent(MainActivity.this, ExclusiveInterviewActivity.class);
            startActivity(intent);
        }

        if (id == R.id.story) {
            Intent intent = new Intent(MainActivity.this, ExclusiveStoryActivity.class);
            startActivity(intent);
        }

        if (id == R.id.male) {
            Intent intent = new Intent(MainActivity.this, MaleActivity.class);
            startActivity(intent);
        }

        if (id == R.id.female) {
            Intent intent = new Intent(MainActivity.this, FemaleActivity.class);
            startActivity(intent);
        }

        if (id == R.id.groupband) {
            Intent intent = new Intent(MainActivity.this, GroupBandActivity.class);
            startActivity(intent);
        }

        if (id == R.id.album) {
            Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
            startActivity(intent);
        }

        if (id == R.id.moviesandtv) {
            Intent intent = new Intent(MainActivity.this, MoviesAndTVActivity.class);
            startActivity(intent);
        }

        if (id == R.id.backstagestory) {
            Intent intent = new Intent(MainActivity.this, BackstageStoryActivity.class);
            startActivity(intent);
        }

        if (id == R.id.fashion) {
            Intent intent = new Intent(MainActivity.this, FashionActivity.class);
            startActivity(intent);
        }

        if (id == R.id.eventorganizer) {
            Intent intent = new Intent(MainActivity.this, EventOrganizerActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://co.newsmusik/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://co.newsmusik/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.setMessage("Loading, Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.e (e.getClass().getName(), e.getMessage()==null?"":e.getMessage(),e.getCause());
            }

            return result; //"Failed to fetch data!";

        }

        @Override
        protected void onPostExecute(Integer result) {
            pd.dismiss();
            adapter = new MyRecyclerAdapter(MainActivity.this, feedItemList);
            mRecyclerView.setAdapter(adapter);

        }

    }


    private void loadData(String result) {
        try {
            isLoading = false;
            int index = adapter.getItemCount();
            int end = index + PAGE_SIZE;
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray(DATA);
            if (null == feedItemList) {
                feedItemList = new ArrayList<FeedItem>();
            }
            if (end <= posts.length()) {
                for (int i = index; i < end; i++) {
                    JSONObject post = posts.optJSONObject(i);
                    FeedItem item = new FeedItem();
                    item.setTitle(post.optString(TAG_TITLE));
                    item.setThumbnail(post.optString(TAG_PICTURE));
                    item.setCategory(post.optString(TAG_CATEGORY));
                    item.setDate(post.optString(TAG_DATE));
                    item.setContentDetail(post.optString(TAG_INTROTEXT));
                    item.setImageCredit(post.optString(TAG_IMAGECREDITS));
                    item.setShareLink(post.optString(TAG_SHARELINK));
                    feedItemList.add(item);

                    adapter.addAll(feedItemList);
                    if (end >= posts.length()) {
                        adapter.setLoading(false);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseResult(String result) {
            try {
                JSONObject response = new JSONObject(result);
                JSONArray posts = response.optJSONArray(DATA);

            /*Initialize array if null*/
                if (null == feedItemList) {
                    feedItemList = new ArrayList<FeedItem>();
                }

                for (int i = 0; i < PAGE_SIZE; i++) {
                    JSONObject post = posts.optJSONObject(i);
                    FeedItem item = new FeedItem();
                    item.setTitle(post.optString(TAG_TITLE));
                    item.setThumbnail(post.optString(TAG_PICTURE));
                    item.setCategory(post.optString(TAG_CATEGORY));
                    item.setDate(post.optString(TAG_DATE));
                    item.setContentDetail(post.optString(TAG_INTROTEXT));
                    item.setImageCredit(post.optString(TAG_IMAGECREDITS));
                    item.setShareLink(post.optString(TAG_SHARELINK));
                    feedItemList.add(item);
                }
                adapter.addAll(feedItemList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    isLoading = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            feedItemList.remove(null);
                            adapter.notifyItemRemoved(feedItemList.size());
                            HttpHandler sh = new HttpHandler();
                            String jsonStr = sh.makeServiceCall("http://api.newsmusik.co/articles");
                            Log.e(TAG, "Response from url: " + jsonStr);
                            if (jsonStr != null) {
                                try {
                                    int index = adapter.getItemCount();
                                    int end = index + PAGE_SIZE;
                                    JSONObject response = new JSONObject(jsonStr);
                                    JSONArray posts = response.optJSONArray(DATA);
                                    if (null == feedItemList) {
                                        feedItemList = new ArrayList<FeedItem>();
                                    }
                                    if (end <= posts.length()) {
                                        for (int i = index; i < end; i++) {
                                            JSONObject post = posts.optJSONObject(i);
                                            FeedItem item = new FeedItem();
                                            item.setTitle(post.optString(TAG_TITLE));
                                            item.setThumbnail(post.optString(TAG_PICTURE));
                                            item.setCategory(post.optString(TAG_CATEGORY));
                                            item.setDate(post.optString(TAG_DATE));
                                            item.setContentDetail(post.optString(TAG_INTROTEXT));
                                            item.setImageCredit(post.optString(TAG_IMAGECREDITS));
                                            item.setShareLink(post.optString(TAG_SHARELINK));
                                            feedItemList.add(item);

                                            adapter.addAll(feedItemList);
                                            if (end >= posts.length()) {
                                                adapter.setLoading(false);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            //adapter.setLoaded();
                        }
                    }, 1000);
                }
            }
        }
    };
}
