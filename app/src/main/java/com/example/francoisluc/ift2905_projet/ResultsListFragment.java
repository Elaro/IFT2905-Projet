package com.example.francoisluc.ift2905_projet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class ResultsListFragment extends Fragment {

    public ResultsListFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.results_list_fragment, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.result_list_view);

        ArrayList<Station> results_list = new ArrayList<Station>() ;

        Station st1 = new Station(11, "Édouard-Montpetit (Université de Montréal",
                1, 0, 0, 12, 2);
        Station st2 = new Station(12, "Louis Collin / Willowdale",
                1,0,0,7,8);
        results_list.add(st1);
        results_list.add(st2);
        ResultsListAdapter adapter = new ResultsListAdapter(getContext(), results_list);
        lv.setAdapter(adapter);
        return rootView;
    }

}
