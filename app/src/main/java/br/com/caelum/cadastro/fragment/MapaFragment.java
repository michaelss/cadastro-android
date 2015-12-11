package br.com.caelum.cadastro.fragment;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.location.AtualizadorDeLocalizacao;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.util.Localizador;

public class MapaFragment extends SupportMapFragment {
    GoogleMap map;

    @Override
    public void onResume() {
        super.onResume();

        map = getMap();
        Localizador localizador = new Localizador(getActivity());
        LatLng local = localizador.getCoordenada("Rua Vergueiro 3185 Vila Mariana");

        this.centralizaNo(local);

        AlunoDAO dao = new AlunoDAO(getActivity());
        List<Aluno> alunos = dao.getLista();
        dao.close();

        for (Aluno aluno : alunos) {
            MarkerOptions marcador = new MarkerOptions();
            marcador.position(localizador.getCoordenada(aluno.getEndereco())).title(aluno.getNome());
            Log.i("MAPA", "Endereço: " + aluno.getEndereco());
            map.addMarker(marcador);
        }

        Log.i("MAPA", "Coordenadas da Caelum: " + local);

        new AtualizadorDeLocalizacao(getActivity(), this);
    }

    public void centralizaNo(LatLng local) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 14));
        map.addMarker(new MarkerOptions().position(local));
    }
}
