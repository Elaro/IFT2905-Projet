package com.example.francoisluc.ift2905_projet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by François Luc on 2017-04-26.
 */

public class DocksFragment extends Fragment implements View.OnClickListener {
    EditText docksLocation;
    ImageButton docksLocate, docksListButton;
    boolean showingDocksMap;
    MyGMapFragmentDock myGMapFragmentD;
    ResultsListFragment resultsListFragmentD;
    FragmentManager fragmentManager;
    final private int mode = 2;

    public DocksFragment()
    {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_docks, container, false);
        myGMapFragmentD = new MyGMapFragmentDock();
        resultsListFragmentD = new ResultsListFragment();
        docksLocation = (EditText) view.findViewById(R.id.dockslocationtextView);
        docksLocate = (ImageButton) view.findViewById(R.id.docksbutton);
        docksLocate.setOnClickListener(this);
        docksListButton = (ImageButton) view.findViewById(R.id.dockslistbutton);
        docksListButton.setOnClickListener(this);
        showingDocksMap=true;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.docks_content_frame, myGMapFragmentD).commit();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == docksListButton){
            if(showingDocksMap){
                ArrayList<Station> stationsToShow = myGMapFragmentD.getStationList();
                Bundle args = new Bundle();
                args.putParcelableArrayList("stationsList", stationsToShow );
                args.putInt("mode", mode);
                resultsListFragmentD.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.docks_content_frame, resultsListFragmentD).commit();
                showingDocksMap = false;
                docksListButton.setImageResource(R.drawable.ic_map_black_24px);
                docksLocate.setVisibility(View.GONE);
            }
            else{
                fragmentManager.beginTransaction().replace(R.id.docks_content_frame, myGMapFragmentD).commit();
                showingDocksMap = true;
                docksListButton.setImageResource(R.drawable.ic_list_black_24px);
                docksLocate.setVisibility(View.VISIBLE);
            }
        }
        if(v == docksLocate){
            myGMapFragmentD.findAddress(docksLocation.getText().toString());
        }
    }
}
