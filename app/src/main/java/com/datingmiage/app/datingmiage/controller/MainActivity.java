package com.datingmiage.app.datingmiage.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.datingmiage.app.datingmiage.R;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                ArrayList<String> coordonnes = parseCode(message);//Parse verification code
                // ICI IL FAUT FAIRE LE CODE ACCEPTER OU NON LE RDV

            }
        });
        smsVerifyCatcher.setFilter("/My Location is - \n" + "Lat: +[-+]?\\d{1,3}.\\d{1,10}\n" + "Long: +[-+]?\\d{1,3}.\\d{1,10}/gm");
    }

    public void searchContact(View view) {
        Intent intent = new Intent(getBaseContext(), SearchContactActivity.class);
        startActivity(intent);
    }

    public void contactByNum(View view) {
        Intent intent = new Intent(getBaseContext(), ContactNumberActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }
    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from message string
     */
    private ArrayList<String> parseCode(String message) {
        Pattern p = Pattern.compile("/[-+]?\\d{1,3}.\\d{1,10}/gm");
        Matcher m = p.matcher(message);
        ArrayList<String> coordonnes = new ArrayList<String>();
        while (m.find()) {
            coordonnes.add(m.group());
        }
        return coordonnes;
    }

}
