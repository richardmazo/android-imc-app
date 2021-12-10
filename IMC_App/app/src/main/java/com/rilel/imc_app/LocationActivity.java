package com.rilel.imc_app;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilel.imc_app.model.Ubicacion;
import com.rilel.imc_app.model.Usuario;
import com.rilel.imc_app.repository.LocationFirebase;
import com.rilel.imc_app.repository.LocationFirebaseImpl;
import com.rilel.imc_app.utils.Callback;
import com.rilel.imc_app.utils.GpsUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    LocationFirebase locationFirebase;
    Usuario usuario;

    private static  final String[] LOCATION_PERMS ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int LOCATION_REQUEST = 1337;
    Marker marker;

    final static String COLLECTION = "ubicacion";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Logs Firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Bundle bundle = this.getIntent().getExtras();
        Gson gson = new Gson();
        String Usuario = bundle.getString("Usuario");
        Type typeUsuario = new TypeToken<com.rilel.imc_app.model.Usuario>(){}.getType();
        usuario = gson.fromJson(Usuario, typeUsuario);

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

        }else{

            consultarUbicaciones();
            iniciarMapa();

        }
    }

    private void consultarUbicaciones() {

        locationFirebase = new LocationFirebaseImpl();

        Callback callbackConsultarUbicacion = new Callback() {
            @Override
            public void OnSuccess(Object object) {
                Log.d("Logs de callbackConsultarUbicacion", "Respuesta con callbackConsultarUbicacion, ");
            }

            @Override
            public void OnFailure(Object object) {
                Log.d("Logs de callbackConsultarUbicacion", "Respuesta con callbackConsultarUbicacion, FALLO");
            }
        };


        /*List<DocumentSnapshot>[] todasLasubicaciones = locationFirebase.leerTodos(callbackConsultarUbicacion);
        System.out.println("Holaaa "+todasLasubicaciones);

        for (List<DocumentSnapshot> nombre: todasLasubicaciones) {
            System.out.println("Holi "+nombre);
        }

        locationFirebase.leerPorId("1-20:41:53",callbackConsultarUbicacion);

        List<Type> type = locationFirebase.getListItems();
        System.out.println("Holaaa "+type);*/

        obtenerTodo2(callbackConsultarUbicacion);


    }

    private void obtenerTodo() {
        db.collection(COLLECTION).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            //asignar(documentSnapshots);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public /*List<DocumentSnapshot>[]*/ void obtenerTodo2(Callback callback) {
        db.collection(COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"signInWithCredential:success");
                            callback.OnSuccess(task.getResult().getDocuments());
                            asignar(task);
                        }else{
                            Log.w(TAG,"signInWithCredential:failure",task.getException());
                            Log.d("Logs de leerTodos", "Respuesta con leerTodos, " + task.getException());
                            callback.OnFailure(null);
                        }
                    }
                });
    }

    private void asignar(Task<QuerySnapshot> task) {
        List<DocumentSnapshot> ubicaciones = task.getResult().getDocuments();

        for(int i = 0; i < ubicaciones.size(); i++){
            Map<String, Object> resultado1 = ubicaciones.get(i).getData();
            Double latitud = null;
            Double longitud = null;
            String idUsario = null;
            for (Map.Entry<String, Object> entry : resultado1.entrySet()) {
                if(entry.getKey().equals("idUsuario")){
                    idUsario = entry.getValue().toString();
                }
                if(entry.getKey().equals("latitud")){
                    latitud = (Double) entry.getValue();
                }
                if(entry.getKey().equals("longitud")){
                    longitud = (Double) entry.getValue();
                }
                System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
            }
            String usuarioId = usuario.getId().toString();
            if(usuarioId.equals(idUsario)){
                LatLng punto = new LatLng(latitud, longitud);
                marker = mMap.addMarker(new MarkerOptions().position(punto).title("Posicion"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
            }
        }
    }

    private void iniciarMapa() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        (new GpsUtils(LocationActivity.this)).turnGPSOn(new GpsUtils.onGpsListener(){
            @Override
            public void gpsStatus(boolean isGPSEnable){

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            return;

        }

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setMinZoomPreference(17f);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                iniciarMapa();
            }
        }
    }


}