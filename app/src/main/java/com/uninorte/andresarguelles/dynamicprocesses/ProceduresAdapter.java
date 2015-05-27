package com.uninorte.andresarguelles.dynamicprocesses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andres Arguelles on 27/05/2015.
 */
public class ProceduresAdapter extends ArrayAdapter<Procedure> {
    public ProceduresAdapter (Context context, ArrayList<Procedure> procedures) {
        super(context,0,procedures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Procedure procedure = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }
        // Lookup view for data population
        TextView tvId = (TextView) convertView.findViewById(R.id.tvID);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvId.setText(procedure.procedure_id+"");
        tvName.setText(procedure.name);
        // Return the completed view to render on screen
        return convertView;
    }
}
