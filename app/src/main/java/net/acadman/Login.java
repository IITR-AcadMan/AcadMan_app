package net.acadman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.security.MessageDigest;

public class Login extends AppCompatActivity {
    TextView username;
    TextView password;
    TextView regisintent;
    Button login;
    PostRequest aa;
    PostRequest bb;
    ProgressBar loginprog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        loginprog = (ProgressBar) findViewById(R.id.loginprog);
        regisintent = (TextView)findViewById(R.id.regisintent);
login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final String uusername =username.getText().toString().trim();
        String ppassword = password.getText().toString().trim();
        if(uusername.equals("")||ppassword.equals("")){
            Toast.makeText(Login.this, "Fill all Fields", Toast.LENGTH_SHORT).show();
        }else{
loginprog.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
            regisintent.setEnabled(false);
       String encryptpass= hashconverter(ppassword);
       aa= new PostRequest(new String[]{"reqid=","&id=","&pwd=","&manufacturer=","&model="},new String[]{"1",uusername,encryptpass, Build.MANUFACTURER,Build.MODEL});

        aa.execute();


aa.bindListener(new JsonListener() {
    @Override
    public void jsonreceived(String string) {
        loginprog.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        regisintent.setEnabled(true);
        try {JSONObject main = new JSONObject(string);
             if (main.getString("err").equals("200")){
                 SharedPreferences.Editor editr = getSharedPreferences("Personal_Data", MODE_PRIVATE).edit();
                 editr.putString("tid", main.getString("tid"));
                 editr.putString("token",main.getString("token"));
                 editr.putString("eid",main.getString("eid"));
                 editr.apply();


                 Intent dasboard = new Intent(Login.this,Dashboard.class);
                 startActivity(dasboard);
                 finish();
             }
             else
             {
            bb= new PostRequest(new String[]{"reqid=","&id="},new String[]{"8",uusername});
            bb.execute();
            bb.bindListener(new JsonListener() {
                @Override
                public void jsonreceived(String string) {
                    try{
                        if(!new JSONObject(string).getString("err").equals("200")){
                            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Login.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                     catch (Exception e){
                        Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                    }

               }});
             }
        }
        catch (Exception e){
            Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
});
    }}
});

    regisintent.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent registerintent = new Intent(Login.this,Register.class);
            startActivity(registerintent);
        }
    });





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


}
 /*  SharedPreferences.Editor editr = getSharedPreferences("restart", MODE_PRIVATE).edit();
                editr.putInt("restart", 1);
                        editr.apply(); */
 /*SharedPreferences pddref = this.getSharedPreferences("versionnet", this.MODE_PRIVATE);
        String post  = pddref.getString("versionnet", "kaishu");*/
