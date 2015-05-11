package com.uninorte.andresarguelles.dynamicprocesses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andres Arguelles on 11/05/2015.
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {
    public CategoriesAdapter(Context context, ArrayList<Category> categories){
        super(context, 0, categories);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Category category = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }
        // Lookup view for data population
        TextView tvId = (TextView) convertView.findViewById(R.id.tvID);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvId.setText(category.id+"");
        tvName.setText(category.name);
        // Return the completed view to render on screen
        return convertView;
    }
}
