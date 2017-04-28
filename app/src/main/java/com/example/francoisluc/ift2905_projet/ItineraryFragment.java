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

/**
 * Created by François Luc on 2017-04-26.
 */

public class ItineraryFragment extends Fragment implements View.OnClickListener{
    EditText itinStartLocation, itinDestLocation;
    ImageButton itinerary;
    MyGMapFragment mapFrag;
    FragmentManager fragmentManager;

    public ItineraryFragment()
    {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_itinerary, container, false);
        mapFrag = new MyGMapFragment();
        itinStartLocation = (EditText) v.findViewById(R.id.starttextView);
        itinDestLocation = (EditText) v.findViewById(R.id.desttextView);
        itinerary = (ImageButton) v.findViewById(R.id.itinerarybutton);
        itinerary.setOnClickListener(this);

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.itinerary_content_frame, mapFrag).commit();
        return v;
    }

    @Override
    public void onClick(View v) {
        //call map method
    }
}
