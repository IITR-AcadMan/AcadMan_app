package net.acadman;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    List<String> JSON_element = new ArrayList<String>() ;
View v;
    EditText[] Fields = new EditText[10];
    android.support.design.widget.TextInputLayout[] til= new android.support.design.widget.TextInputLayout[2];
    Button Registerreg;
    Spinner securityquestion;
    int securityquestionint;
    FrameLayout mainpage;
    ProgressBar progresspro;
    SwipeRefreshLayout swf;
    String tidsp,tokensp;
    String enrlid="null";

    public Profile(String enrlid) {
        this.enrlid = enrlid;

    }
    public Profile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_register,container,false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        mainpage= (FrameLayout)v.findViewById(R.id.mainpage);
        progresspro = (ProgressBar)v.findViewById(R.id.progresspro);
        mainpage.setVisibility(View.INVISIBLE);
        swf = (SwipeRefreshLayout) v.findViewById(R.id.swfreg);
        swf.setOnRefreshListener(this);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        swf.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        SharedPreferences prosp1 = getActivity().getSharedPreferences("Personal_Data", getActivity().MODE_PRIVATE);
        tidsp  = prosp1.getString("tid", "null");
        tokensp = prosp1.getString("token","null");
        if(this.enrlid.equals("null")){
        enrlid = prosp1.getString("eid","null");}
        Fields[0] = (EditText)v.findViewById(R.id.EnrollmentNoreg);
        Fields[1] = (EditText)v.findViewById(R.id.Usernamereg);
        Fields[2] = (EditText)v.findViewById(R.id.passwordreg);
        Fields[3] = (EditText)v.findViewById(R.id.ConfirmPasswordreg);
        Fields[4] = (EditText)v.findViewById(R.id.FirstNamereg);
        Fields[5] = (EditText)v.findViewById(R.id.lastnamereg);
        Fields[6] = (EditText)v.findViewById(R.id.datereg);
        Fields[7] = (EditText)v.findViewById(R.id.phonenoreg);
        Fields[8] = (EditText)v.findViewById(R.id.emailreg);
        Fields[9] = (EditText)v.findViewById(R.id.answerreg);
        Registerreg = (Button)v.findViewById(R.id.registerreg);
        securityquestion=(Spinner)v.findViewById(R.id.securityquestion);
        til [0]=(TextInputLayout) v.findViewById(R.id.passwordregreg);
        til[1]=(TextInputLayout) v.findViewById(R.id.ConfirmPasswordregreg);

        for (int a =0;a<=9;a++){
            Fields[a].setFocusable(false);
            Fields[a].setClickable(false);}
        Fields[9].setVisibility(View.GONE);
        for (int b = 0; b<2;b++){
            til[b].setVisibility(View.GONE);
        }
        Registerreg.setVisibility(View.GONE);
        securityquestion.setVisibility(View.GONE);

        SharedPreferences profilesp = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE);
        String filesJSON  = profilesp.getString("profile", "null");
        if(!filesJSON.equals("null")){
mainpage.setVisibility(View.VISIBLE);
            JSON_parse_profile(filesJSON);

        }
        else{progresspro.setVisibility(View.VISIBLE);
            no_history();}
        return v;
    }
    public void no_history (){
        if(isOnline()){swf.setRefreshing(true);
        PostRequest profile_elereq = new PostRequest(new String[]{"reqid=","&tid=","&token=","&enrlid="},new String[]{"12",tidsp,tokensp,enrlid});
        profile_elereq.execute();
        profile_elereq.bindListener(new JsonListener() {
            @Override
            public void jsonreceived(String string) {
                progresspro.setVisibility(View.INVISIBLE);
                mainpage.setVisibility(View.VISIBLE);
                JSON_parse_profile(string);
                swf.setRefreshing(false);
            }
        });}
        else {
            Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }
    public void JSON_parse_profile(String JSON) {


        try {
            JSONObject main = new JSONObject(JSON);
        if(main.getString("err").equals("200")){
            JSONObject profile = main.getJSONObject("profile");
            Fields[0].setText(enrlid);
            Fields[1].setText(profile.getString("id"));
            Fields[4].setText(profile.getString("fn"));
            Fields[5].setText(profile.getString("ln"));
            Fields[6].setText(profile.getString("dob"));
            Fields[7].setText(profile.getString("ph"));
            Fields[8].setText(profile.getString("email"));
            SharedPreferences.Editor proins = getActivity().getSharedPreferences("JSONs", getActivity().MODE_PRIVATE).edit();
            proins.putString("profile", JSON);
            proins.apply();

        }
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();}

    @Override
    public void onRefresh() {

    }
}
