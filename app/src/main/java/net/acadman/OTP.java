package net.acadman;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Array;

public class OTP extends AppCompatActivity {
EditText[] otp =new EditText[6];
    EditText otpbackground ;
    InputMethodManager keyboard;
    TextView phoneno;
    IntentFilter intentFilter=new IntentFilter();
    int focused;
    BroadcastReceiver sms = new SmsReceiver();
    String finalOTP;
    FrameLayout mainotp;
    ProgressBar otpprog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp[0]= (EditText)findViewById(R.id.otp1);
        otp[1]= (EditText)findViewById(R.id.otp2);
        otp[2]= (EditText)findViewById(R.id.otp3);
        otp[3]= (EditText)findViewById(R.id.otp4);
        otp[4]= (EditText)findViewById(R.id.otp5);
        otp[5]= (EditText)findViewById(R.id.otp6);
        otpbackground = (EditText)findViewById(R.id.OTPbackground);
 phoneno = (TextView) findViewById(R.id.phoneno);
        phoneno.setText("+91"+getIntent().getStringExtra("ph"));
        otpprog = (ProgressBar) findViewById(R.id.otpprog);
        mainotp = (FrameLayout) findViewById(R.id.mainotp);

        otpbackground.requestFocus();
otpbackground.postDelayed(new Runnable() {
    @Override
    public void run() {
       keyboard=(InputMethodManager)getSystemService(OTP.this.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(otpbackground,0);

    }
},200);


otpbackground.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        char[] otparray = s.toString().toCharArray();
        if(otparray.length==0){
            otp[0].setText("");
        }
        for (int a = 0; a<otparray.length;a++){
            otp[a].setText(Character.toString(otparray[a]));
            int b = a;
while ((5-b)>a){
    otp[(5-b)].setText("");
b++;
}
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
    char[] noofchar = s.toString().toCharArray();
    if(noofchar.length==6){
      keyboard.hideSoftInputFromWindow(otpbackground.getWindowToken(), 0);
        send_all_data();
    }
    }
});



        SmsReceiver.bindListener(new OTP.SmsListener(){
            @Override
            public void messageReceived(String messageText) {
                String encryptedOTP= messageText.replaceAll("[^0-9]", "");
          char[] encrOTP = encryptedOTP.toCharArray();
                 finalOTP="";
                for(int a = 10;a<16;a++){

             finalOTP +=   Character.toString(encrOTP[a]);

                }

        otpbackground.setText(finalOTP);
                send_all_data();

            }
        });






    }
    public interface SmsListener {
        public void messageReceived(String messageText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(sms,intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sms);
    }


public void send_all_data(){
    otpprog.setVisibility(View.VISIBLE);
    mainotp.setAlpha(0.4f);
    String[] values =new String[]{"3",getIntent().getStringExtra("enrlid"),getIntent().getStringExtra("id"),getIntent().getStringExtra("pwd"),getIntent().getStringExtra("fn"),getIntent().getStringExtra("ln"),getIntent().getStringExtra("dob"),getIntent().getStringExtra("ph"),getIntent().getStringExtra("email"),getIntent().getStringExtra("q"),getIntent().getStringExtra("a"),finalOTP};
PostRequest sendpostreq = new PostRequest(new String[]{"reqid=","&enrlid=","&id=","&pwd=","&fn=","&ln=","&dob=","&ph=","&email=","&q=","&a=","&otp="},values);
    sendpostreq.execute();

    sendpostreq.bindListener(new JsonListener() {
        @Override
        public void jsonreceived(String string) {
try {
    if(new JSONObject(string).getString("err").equals("200")){
       PostRequest finallogin= new PostRequest(new String[]{"reqid=","&id=","&pwd=","&manufacturer=","&model="},new String[]{"1",getIntent().getStringExtra("id"),getIntent().getStringExtra("pwd"), Build.MANUFACTURER,Build.MODEL});
        finallogin.execute();
        finallogin.bindListener(new JsonListener() {
            @Override
            public void jsonreceived(String string) {
                otpprog.setVisibility(View.INVISIBLE);
                mainotp.setAlpha(1f);
              try {
                  JSONObject mainlogin = new JSONObject(string);
                  if(mainlogin.getString("err").equals("200")){
                      Toast.makeText(OTP.this, "Account Created", Toast.LENGTH_SHORT).show();
                      Intent dashboard = new Intent(OTP.this,Dashboard.class);
                      startActivity(dashboard);
                  }
                  else {
                      Toast.makeText(OTP.this, "Technical Error Try Again Later", Toast.LENGTH_SHORT).show();
                      Intent regoster = new Intent(OTP.this,Register.class);
                      startActivity(regoster);
                  }
              }
              catch(Exception e){
                  Toast.makeText(OTP.this, "Error", Toast.LENGTH_SHORT).show();
              }
            }
        });


    }
    else if(new JSONObject(string).equals("206")) {
        otpprog.setVisibility(View.INVISIBLE);
        mainotp.setAlpha(1f);
        Toast.makeText(OTP.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
    }
    else {
        Toast.makeText(OTP.this, "Technical Error", Toast.LENGTH_SHORT).show();
        Intent tecni = new Intent(OTP.this,Register.class);
        startActivity(tecni);
        finish();
    }
}
        catch (Exception e){
            Toast.makeText(OTP.this, "Error", Toast.LENGTH_SHORT).show();


        }}
    });

}


}
