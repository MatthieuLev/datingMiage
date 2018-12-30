package fr.miage.app.meetingmiage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

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
        message = message.replace("MeetingMiage", phoneNumber);
        String latlng = regexCoordinate();
        lat = latlng.split(",")[0];
        lng = latlng.split(",")[1];

        askingDialog(savedInstanceState);

    }

    private void askingDialog(Bundle savedInstanceState) {
        TextView tvMaps= findViewById(R.id.tvMaps);
        tvMaps.setText(message);

        MapView mMapView = findViewById(R.id.mapView);
        MapsInitializer.initialize(this);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                LatLng pos = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(pos).title("Meeting"));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                LatLng coordinate = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 11.0f);
                googleMap.animateCamera(yourLocation);
            }
        });;
    }

    public void acceptMeeting(View view){
        SmsSender.sendSMS(this, phoneNumber, "MeetingMiage : your contact accept your invitation");
    }
    public void refuseMeeting(View view){
        SmsSender.sendSMS(this, phoneNumber, "MeetingMiage : your contact refuse your invitation");
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
}
