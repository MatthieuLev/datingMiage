package com.datingmiage.app.datingmiage.controller;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsSender {

    // send SMS se déclanche lorsque l'on click sur le bouton d'envoi des coordonnées
    public  static String sendSMSForRDV(Context context, String numero) {
        String message = "";
        // compilation de la regex du numero de telephone français
        Pattern p = Pattern.compile("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$");
        // création d'un moteur de recherche
        Matcher m = p.matcher(numero);
        // lancement de la recherche de toutes les occurrences
        boolean b = m.matches();
        // si recherche fructueuse
        if (!b) {
            Toast.makeText(context, "Number " + numero + " is not valide " + numero, Toast.LENGTH_SHORT).show();
            message = "Number " + numero + " is not valide " + numero;
        }

        GPSTracker gps = new GPSTracker(context);

        // Check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            message = "Rendez-vous proposé coordonnées " + latitude + "," + longitude;
            SmsManager.getDefault().sendTextMessage(numero, null, latitude + "," + longitude, null, null);
            return message;
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
        return message;
    }
}
