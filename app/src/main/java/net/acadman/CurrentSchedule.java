package net.acadman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CurrentSchedule extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
View v;
    RecyclerView recyclerView;
  List<String> slotmap =  new ArrayList<String>();
    List<String> colormap = new ArrayList<String>();
    SwipeRefreshLayout swipeLayout;
    String tidsp,tokensp;
    AlertDialog cganhesshe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.subschedule, container, false);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        recyclerView = (RecyclerView)v.findViewById(R.id.schedulerec);
        SharedPreferences cssp = getActivity().getSharedPreferences("Personal_Data", getActivity().MODE_PRIVATE);
        tidsp  = cssp.getString("tid", "null");
        tokensp = cssp.getString("token","null");
        setRetainInstance(true);

        SharedPreferences courselistsp = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE);
        String courseJSON  = courselistsp.getString("cs", "null");
        if(!courseJSON.equals("null")){
            JSON_parser(courseJSON);

        }
        else{no_history();}





    return v;
    }


    @Override
    public void onRefresh() {
        recyclerView.setAlpha(0.4f);
no_history();

    }

    public void setRecyclerView(List<String> hm, List<String> cm){


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),6));

        slotadapter slotadapter= new slotadapter(hm,cm);
        recyclerView.setAdapter(slotadapter);
    }
    private class slotholder extends RecyclerView.ViewHolder {
         TextView name;
        public slotholder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.element);

        }
    }



    private class slotadapter extends RecyclerView.Adapter<slotholder> {
       List<String> hm;
        List<String> cm;

        public slotadapter(List<String> hm,List<String> cm) {
this.hm = hm;
this.cm = cm;
        }

        @Override
        public slotholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.schedule_element, parent, false);
            return new slotholder(view);
        }

        @Override
        public void onBindViewHolder(slotholder holder, int position)
         {int diff=1;
             FrameLayout height = (FrameLayout)v.findViewById(R.id.getheight);
             holder.name.getLayoutParams().height=(height.getHeight()-270)/10;
             if(position==0){ holder.name.setText("SCHEDULE");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
         else if(position<6){
                switch (position)
                {
                    case 1 : holder.name.setText("MONDAY");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_mon));break;
                    case 2 : holder.name.setText("TUESDAY");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_mon));break;
                    case 3 : holder.name.setText("WEDNESDAY");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_mon));break;
                    case 4 : holder.name.setText("THURSDAY");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_mon));break;
                    case 5 : holder.name.setText("FRIDAY");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_mon));break;
                }}
         else if(position==6){ holder.name.setText("8AM-9AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==12){ holder.name.setText("9AM-10AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==18){ holder.name.setText("10AM-11AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==24){ holder.name.setText("11AM-12AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==30){ holder.name.setText("12AM-1AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==36){ holder.name.setText("2AM-3AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==42){ holder.name.setText("3AM-4AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==48){ holder.name.setText("4AM-5AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position==54){ holder.name.setText("5AM-6AM");holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col));}
             else if(position>6){
              if(position<12){diff=7;}
             else if(position<18){diff=8;}
             else if(position<24){diff=9;}
             else if(position<30){diff=10;}
             else if(position<36){diff=11;}
             else if(position<42){diff=12;}
             else if(position<48){diff=13;}
             else if(position<54){diff=14;}
             else if(position<60){diff=15;}
             holder.name.setText(hm.get(position - diff));
                 final String local = hm.get(position - diff);
                 final String localint = cm.get(position - diff);
                 final int  localintint=position-diff;
                 if (cm.get(position - diff).equals("1")) {
                     holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou3));
                 } else if (cm.get(position - diff).equals("2")) {
                     holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou1));
                 } else if (cm.get(position - diff).equals("3")) {
                     holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou2));
                 } else if (cm.get(position - diff).equals("4")) {
                     holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou4));
                 }
                 else {holder.name.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_idl));}



                 holder.name.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (isOnline()) {

                             cganhesshe = new AlertDialog.Builder(getActivity())
                                     .setTitle("Change Schedule")
                                     .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     })
                                     .setNegativeButton("Cancel", null)
                                     .setView(R.layout.change_schedule)
                                     .create();
                             cganhesshe.show();
                             TextView element1 = (TextView) (cganhesshe).findViewById(R.id.element1);
                             element1.setText(local);
                             if (localint.equals("1")) {
                                 element1.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou3));
                             } else if (localint.equals("2")) {
                                 element1.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou1));
                             } else if (localint.equals("3")) {
                                 element1.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou2));
                             } else if (localint.equals("4")) {
                                 element1.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_cou4));
                             }
                             else {element1.setBackground(getResources().getDrawable(R.drawable.schedule_ele_col_idl));}
                             final ProgressBar chapro = (ProgressBar)(cganhesshe).findViewById(R.id.chansheprog) ;
                             final LinearLayout mainlch = (LinearLayout) (cganhesshe).findViewById(R.id.invisibleshe);
                             final Spinner coulist = (Spinner)(cganhesshe).findViewById(R.id.spinnerchangeschedule);
                             chapro.setVisibility(View.VISIBLE);
                             mainlch.setVisibility(View.INVISIBLE);
                             final PostRequest getcoulist= new PostRequest(new String[]{"reqid=","&tid=","&token="},new String[]{"9",tidsp,tokensp});
                             getcoulist.execute();
                             final List<String> list = new ArrayList<String>();
                             getcoulist.bindListener(new JsonListener() {
                                 @Override
                                 public void jsonreceived(String string) {
                                     try {
                                         JSONObject maincha = new JSONObject(string);
                                         if(maincha.getString("err").equals("200")){
                                             JSONArray coursearray = maincha.getJSONArray("courses");
                                             list.add("Choose one");
                                             for(int a=0;a<coursearray.length();a++){
                                                 list.add(coursearray.getString(a));  }
                                             list.add("CANCELLED");
                                             ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, list);
                                             adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                             coulist.setAdapter(adp1);
                                             chapro.setVisibility(View.INVISIBLE);
                                             mainlch.setVisibility(View.VISIBLE);
                                         }
                                     }
                                     catch (Exception e){
                                         Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });
                             cganhesshe.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                             {
                                 @Override
                                 public void onClick(View v){
                                     cganhesshe.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v){}});
                                     int row = (localintint/5)+1;
                                     int column = (localintint%5);
                                     String[] abbr ={"m","t","w","th","f"};
                                     String slot = abbr[column]+Integer.toString(row);
                                     chapro.setVisibility(View.VISIBLE);
                                     mainlch.setAlpha(0.4f);
                                     RadioGroup radiogrp = (RadioGroup) (cganhesshe).findViewById(R.id.radiogroup);
                                     RadioButton radioButton = (RadioButton)(cganhesshe).findViewById(radiogrp.getCheckedRadioButtonId());
                                     String radiotag;
                                     if(((String)coulist.getSelectedItem()).equals("CANCELLED")){
                                         radiotag="4";
                                     }
                                     else{
                                         radiotag=(String) radioButton.getTag();
                                     }
                                     PostRequest sendcha= new PostRequest(new String[]{"reqid=","&tid=","&token=","&slot=","&venue=","&type=","&schedule=","&course="},new String[]{"6",tidsp,tokensp,slot,"",radiotag,"cs",(String)coulist.getSelectedItem()});
                                     sendcha.execute();
                                     sendcha.bindListener(new JsonListener() {
                                         @Override
                                         public void jsonreceived(String string) {
                                             try {
                                                 JSONObject maincharec = new JSONObject(string);
                                                 if(maincharec.getString("err").equals("200")){
                                                     no_history();
                                                     swipeLayout.setRefreshing(false);


                                                 }
                                             }
                                             catch (Exception e){
                                                 Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                                             }

                                         }
                                     });

                                 }});
                         }
                         else {
                             Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                         }
                     } });
         }
            }

        @Override
        public int getItemCount() {
            return 60;
        }
    }
    public void no_history(){
        if(isOnline()){swipeLayout.setRefreshing(true);
        PostRequest schedulepostreq = new PostRequest(new String[]{"reqid=","&tid=","&token=","&type="},new String[]{"5",tidsp,tokensp,"cs"});
        schedulepostreq.execute();
        schedulepostreq.bindListener(new JsonListener() {

            @Override
            public void jsonreceived(String string) {
                JSON_parser(string);
                try{cganhesshe.dismiss();}
                catch (Exception e){}
                swipeLayout.setRefreshing(false);
                recyclerView.setAlpha(1f);
            }
        });}
else {
            Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            swipeLayout.setRefreshing(false);
            recyclerView.setAlpha(1f);
        }


    }
    public void JSON_parser (String JSON){
        try{
            swipeLayout.setRefreshing(false);
            JSONObject main = new JSONObject(JSON);
            String y= main.getString("err");
            if(y.equals("200")){
                JSONObject schedule = main.getJSONObject("schedule");
                JSONObject type = main.getJSONObject("type");
                String[] abbr ={"m","t","w","th","f"};
                String slotid="";
                int positioninlist=0;
                for(int b = 1;b<=9;b++){
                    for(int a=0;a<abbr.length;a++){
                        slotid=abbr[a]+Integer.toString(b);

                        if(schedule.has(slotid)){
                            slotmap.add(positioninlist,schedule.getString(slotid));
                            colormap.add(positioninlist,type.getString(slotid));
                        }
                        else{slotmap.add(positioninlist,"");
                            colormap.add(positioninlist,"0");}
               ++positioninlist;
                    }
                }
                setRecyclerView(slotmap,colormap);
                SharedPreferences.Editor couinsert = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
                couinsert.putString("cs", JSON);
                couinsert.apply();
            }

        }
        catch (Exception e){
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();}
}