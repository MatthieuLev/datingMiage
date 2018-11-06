package com.datingmiage.app.datingmiage.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.datingmiage.app.datingmiage.R;
import com.datingmiage.app.datingmiage.smsverifycatcherLibrary.OnSmsCatchListener;
import com.datingmiage.app.datingmiage.smsverifycatcherLibrary.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context _selft = this;

        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String coordonnes = parseCode(message);//Parse verification code
                Log.d("TOAST", coordonnes);
                new AlertDialog.Builder(_selft)
                        .setMessage("Voulez vous accepter un rendez-vous au coordonnées : \nLat: " + coordonnes.split(",")[0] + "\nLong: " + coordonnes.split(",")[1])
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
        smsVerifyCatcher.setFilter("^([-+]?)([\\d]{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?)$");
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
    private String parseCode(String message) {
        Log.d("TOAST", message);
        Pattern p = Pattern.compile("^([-+]?)([\\d]{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?)$");
        Matcher m = p.matcher(message);
        String coordonnes = "";
        boolean b = m.matches();
        // si recherche fructueuse
        if (b) {
            // affichage de la chaîne capturée
            Log.d("TOAST", m.group());
            coordonnes = m.group();
        }
        return coordonnes;
    }

}
