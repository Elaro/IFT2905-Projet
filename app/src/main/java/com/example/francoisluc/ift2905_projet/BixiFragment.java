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


public class BixiFragment extends Fragment implements View.OnClickListener{
    EditText bixiLocation;
    ImageButton bixiLocate, bixiListButton;
    boolean showingBixiMap;
    MyGMapFragmentBixi myGMapFragmentBixi;
    ResultsListFragment resultsListFragment;
    FragmentManager fragmentManager;
    final private int mode = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_bixi, container, false);
        myGMapFragmentBixi = new MyGMapFragmentBixi();
        resultsListFragment = new ResultsListFragment();
        bixiLocation = (EditText) v.findViewById(R.id.bixilocationtextView);
        bixiLocate = (ImageButton) v.findViewById(R.id.bixibutton);
        bixiLocate.setOnClickListener(this);
        bixiListButton = (ImageButton) v.findViewById(R.id.bixilistbutton);
        bixiListButton.setOnClickListener(this);
        showingBixiMap=true;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.bixi_content_frame, myGMapFragmentBixi).commit();

        return v;

    }

    @Override
    public void onClick(View v) {
        if(v == bixiListButton){
            if(showingBixiMap){
                ArrayList<Station> stationsToShow = myGMapFragmentBixi.getStationList();
                Bundle args = new Bundle();
                args.putParcelableArrayList("stationsList", stationsToShow );
                args.putInt("mode", mode);
                resultsListFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.bixi_content_frame, resultsListFragment).commit();
                showingBixiMap = false;
                bixiListButton.setImageResource(R.drawable.ic_map_black_24px);
                bixiLocate.setVisibility(View.GONE);
            }
            else{
                fragmentManager.beginTransaction().replace(R.id.bixi_content_frame, myGMapFragmentBixi).commit();
                showingBixiMap = true;
                bixiListButton.setImageResource(R.drawable.ic_list_black_24px);
                bixiLocate.setVisibility(View.VISIBLE);
            }
        }
        if(v == bixiLocate){
                myGMapFragmentBixi.findAddress(bixiLocation.getText().toString());
        }
    }

}
