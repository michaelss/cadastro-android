package br.com.caelum.cadastro.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import br.com.caelum.cadastro.fragment.MapaFragment;

/**
 * Created by android5843 on 11/12/15.
 */
public class AtualizadorDeLocalizacao implements LocationListener {

    private GoogleApiClient client;
    private MapaFragment mapaFragment;

    public AtualizadorDeLocalizacao(Context context, MapaFragment mapaFragment) {

        Configurador config = new Configurador(this);
        this.client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(config)
                .build();

        this.client.connect();

        this.mapaFragment = mapaFragment;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng local = new LatLng(latitude, longitude);

        this.mapaFragment.centralizaNo(local);
    }

    public void inicia(LocationRequest req) {
        LocationServices.FusedLocationApi.requestLocationUpdates(client, req, this);
    }

    public void cancela() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        this.client.disconnect();
    }
}
