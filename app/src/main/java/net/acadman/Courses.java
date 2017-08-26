package net.acadman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaishu on 8/13/2017.
 */

public class Courses extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    View v;
    RecyclerView courselistrec;
    PostRequest aa;
    List <String> courselist = new ArrayList<String>();
    SwipeRefreshLayout refreshcourse;
     String sendcourse;
    String tidsp;
    String tokensp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_course_list, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Courses");
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        courselistrec = (RecyclerView) v.findViewById(R.id.courselistrec);
        courselistrec.setLayoutManager(new LinearLayoutManager(getActivity()));
         refreshcourse = (SwipeRefreshLayout) v.findViewById(R.id.courserefresh);
        SharedPreferences cousp = getActivity().getSharedPreferences("Personal_Data", getActivity().MODE_PRIVATE);
        tidsp  = cousp.getString("tid", "null");
        tokensp = cousp.getString("token","null");
        refreshcourse.setOnRefreshListener(this);
        refreshcourse.setColorSchemeColors(Color.parseColor("#669900"),
                Color.parseColor("#cc0000"),
                Color.parseColor("#0099cc"),
                Color.parseColor("#ff8800"));
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        SharedPreferences courselistsp = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE);
        String courseJSON  = courselistsp.getString("Courses_list", "null");
if(!courseJSON.equals("null")){
   JSON_parse_courses(courseJSON);

}
        else{no_history();}



//        aa= new PostRequest(new String[]{"reqid=","&tid=","&token="},new String[]{"9","1","sahu"});
     /*   aa.execute();
        aa.bindListener(new JsonListener() {
            @Override
            public void jsonreceived(String string) {
                try{
                JSONObject main = new JSONObject(string);
                JSONArray coursearray = main.getJSONArray("courses");
                    for(int a=0;a<coursearray.length();a++){
                        courselist.add(coursearray.getString(a));}
                    Courseadapter courseadapter = new Courseadapter(courselist);
                     courselistrec.setAdapter(courseadapter);
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        return  v;
    }
    private class Courseholder extends RecyclerView.ViewHolder {
         TextView name;
        public Courseholder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.courseelement);
        }
    }



    private class Courseadapter extends RecyclerView.Adapter<Courses.Courseholder> {

List<String> cl;
        public Courseadapter(List<String> cl) {
          this.cl=cl;
        }

        @Override
        public Courses.Courseholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.course_list_element, parent, false);
            return new Courses.Courseholder(view);
        }
        public void onBindViewHolder(Courses.Courseholder holder, int position) {


           sendcourse  = cl.get(position);
            holder.name.setText(sendcourse);
            final Fragment nxt = new Files(sendcourse);
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().add(R.id.baseforfragment,nxt).commit();
                }
            });

        }
        @Override
        public int getItemCount() {
            return cl.size();
        }

    }

    @Override
    public void onRefresh() {
       no_history();
    }



    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();}




    public void no_history(){
        if(isOnline()){
        aa= new PostRequest(new String[]{"reqid=","&tid=","&token="},new String[]{"9",tidsp,tokensp});
           aa.execute();
        aa.bindListener(new JsonListener() {
            @Override
            public void jsonreceived(String string) {
                JSON_parse_courses(string);
            }
        });
    }
    else {
            Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            refreshcourse.setRefreshing(false);
        }}
public void JSON_parse_courses(String JSON){

    try{
        courselist.clear();
        JSONObject main = new JSONObject(JSON);
        if(main.getString("err").equals("200")){
            JSONArray coursearray = main.getJSONArray("courses");
            for(int a=0;a<coursearray.length();a++){
                courselist.add(a,coursearray.getString(a));}


            Courseadapter courseadapter = new Courseadapter(courselist);
            courselistrec.setAdapter(courseadapter);
            SharedPreferences.Editor couinsert = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
            couinsert.putString("Courses_list", JSON);
            couinsert.apply();
            refreshcourse.setRefreshing(false);}

    }
    catch (Exception e){
        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        refreshcourse.setRefreshing(false);
    }
}
}
