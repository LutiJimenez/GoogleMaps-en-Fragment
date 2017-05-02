package lutistudio.seccion_09_maps.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import lutistudio.seccion_09_maps.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapActivity);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Nos acota el zoom
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(15);

        // Add a marker in Sydney and move the camera
        LatLng madrid = new LatLng(40.43807216375375, -3.6795366500000455);
        //le decimos que el marcador se puede arrastras con draggable
        mMap.addMarker(new MarkerOptions().position(madrid).title("Estamos en Madrid").draggable(true));

        //bearing orientacion de la camara hacie el este 90º max 365º
        //zoom maximo zoom es 21 lo normal es dejarlo en 15
        //tilt es para la inclinacion, efecto 3d, max 90º
        CameraPosition camera = new CameraPosition.Builder()
                .target(madrid)
                .zoom(10)
                .bearing(90)
                .tilt(30)
                .build();
        //necesitamos una camara animada para poder meter nuestra configuracion
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(madrid));

        //Eventos en el mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Click on: \n" +
                        "Lat: "+latLng.latitude + "\n"+
                        "Lon: "+latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Click Long on: \n" +
                        "Lat: "+latLng.latitude + "\n"+
                        "Lon: "+latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MapsActivity.this, "Marker dragged: \n" +
                        "Lat: "+ marker.getPosition().latitude+ "\n"+
                        "Lon: "+marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
