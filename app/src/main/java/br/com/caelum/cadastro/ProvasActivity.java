package br.com.caelum.cadastro;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import br.com.caelum.cadastro.fragment.DetalhesProvaFragment;
import br.com.caelum.cadastro.fragment.ListaProvasFragment;
import br.com.caelum.cadastro.modelo.Prova;

/**
 * Created by android5843 on 10/12/15.
 */
public class ProvasActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isTablet()) {
            transaction.replace(R.id.provas_lista, new ListaProvasFragment());
            transaction.replace(R.id.provas_detalhes, new DetalhesProvaFragment());
        }
        else {
            transaction.replace(R.id.provas_view, new ListaProvasFragment());
        }
        transaction.commit();

    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void selecionaProva(Prova prova) {
        FragmentManager manager = getSupportFragmentManager();

        if (isTablet()) {
            DetalhesProvaFragment detalhesProva = (DetalhesProvaFragment) manager.findFragmentById(R.id.provas_detalhes);
            detalhesProva.populaCamposComDados(prova);
        }
        else {
            Bundle argumentos = new Bundle();
            argumentos.putSerializable("prova", prova);

            DetalhesProvaFragment detalhesProva = new DetalhesProvaFragment();
            detalhesProva.setArguments(argumentos);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.provas_view, detalhesProva);

            // Passar null indica que é voltar para o fragment anterior.
            transaction.addToBackStack(null);

            transaction.commit();
        }

    }
}
