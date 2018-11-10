package ali.naseem.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ali.naseem.newsapp.adapters.NewsAdapter;
import ali.naseem.newsapp.models.News;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter adapter;
    private ProgressBar loadingIndicator;
    private TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list);
        loadingIndicator = findViewById(R.id.loading_indicator);
        empty_view = findViewById(R.id.empty_view);
        listView.setEmptyView(empty_view);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News news = adapter.getItem(position);
                Uri earthquakeUri = Uri.parse(news.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            empty_view.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public android.content.Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> news) {
        adapter.setItems(news);
        loadingIndicator.setVisibility(View.GONE);
        if (news.size() == 0)
            empty_view.setText(R.string.no_news);
        else
            empty_view.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        adapter.setItems(new ArrayList<News>());
        loadingIndicator.setVisibility(View.VISIBLE);
        empty_view.setText("");
    }
}
