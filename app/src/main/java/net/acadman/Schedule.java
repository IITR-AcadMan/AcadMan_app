package net.acadman;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;




public class Schedule extends Fragment {
    ViewPager viewPager;
ProgressBar st;
    String tidsp,tokensp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_schedule,container, false);


         viewPager = (ViewPager) view.findViewById(R.id.viewpager);


        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        SharedPreferences ussp = getActivity().getSharedPreferences("Personal_Data", getActivity().MODE_PRIVATE);
        tidsp  = ussp.getString("tid", "null");
        tokensp = ussp.getString("token","null");
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Schedule");

st = (ProgressBar)view.findViewById(R.id.progressBarst);
getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        SharedPreferences courselistsp = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE);
        String courseJSON  = courselistsp.getString("ds", "null");
        if(!courseJSON.equals("null")){
            setupViewPager(viewPager);
            viewPager.setCurrentItem(1);
            viewPager.setOffscreenPageLimit(3);
        }
        else{
st.setVisibility(View.VISIBLE);
        PostRequest schedulepostreq = new PostRequest(new String[]{"reqid=","&tid=","&token=","&type="},new String[]{"5",tidsp,tokensp,"ds"});
        schedulepostreq.execute();
        schedulepostreq.bindListener(new JsonListener() {

            @Override
            public void jsonreceived(String string) {
                SharedPreferences.Editor couinsert = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
                couinsert.putString("ds", string);
                couinsert.apply();
                PostRequest schedulepostreq = new PostRequest(new String[]{"reqid=","&tid=","&token=","&type="},new String[]{"5",tidsp,tokensp,"cs"});
                schedulepostreq.execute();
                schedulepostreq.bindListener(new JsonListener() {

                    @Override
                    public void jsonreceived(String string) {
                        SharedPreferences.Editor couinsert = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
                        couinsert.putString("us", string);
                        couinsert.apply();
                        PostRequest schedulepostreq = new PostRequest(new String[]{"reqid=","&tid=","&token=","&type="},new String[]{"5",tidsp,tokensp,"us"});
                        schedulepostreq.execute();
                        schedulepostreq.bindListener(new JsonListener() {

                            @Override
                            public void jsonreceived(String string) {
                                SharedPreferences.Editor couinsert = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
                                couinsert.putString("cs", string);
                                couinsert.apply();
                                setupViewPager(viewPager);
                                viewPager.setCurrentItem(1);
                                viewPager.setOffscreenPageLimit(3);
                                st.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
        });}
        return view;}


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        Toast.makeText(getActivity(), "Tap slot to change it", Toast.LENGTH_SHORT).show();
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new DefaultSchedule(), "Default Schedule");
        adapter.addFragment(new CurrentSchedule(), " Current Schedule");
        adapter.addFragment(new UpcomingSchedule(), " Upcomimg Schedule");

        viewPager.setAdapter(adapter);



    }



    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}