package net.acadman;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Register extends AppCompatActivity {
  EditText[] Fields = new EditText[10];
    String[] values = new String[10];
    String sendingdate;
    ProgressBar regisprog;
    // EnrollmentNoreg,Usernamereg,passwordreg,ConfirmPasswordreg,FirstNamereg,lastnamereg,datereg,phonenoreg,emailreg,answerreg;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog freshdate ;
    Button Registerreg;
    Spinner securityquestion;
    int securityquestionint;
    Intent OTPActivity;
    SwipeRefreshLayout swf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Fields[0] = (EditText)findViewById(R.id.EnrollmentNoreg);
        Fields[1] = (EditText)findViewById(R.id.Usernamereg);
        Fields[2] = (EditText)findViewById(R.id.passwordreg);
        Fields[3] = (EditText)findViewById(R.id.ConfirmPasswordreg);
        Fields[4] = (EditText)findViewById(R.id.FirstNamereg);
        Fields[5] = (EditText)findViewById(R.id.lastnamereg);
        Fields[6] = (EditText)findViewById(R.id.datereg);
        Fields[7] = (EditText)findViewById(R.id.phonenoreg);
        Fields[8] = (EditText)findViewById(R.id.emailreg);
        Fields[9] = (EditText)findViewById(R.id.answerreg);
        Registerreg = (Button)findViewById(R.id.registerreg);
        securityquestion=(Spinner)findViewById(R.id.securityquestion);
        regisprog = (ProgressBar)findViewById(R.id.regisprog);
        swf= (SwipeRefreshLayout)findViewById(R.id.swfreg);
        swf.setEnabled(false);





        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        Fields[6].setOnTouchListener(new View.OnTouchListener(){

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
           freshdate = new DatePickerDialog(Register.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            DatePicker finaldate = freshdate.getDatePicker();
            String myDatemax = "2002/12/31 00:00:00";
            String myDatemin = "1980/01/01 00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            long millismax=0;
            long millismin=0;
            try{Date dateymax = sdf.parse(myDatemax);
                millismax = dateymax.getTime();
                Date dateymin = sdf.parse(myDatemin);
                millismin = dateymin.getTime();}
            catch (Exception e){}

            finaldate.setMaxDate(millismax);
            finaldate.setMinDate(millismin);
                   freshdate.show();  }
        return false;
    }
});


Registerreg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      /*  String EnrollmentNoregstring = Fields[0].getText().toString().trim();
        String Usernameregstring = Fields[1].getText().toString().trim();
        String passwordregstring = Fields[2].getText().toString().trim();
        String ConfirmPasswordregstring = Fields[3].getText().toString().trim();
        String FirstNameregstring =  Fields[4].getText().toString().trim();
        String lastnameregstring = Fields[5].getText().toString().trim();
        String dateregstring = Fields[6].getText().toString().trim();
        String phonenoregstring = Fields[7].getText().toString().trim();
        String emailregstring = Fields[8].getText().toString().trim();
        String answerregstring = Fields[9].getText().toString().trim();
       int securityquestionstring = securityquestion.getSelectedItemPosition();*/

             if(validation(Fields,values)){
                 regisprog.setVisibility(View.VISIBLE);
                 Registerreg.setVisibility(View.INVISIBLE);
                 PostRequest usernameexit= new PostRequest(new String[]{"reqid=","&id="},new String[]{"8",Fields[1].getText().toString().trim()});
                 usernameexit.execute();
                 usernameexit.bindListener(new JsonListener() {
                     @Override
                     public void jsonreceived(String string) {
                        try {
                            JSONObject main = new JSONObject(string);
                            if(main.getString("err").equals("200")){
                                all_good();
                            }
                            else {
                                Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                     }
                 });


             }
             else {
                 Toast.makeText(Register.this, "Fill all Fields", Toast.LENGTH_SHORT).show();
             }













    }
});

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String sendingformat="yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf22 = new SimpleDateFormat(sendingformat, Locale.US);
          sendingdate = sdf22.format(myCalendar.getTime());
        Fields[6].setText(sdf.format(myCalendar.getTime()));
    }




public Boolean validation(EditText[] fields,String[] values){
    Boolean validation=false;
    for (int a=0;a<fields.length;a++){
        if(fields[a].getText().toString().trim().equals("")){validation=false;
        fields[a].requestFocus();break;}
        else {validation=true;
        values[a]=fields[a].getText().toString().trim();}
    }if(validation){
    if(securityquestion.getSelectedItemPosition()==0){
        Toast.makeText(this, "Choose Security Question", Toast.LENGTH_SHORT).show();
        validation=false;}
    else{validation=true;
    securityquestionint=securityquestion.getSelectedItemPosition();}}
if(validation){
    if(fields[2].getText().toString().trim().equals(fields[3].getText().toString().trim())){
        validation =true;
    }
    else{
        Toast.makeText(this, "Entered passwords do not match", Toast.LENGTH_SHORT).show();
    fields[3].requestFocus();
        validation=false;
    }}

    return validation;
}
public void all_good(){

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (result != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Register.this,
                    new String[]{Manifest.permission.READ_SMS},
                    1);
        }
        else{OTPintent();}




    }
    public void OTPintent(){
         OTPActivity = new Intent(Register.this,OTP.class);
        OTPActivity.putExtra("enrlid",values[0]);
        OTPActivity.putExtra("id",values[1]);
        OTPActivity.putExtra("pwd",values[3]);
        OTPActivity.putExtra("fn",values[4]);
        OTPActivity.putExtra("ln",values[5]);
        OTPActivity.putExtra("dob",sendingdate);
        OTPActivity.putExtra("ph",values[7]);
        OTPActivity.putExtra("email",values[8]);
        OTPActivity.putExtra("q",Integer.toString(securityquestionint));
        OTPActivity.putExtra("a",values[9]);

        PostRequest sendotpreq=new PostRequest(new String[]{"reqid=","&ph=","&type="},new String[]{"4",values[7],"1"});
        sendotpreq.execute();
        sendotpreq.bindListener(new JsonListener() {
    @Override
    public void jsonreceived(String string) {
        try {
            Log.e("respoce",string);
            if (new JSONObject(string).getString("err").equals("200")){
                regisprog.setVisibility(View.INVISIBLE);
                Registerreg.setVisibility(View.VISIBLE);
                startActivity(OTPActivity);
            }
            else {
                Toast.makeText(Register.this, "Technical Error", Toast.LENGTH_SHORT).show();
            }
        }
    catch (Exception e){
        Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
    }
    }
});


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    OTPintent();
                    break;

                } else {
                   for (int a=0 ;a<2;a++){
                    Toast.makeText(Register.this, "You have to manually type your OTP", Toast.LENGTH_SHORT).show();}

                } OTPintent();
                return;
            }


        }
    }
}
