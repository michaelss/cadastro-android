package br.com.caelum.cadastro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.cadastro.ProvasActivity;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.modelo.Prova;

/**
 * Created by android5843 on 10/12/15.
 */
public class ListaProvasFragment extends Fragment {

    private ListView listViewProvas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutProvas = inflater.inflate(R.layout.fragment_lista_provas, container, false);

        this.listViewProvas = (ListView) layoutProvas.findViewById(R.id.lista_provas_listview);

        ArrayAdapter<Prova> adapter = new ArrayAdapter<Prova>(getActivity(),
                android.R.layout.simple_list_item_1, listaProvas());

        this.listViewProvas.setAdapter(adapter);

        this.listViewProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Prova selecionada = (Prova) adapter.getItemAtPosition(position);

                Toast.makeText(getActivity(), "Prova selecionada: " + selecionada, Toast.LENGTH_LONG).show();

                ProvasActivity calendarioProvas = (ProvasActivity) getActivity();
                calendarioProvas.selecionaProva(selecionada);
            }
        });

        return layoutProvas;
    }

    private List<Prova> listaProvas() {
        Prova p1 = new Prova("07/12/2015", "Matemática");
        p1.setTopicos(Arrays.asList("Álgebra Linear", "Cálculo", "Estatística"));

        Prova p2 = new Prova("10/12/2015", "Português");
        p2.setTopicos(Arrays.asList("Complemento nominal", "Orações subordinadas", "Análise sintática"));

        return Arrays.asList(p1, p2);
    }
}
