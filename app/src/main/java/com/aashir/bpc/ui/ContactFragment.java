package com.aashir.bpc.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        TextView address = (TextView) rootView.findViewById(R.id.location_address);
        TextView phone = (TextView) rootView.findViewById(R.id.contact_phone);
        TextView email = (TextView) rootView.findViewById(R.id.contact_email);

        address.setText("School, Sector 53, Islamabad");
        phone.setText("051-0000000");
        email.setText("email@school.com");

        phone.setOnClickListener(this);
        email.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ViewStub) view.findViewById(R.id.map_stub)).inflate();
                setUpMap();
            }
        }, 250);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMap != null) mMap.clear();
    }

    private void setUpMap() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        /*if (mMap != null) {
            LatLng lat = new LatLng(33.54025, 73.114503);
            Marker marker = mMap.addMarker(new MarkerOptions().position(lat).title("School"));
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_phone:
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + "0510039109"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    if (isAdded())
                        Toast.makeText(getActivity(), "No Phone app found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.contact_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "email@school.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send using"));
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng lat = new LatLng(33.54025, 73.114503);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(lat).title("School"));
        marker.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 15));
    }
}