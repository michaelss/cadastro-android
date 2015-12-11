package br.com.caelum.cadastro.location;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by android5843 on 11/12/15.
 */
public class Configurador implements GoogleApiClient.ConnectionCallbacks {

    private AtualizadorDeLocalizacao atualizador;

    public Configurador(AtualizadorDeLocalizacao atualizador) {
        this.atualizador = atualizador;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest req = LocationRequest.create();
        req.setInterval(2000);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        req.setSmallestDisplacement(50);

        atualizador.inicia(req);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
