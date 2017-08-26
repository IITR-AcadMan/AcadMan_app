package net.acadman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.JSONObject;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kaishu on 8/13/2017.
 */

public class Logout {
    String tidsp,tokensp;
    PostRequest logoutreq;
    Context context;

    public Logout(Context context) {
    this.context = context;
    }
    public void logging_out(final Context context){

        SharedPreferences outsp = context.getSharedPreferences("Personal_Data", context.MODE_PRIVATE);
        tidsp  = outsp.getString("tid", "null");
        tokensp = outsp.getString("token","null");
        Dialog ss= new AlertDialog.Builder(context)
                .setTitle("Are You Sure ?")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             if(isOnline()){
                        logoutreq = new PostRequest(new String[]{"reqid=", "&tid="}, new String[]{"2", tidsp});
                        logoutreq.execute();
                        logoutreq.bindListener(new JsonListener() {
                            @Override
                            public void jsonreceived(String string) {
                                try {
                                    JSONObject main = new JSONObject(string);
                                    String check = main.getString("err");
                                    if (check.equals("200")) {
                                        Toast.makeText(context, "Successfully logged out", Toast.LENGTH_SHORT).show();
                                        Intent logout = new Intent(context, Login.class);

                                        SharedPreferences.Editor clearall = context.getSharedPreferences("JSONs", MODE_PRIVATE).edit();
                                        clearall.clear().commit();
                                        clearall = context.getSharedPreferences("Personal_Data", MODE_PRIVATE).edit();
                                        clearall.clear().commit();
                                        context.startActivity(logout);
                                    } else {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                   else {
                                 Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                             } }
                })
                .setNegativeButton("Cancel",null)
                .create();
        ss.show();




    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();}
}
