package com.datingmiage.app.datingmiage.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    String format = bundle.getString("format");
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Pattern p = Pattern.compile("^([-+]?)([\\d]{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?)$");
                    Matcher m = p.matcher(message);
                    boolean b = m.matches();

                    // si recherche fructueuse
                    if (b) {
                        Toast.makeText(context, "Rendez vous proposé par \n" + phoneNumber + "\n aux coordonnées \n"  + message, Toast.LENGTH_SHORT).show();
                        Log.d("TOAST", "Rendez vous proposé par \n" + phoneNumber + "\n aux coordonnées \n"  + message);

                        //start activity
                        Intent myIntent = new Intent();
                        myIntent.setClass(context, MainActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myIntent);
                    }

                } // end for loop
            } // bundle is null
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}