package com.example.newsaggregator;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SourceAdapter extends ArrayAdapter<Source> {

    private final MainActivity mainActivity;
    private final Source[] objects;

    public SourceAdapter(@NonNull Context context, int resource, @NonNull Source[] objects) {
        super(context, resource, objects);
        this.mainActivity = (MainActivity) context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SourceViewHolder vh;

        if (convertView == null) {

            LayoutInflater inflater = mainActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);

            vh = new SourceViewHolder(convertView);

            convertView.setTag(vh);
        } else {
            vh = (SourceViewHolder) convertView.getTag();
        }

        Source source = objects[position];
        vh.name.setText(source.getName());
        // Color is set; if no color, default is white
        vh.name.setTextColor(Color.parseColor(
                mainActivity.colorCategories.getOrDefault(source.getCategory(), "#FFFFFF")));

        return convertView;
    }
}
