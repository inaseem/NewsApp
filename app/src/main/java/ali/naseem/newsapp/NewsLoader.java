package ali.naseem.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ali.naseem.newsapp.models.News;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private final String URL = "https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2018-09-01&api-key=test";

    public NewsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        System.out.println("INSIDE");
        List<News> list = new ArrayList<News>();
        try {
            list.addAll(extractFeatureFromJson(makeHttpRequest(createUrl(URL))));
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
        return list;
    }

    private List<News> extractFeatureFromJson(String JSON) {
        List<News> news = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            JSONObject response = jsonObject.getJSONObject("response");
            String status = response.getString("status");
            if (status.trim().equals("ok")) {
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i < results.length(); ++i) {
                    JSONObject result = results.getJSONObject(i);
                    String sectionName = result.getString("sectionName");
                    String webPublicationDate = result.getString("webPublicationDate");
                    String webUrl = result.getString("webUrl");
                    String webTitle = result.getString("webTitle");
                    news.add(new News(webTitle, sectionName, webUrl, webPublicationDate));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return news;
        }
        return news;
    }

    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
            System.out.println("RESPONSE=" + jsonResponse);
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }
}
