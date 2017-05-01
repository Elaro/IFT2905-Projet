package com.example.francoisluc.ift2905_projet;

import java.util.List;

/**
 * Created by a on 2017-05-01.
 */

public interface DirectioFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
