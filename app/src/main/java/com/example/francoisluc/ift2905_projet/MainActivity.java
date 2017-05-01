package com.example.francoisluc.ift2905_projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.francoisluc.ift2905_projet.Database.StationsDB;

public class MainActivity extends AppCompatActivity {

    private TabLayout mainhost;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    public final int NB_TABS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainhost = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        mainhost.setupWithViewPager(pager);

        mainhost.getTabAt(0).setIcon(R.drawable.ic_bicycle_pictogram);
        mainhost.getTabAt(1).setIcon(R.drawable.ic_docks_30px);
        mainhost.getTabAt(2).setIcon(R.drawable.ic_road_with_two_placeholders);

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        BixiFragment bixiFragment;
        DocksFragment docksFragment;
        ItineraryFragment itineraryFragment;

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            switch(position) {
                case 0:
                    if(bixiFragment == null)
                        bixiFragment=new BixiFragment();
                    return bixiFragment;
                case 1:
                    if(docksFragment == null)
                        docksFragment= new DocksFragment();
                    return docksFragment;
                case 2:
                    if(itineraryFragment == null)
                        itineraryFragment=new ItineraryFragment();
                    return itineraryFragment;
                default:return null;
            }
        }

        @Override
        public int getCount() {
            return NB_TABS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Favorite:
                Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
