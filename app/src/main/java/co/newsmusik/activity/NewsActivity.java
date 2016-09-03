package co.newsmusik.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import co.newsmusik.R;
import co.newsmusik.adapter.NewsAdapter;


public class NewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private static final String TAG = "NewsMusik";
    List<FeedItem> feedItemList = new ArrayList<FeedItem>();
    private RecyclerView mRecyclerView;
    private NewsAdapter adapter;
    ProgressDialog pd;
    private static final String DATA = "data";
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "created";
    private static final String TAG_INTROTEXT = "introtext";
    private static final String TAG_CATEGORY = "name";
    private static final String TAG_IMAGECREDITS = "image_credits";
    private static final String TAG_SHARELINK = "image_caption";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Initialize recyclerview */
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*Downloading data from below url*/
        final String url = "http://api.newsmusik.co/news";
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

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(NewsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.news) {
            Intent intent = new Intent(NewsActivity.this, NewsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.legend) {
            Intent intent = new Intent(NewsActivity.this, LegendActivity.class);
            startActivity(intent);
        }

        if (id == R.id.interview) {
            Intent intent = new Intent(NewsActivity.this, ExclusiveInterviewActivity.class);
            startActivity(intent);
        }

        if (id == R.id.story) {
            Intent intent = new Intent(NewsActivity.this, ExclusiveStoryActivity.class);
            startActivity(intent);
        }

        if (id == R.id.male) {
            Intent intent = new Intent(NewsActivity.this, MaleActivity.class);
            startActivity(intent);
        }

        if (id == R.id.female) {
            Intent intent = new Intent(NewsActivity.this, FemaleActivity.class);
            startActivity(intent);
        }

        if (id == R.id.groupband) {
            Intent intent = new Intent(NewsActivity.this, GroupBandActivity.class);
            startActivity(intent);
        }

        if (id == R.id.album) {
            Intent intent = new Intent(NewsActivity.this, AlbumActivity.class);
            startActivity(intent);
        }

        if (id == R.id.moviesandtv) {
            Intent intent = new Intent(NewsActivity.this, MoviesAndTVActivity.class);
            startActivity(intent);
        }

        if (id == R.id.backstagestory) {
            Intent intent = new Intent(NewsActivity.this, BackstageStoryActivity.class);
            startActivity(intent);
        }

        if (id == R.id.fashion) {
            Intent intent = new Intent(NewsActivity.this, FashionActivity.class);
            startActivity(intent);
        }

        if (id == R.id.eventorganizer) {
            Intent intent = new Intent(NewsActivity.this, EventOrganizerActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

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

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(NewsActivity.this);
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
                if (statusCode ==  200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            pd.dismiss();

            /* Download complete. Lets update UI */
            if (result == 1) {
                adapter = new NewsAdapter(NewsActivity.this, feedItemList);
                mRecyclerView.setAdapter(adapter);

            } else {
                Log.e(TAG, "Failed to fetch data!");
            }

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

            for (int i = 0; i < posts.length(); i++) {
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
