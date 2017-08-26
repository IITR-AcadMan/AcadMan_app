package net.acadman;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import static android.content.Context.MODE_PRIVATE;


public class Prefrences extends Fragment {
    Button change_pass;
    Button sessions;
    Button change_cou;
    Button admin_rig;
    AlertDialog ss;
    AlertDialog ss1;
    String cources;
    String tidsp,tokensp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_prefrences, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Prefrences");
        change_pass = (Button)v.findViewById(R.id.change_pass);
        sessions = (Button)v.findViewById(R.id.sessions);
        change_cou = (Button)v.findViewById(R.id.change_cou);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        admin_rig = (Button)v.findViewById(R.id.apply_admin);
        SharedPreferences presp = getActivity().getSharedPreferences("Personal_Data", getActivity().MODE_PRIVATE);
        tidsp  = presp.getString("tid", "null");
        tokensp = presp.getString("token","null");


        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                 ss= new AlertDialog.Builder(getActivity())
                        .setTitle("Change Password")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel",null)
                          .setView(R.layout.change_pass)
                        .create();
                ss.show();
                ss.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v){
                        final TextInputLayout oldpasstil = (TextInputLayout) (ss).findViewById(R.id.oldpasstil);
                        TextInputLayout conpasstil = (TextInputLayout) (ss).findViewById(R.id.conpasstil);
                        TextInputLayout newpasstil = (TextInputLayout) (ss).findViewById(R.id.newpasstil);
                        final LinearLayout mainchapa = (LinearLayout)(ss).findViewById(R.id.mainchapa);
                        final ProgressBar diapro = (ProgressBar)(ss).findViewById(R.id.progdia);
                        EditText oldpassedit = (EditText) (ss).findViewById(R.id.oldpassword);
                        EditText newpassedit = (EditText) (ss).findViewById(R.id.newpassword);
                        EditText conpassedit = (EditText) (ss).findViewById(R.id.conpassword);
                        String oldpass = oldpassedit.getText().toString().trim();
                        String newpass = newpassedit.getText().toString().trim();
                        String conpass = conpassedit.getText().toString().trim();
                      try{  oldpasstil.setError(null);
                        newpasstil.setError(null);
                        conpasstil.setError(null);}
                      catch (Exception e){}
                        if(oldpass.equals("")){oldpasstil.setError("Fill all fields");}
                        else if (newpass.equals("")){newpasstil.setError("Fill all fields");}
                        else if (conpass.equals("")){conpasstil.setError("Fill all fields");}
                        else if(!newpass.equals(conpass)){
                           conpasstil.setError("Password doesn't match!");}
                        else {mainchapa.setAlpha(0.4f);
                            diapro.setVisibility(View.VISIBLE);
                            PostRequest chapass = new PostRequest(new String[]{"reqid=", "&tid=", "&token=", "&pwd=", "&newpwd="}, new String[]{"16",tidsp,tokensp,hashconverter(oldpass),hashconverter(newpass)});
                            chapass.execute();

                            chapass.bindListener(new JsonListener() {
                                @Override
                                public void jsonreceived(String string) {
                                    try {
                                        JSONObject main = new JSONObject(string);
                                        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
                                        if(main.getString("err").equals("200")){
                                            Toast.makeText(getActivity(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                                            ss.dismiss();
                                        }
                                        else if (main.getString("err").equals("401")){
                                            oldpasstil.setError("Incorrect old password");
                                            mainchapa.setAlpha(1f);
                                            diapro.setVisibility(View.INVISIBLE);
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "403", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }

                    }
                });
            }
            else {
                    Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }}
        });


        ////////////////////////////////////////////

        change_cou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(isOnline()){

                ss1= new AlertDialog.Builder(getActivity())
                        .setTitle("Change Courses")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setView(R.layout.change_courses)
                        .create();
                ss1.show();

                final TextView mains = (TextView) (ss1).findViewById(R.id.main);
                final  ProgressBar pu=(ProgressBar)(ss1).findViewById(R.id.procou);
                final  FrameLayout fl = (FrameLayout) (ss1).findViewById(R.id.maincou);
                cources = "Currently enrolled for: \n";
                fl.setAlpha(0.4f);
                pu.setVisibility(View.VISIBLE);
                PostRequest cou = new PostRequest(new String[]{"reqid=","&tid=","&token="},new String[]{"9",tidsp,tokensp});
                cou.execute();
                cou.bindListener(new JsonListener() {
                    @Override
                    public void jsonreceived(String string) {
                        fl.setAlpha(1f);
                        pu.setVisibility(View.INVISIBLE);
                        try{  JSONObject main = new JSONObject(string);
                            if(main.getString("err").equals("200")){
                                JSONArray coursearray = main.getJSONArray("courses");
                                for(int a=0;a<coursearray.length();a++){
                                   cources= cources.concat(coursearray.getString(a));
                                    cources= cources.concat("\n");}
                              cources=  cources.concat("Contact Admin to change. \n");}
                            mains.setText(cources);    }
                        catch(Exception e){
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }


                    }
                });



            }else {
                Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }});

        //////////////////////////////////////
        admin_rig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ss1= new AlertDialog.Builder(getActivity())
                        .setTitle("Admin Rights")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setView(R.layout.change_courses)
                        .create();
                ss1.show();

                final TextView mains = (TextView) (ss1).findViewById(R.id.main);
                final  ProgressBar pu=(ProgressBar)(ss1).findViewById(R.id.procou);
                pu.setVisibility(View.INVISIBLE);

                mains.setText("To apply for admin rights for your batch contact admin. \n");



            }
        });
        /////////////////////////////////////////////////////////////////
        sessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){

                    ss1= new AlertDialog.Builder(getActivity())
                            .setTitle("Sessions")
                            .setPositiveButton("Logout All", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .setView(R.layout.change_courses)
                            .create();
                    ss1.show();
                    ss1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v){

                            final TextView mains1 = (TextView) (ss1).findViewById(R.id.main);
                            final  ProgressBar pu1=(ProgressBar)(ss1).findViewById(R.id.procou);
                            final  FrameLayout fl1 = (FrameLayout) (ss1).findViewById(R.id.maincou);
                            fl1.setAlpha(0.4f);
                            pu1.setVisibility(View.VISIBLE);
                            PostRequest cou1 = new PostRequest(new String[]{"reqid=","&tid=","&token="},new String[]{"15",tidsp,tokensp});
                            cou1.execute();
                            cou1.bindListener(new JsonListener() {
                                @Override
                                public void jsonreceived(String string) {
                                    fl1.setAlpha(1f);
                                    pu1.setVisibility(View.INVISIBLE);
                                    try{  JSONObject main = new JSONObject(string);
                                        if(main.getString("err").equals("200")){
                                            Toast.makeText(getActivity(), "Sucessfully Logged out", Toast.LENGTH_SHORT).show();
                                            Intent logout = new Intent(getActivity(),Login.class);

                                            SharedPreferences.Editor clearall = getActivity().getSharedPreferences("JSONs", MODE_PRIVATE).edit();
                                            clearall.clear().commit();
                                            clearall = getActivity().getSharedPreferences("Personal_Data",MODE_PRIVATE).edit();
                                            clearall.clear().commit();
                                            getActivity().startActivity(logout);
                                        ss1.dismiss();}

                                        mains1.setText(cources);    }
                                    catch(Exception e){
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }});
                    final TextView mains = (TextView) (ss1).findViewById(R.id.main);
                    final  ProgressBar pu=(ProgressBar)(ss1).findViewById(R.id.procou);
                    final  FrameLayout fl = (FrameLayout) (ss1).findViewById(R.id.maincou);
                    cources = "Logged In from ";
                    fl.setAlpha(0.4f);
                    pu.setVisibility(View.VISIBLE);
                    PostRequest cou = new PostRequest(new String[]{"reqid=","&tid=","&token="},new String[]{"17",tidsp,tokensp});
                    cou.execute();
                    cou.bindListener(new JsonListener() {
                        @Override
                        public void jsonreceived(String string) {
                            fl.setAlpha(1f);
                            pu.setVisibility(View.INVISIBLE);
                            try{  JSONObject main = new JSONObject(string);
                                if(main.getString("err").equals("200")){
                                    String count = main.getString("count");
                                        cources= cources.concat(count);
                                    cources=  cources.concat(" device(s). \n");}
                                mains.setText(cources);    }
                            catch(Exception e){
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });



                }else {
                    Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }
            }});

        return v;
    }
    public String hashconverter(String raw){
        String kk = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String text = raw;

            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();
            kk=String.format("%064x", new java.math.BigInteger(1, digest));
        }
        catch(Exception e){
            kk="Technical Error";
        }
        return kk;
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();}


}
