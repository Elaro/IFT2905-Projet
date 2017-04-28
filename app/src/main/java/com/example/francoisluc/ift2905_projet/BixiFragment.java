package com.example.francoisluc.ift2905_projet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by François Luc on 2017-04-26.
 */

public class BixiFragment extends Fragment implements View.OnClickListener{
    EditText bixiLocation;
    ImageButton bixiLocate, bixiListButton;
    boolean showingBixiMap;
    MyGMapFragment myGMapFragment;
    ResultsListFragment resultsListFragment;
    FragmentManager fragmentManager;

    public BixiFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_bixi, container, false);
        myGMapFragment = new MyGMapFragment();
        resultsListFragment = new ResultsListFragment();
        bixiLocation = (EditText) v.findViewById(R.id.bixilocationtextView);
        bixiLocate = (ImageButton) v.findViewById(R.id.bixibutton);
        bixiLocate.setOnClickListener(this);
        bixiListButton = (ImageButton) v.findViewById(R.id.bixilistbutton);
        bixiListButton.setOnClickListener(this);
        showingBixiMap=true;

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.bixi_content_frame, myGMapFragment).commit();

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v == bixiListButton){
            if(showingBixiMap == true){
                fragmentManager.beginTransaction().replace(R.id.bixi_content_frame, resultsListFragment).commit();
                showingBixiMap = false;
                bixiListButton.setImageResource(R.drawable.ic_map_black_24px);
            }
            else{
                fragmentManager.beginTransaction().replace(R.id.bixi_content_frame, myGMapFragment).commit();
                showingBixiMap = true;
                bixiListButton.setImageResource(R.drawable.ic_list_black_24px);
            }
        }
        if(v == bixiLocate){
            //call map methods
        }
    }
}
