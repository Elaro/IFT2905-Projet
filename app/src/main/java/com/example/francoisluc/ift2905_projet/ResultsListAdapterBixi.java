package com.example.francoisluc.ift2905_projet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.francoisluc.ift2905_projet.Database.StationsDB;

import java.util.ArrayList;

/**
 * Created by Rosalie on 2017-04-30.
 */

public class ResultsListAdapterBixi extends BaseAdapter {

    private Context context;
    private LayoutInflater myInflater;
    private ArrayList<Station> dataSource;
    private StationsDB db;


    public ResultsListAdapterBixi(Context c, ArrayList<Station> data){
        context = c;
        dataSource = data;
        db = new StationsDB(c);
        myInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Station> newData){
        dataSource = newData;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View rowView = myInflater.inflate(R.layout.results_list_item, parent, false);

        final Station s = (Station) getItem(position);

        //Get elements references
        TextView stationName = (TextView) rowView.findViewById(R.id.stationName);
        TextView nbBixis = (TextView) rowView.findViewById(R.id.nbBixis);
        TextView nbDocks = (TextView) rowView.findViewById(R.id.nbDocks);
        ImageButton addFav = (ImageButton) rowView.findViewById(R.id.addFav_button);
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        String text;
        if(s.getStatus() == 2){
            stationName.setTextColor(Color.parseColor(("#999999")));
            text = s.getName() + " (OUT OF SERVICE)";
        }
        else if(s.getNbBixis() == 0){
            stationName.setTextColor(Color.parseColor("#F20707"));
            text = s.getName();
        }
        else {
            stationName.setTextColor(Color.parseColor("#06BC66"));
            text = s.getName();
        }

        stationName.setText(text);
        nbBixis.setText("" + s.getNbBixis());
        nbDocks.setText("" + s.getNbDocks());

        db.open();
        boolean inFav = db.checkIfInDatabase(s.getId());
        db.close();

        if(inFav)
            addFav.setVisibility(View.GONE);

        return rowView;
    }
}
