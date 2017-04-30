package com.example.francoisluc.ift2905_projet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.francoisluc.ift2905_projet.Database.StationsDB;
import com.example.francoisluc.ift2905_projet.Database.StationsTableElement;

import java.util.ArrayList;


public class ResultsListFragment extends Fragment {

    private StationsDB db;
    private ArrayList<Station> results_list;
    private int mode;

    public ResultsListFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new StationsDB(getContext());
        View rootView = inflater.inflate(R.layout.results_list_fragment, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.result_list_view);

        Bundle args = getArguments();
        results_list = args.getParcelableArrayList("stationsList") ;
        mode = args.getInt("mode");
        if(mode == 1){
            ResultsListAdapterBixi adapter = new ResultsListAdapterBixi(getContext(), results_list);
            lv.setAdapter(adapter);
        }
        else if(mode == 2){
            ResultsListAdapterDock adapter = new ResultsListAdapterDock(getContext(), results_list);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Station s = results_list.get(position);
                db.open();
                db.insertStation(new StationsTableElement(s.getId()));
                db.close();
            }
        });
        return rootView;
    }

}
