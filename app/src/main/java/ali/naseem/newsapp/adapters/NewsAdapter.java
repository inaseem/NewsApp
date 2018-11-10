package ali.naseem.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ali.naseem.newsapp.R;
import ali.naseem.newsapp.models.News;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        News news = (News) getItem(position);
        TextView webTitle = convertView.findViewById(R.id.webTitle);
        TextView webPublicationDate = convertView.findViewById(R.id.webPublicationDate);
        TextView sectionName = convertView.findViewById(R.id.sectionName);
        TextView authorTextView = convertView.findViewById(R.id.authorTextView);
        webTitle.setText(news.getWebTitle());
        webPublicationDate.setText(news.getWebPublicationDate());
        sectionName.setText(news.getSectionName());
        authorTextView.setText(String.format("by %s", news.getAuthor()));
        return convertView;
    }

    public void setItems(List<News> news) {
        this.clear();
        this.addAll(news);
        notifyDataSetChanged();
    }
}
