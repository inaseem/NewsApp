package ali.naseem.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ali.naseem.newsapp.adapters.NewsAdapter;
import ali.naseem.newsapp.models.News;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private NewsAdapter adapter;
    private ProgressBar loadingIndicator;
    private TextView empty_view;
    private static final String REQUEST_URL = "https://content.guardianapis.com/search";
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list);
        loadingIndicator = findViewById(R.id.loading_indicator);
        empty_view = findViewById(R.id.empty_view);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(adapter);
        listView.setEmptyView(empty_view);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News news = adapter.getItem(position);
                Uri earthquakeUri = Uri.parse(news.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
        if (isNetworkAvailable()) {
            empty_view.setText(null);
            adapter.setItems(new ArrayList<News>());
            LoaderManager loaderManager = getLoaderManager();
            loadingIndicator.setVisibility(View.VISIBLE);
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String useDate = sharedPrefs.getString(
                getString(R.string.usedate),
                "none");

        String orderBy = sharedPrefs.getString(
                getString(R.string.orderby),
                "none"
        );
        String section = sharedPrefs.getString(
                getString(R.string.section),
                "politics"
        );
        String pageSize = sharedPrefs.getString(
                getString(R.string.pagesize),
                "10"
        );

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        if (!orderBy.equals("none")) {
            uriBuilder.appendQueryParameter("order-by", orderBy);
        }
        if (!useDate.equals("none")) {
            uriBuilder.appendQueryParameter("use-date", useDate);
        }
        uriBuilder.appendQueryParameter("q", "debates");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        loadingIndicator.setVisibility(View.GONE);
        if (news == null) {
            empty_view.setText(R.string.no_news);
        } else {
            if (news.size() == 0)
                empty_view.setText(R.string.no_news_action);
            else
                empty_view.setVisibility(View.GONE);
            adapter.setItems(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.setItems(new ArrayList<News>());
        loadingIndicator.setVisibility(View.VISIBLE);
        empty_view.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadNews() {
        if (isNetworkAvailable()) {
            adapter.clear();
            empty_view.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                loadNews();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.usedate)) ||
                key.equals(getString(R.string.section)) ||
                key.equals(getString(R.string.pagesize)) ||
                key.equals(getString(R.string.orderby))) {
            adapter.clear();
            empty_view.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }
}
