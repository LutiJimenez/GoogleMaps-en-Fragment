package lutistudio.seccion_09_maps.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import lutistudio.seccion_09_maps.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener {

    private View rootView;
    private GoogleMap gMap;
    private MapView mapView;

    private List<Address> addresses;
    //encargado de recoger la informacion
    private Geocoder geocoder;

    private MarkerOptions marcador;

    private FloatingActionButton fab;

    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        fab = (FloatingActionButton) rootView.findViewById(R.id.FABconfigGPS);

        //Activamos el GPS si damos al FAB
        fab.setOnClickListener(this);

        return rootView;
    }

    //Aqui es cuando se crea la vista, el fragmento está cargado
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        mapView = (MapView) rootView.findViewById(R.id.map);
        if(mapView != null){
            //Esto para inicializar el mapa implementamos OnMapReadyCallback para que ya sea como
            //en el activity
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //ponerlo aqui me hace un loop
        //this.checkIfGPSIsEnable();
    }

    //En este metodo es donde vamos a configurar el maps
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng madrid = new LatLng(40.43807216375375, -3.6795366500000455);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);


        //le decimos que el marcador se puede arrastras con draggable
        //Vamos a customizar el marcador
        marcador = new MarkerOptions();
        marcador.position(madrid);
        marcador.title("Mi marcador");
        marcador.draggable(true);
        marcador.snippet("Esto es la info del marcador");
        //marcador.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on));

        //gMap.addMarker(new MarkerOptions().position(madrid).title("Estamos en Madrid").draggable(true));
        gMap.addMarker(marcador);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(madrid));
        //CameraPosition camera = new CameraPosition.Builder()
        //        .target(madrid)
        //        .zoom(10)
        //        .bearing(90)
        //        .tilt(30)
        //        .build();
        //necesitamos una camara animada para poder meter nuestra configuracion
        //gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        gMap.animateCamera(zoom);

        //vamos a recoger la informacion
        gMap.setOnMarkerDragListener(this);

        geocoder = new Geocoder(getContext(), Locale.getDefault());


    }

    private void checkIfGPSIsEnable(){
        //Vamos a pedir el GPS
        try {
            // si este int es cero es que no tenemos señal
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            if(gpsSignal == 0 ) {
                //el gps no esta activado
                showInfoAlert();

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
    //cuadro de dialogo para preguntar si quieres activar el GPS
    private void showInfoAlert(){
        new AlertDialog.Builder(getContext()).
                setTitle("GPS Signal")
                .setMessage("You don´t have GPS Signal enabled. Would yuo like to enabled?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //se lanza cuando agarramos el marcador este evento
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //este evento se lanza cada vez que vamos moviendonos

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

        //vamos a coger la info de marcador movido, el 1 es para que tenga una localazacion

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();

        //Se lo añadimos al snipet del marcador
        marker.setSnippet("Address: " + address + "\n"+
                "city: " + city + "\n"+
                "state: " + state + "\n"+
                "country: " + country + "\n"+
                "postal code: " + postalCode);

        marker.showInfoWindow();


        //Recuperamos la dir, pais, codigo postal.
/*        Toast.makeText(getContext(), "Address: " + address + "\n"+
                        "city: " + city + "\n"+
                        "state: " + state + "\n"+
                        "country: " + country + "\n"+
                        "postal code: " + postalCode
                , Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onClick(View v) {
        this.checkIfGPSIsEnable();
    }
}
