package fr.miage.app.meetingmiage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceptionActivity extends AppCompatActivity {
    String phoneNumber, message, lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        // Get the transferred data from source activity.
        Intent intent = getIntent();
        phoneNumber = intent.getExtras().getString("phoneNumber");
        message = intent.getExtras().getString("message");
        String latlng = regexCoordinate();
        lat = latlng.split(",")[0];
        lng = latlng.split(",")[1];

        askingDialog();

    }

    private void askingDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("SUGGESTED MEETING DATE");
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_map, null);
        alertDialog.setView(promptView);

        MapView mMapView = (MapView) promptView.findViewById(R.id.mapView);
        MapsInitializer.initialize(this);

        mMapView.onCreate(alertDialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng pos = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(pos).title("Meeting"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            }
        });

        final Context self_ = this;
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsSender.sendSMS(self_, phoneNumber, "");
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "REFUSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    private String regexCoordinate(){
        String ret ="";
        Pattern coordinates = Pattern.compile("(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)");
        Matcher m = coordinates.matcher(message);
        if (m.find()) {
            ret =  m.group();
        }
        return ret;
    }
    private String regexPhoneNumber(){
    return "a faire";
    }
}
