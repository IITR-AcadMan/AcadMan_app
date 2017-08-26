package net.acadman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private static OTP.SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();


           char[] senderchar = sender.toCharArray();
            try { String senderstring="";
                for (int a=3 ;a<senderchar.length;a++){
                    senderstring = senderstring + Character.toString(senderchar[a]);

               }
    if(senderstring.toUpperCase().equals("WAYSMS")){
                String messageBody = smsMessage.getMessageBody();

                mListener.messageReceived(messageBody);}
            }
            catch (Exception e) {
                Toast.makeText(context, "l", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static void bindListener(OTP.SmsListener listener) {
        mListener = listener;
    }
}
