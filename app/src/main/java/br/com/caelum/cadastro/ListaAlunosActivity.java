package br.com.caelum.cadastro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;


public class ListaAlunosActivity extends ActionBarActivity {

    public static final String ALUNO_SELECIONADO = "alunoSelecionado";

    private ListView listaAlunos;
    private List<Aluno> alunos;

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        alunos = dao.getLista();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        final Context ctx = this;

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = (Aluno) parent.getItemAtPosition(position);

                Intent edicao = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                edicao.putExtra(ALUNO_SELECIONADO, aluno);
                startActivity(edicao);
            }
        });

        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListaAlunosActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        Button botaoAdiciona = (Button) findViewById(R.id.lista_alunos_floating_button);
        botaoAdiciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent novo = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(novo);
            }
        });

        registerForContextMenu(listaAlunos);
    }

    protected void onResume() {
        super.onResume();
        this.carregaLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        final Aluno alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(position);

        menu.add("Ligar");
        menu.add("Enviar SMS");
        menu.add("Achar no mapa");
        menu.add("Navegar no site");
        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ListaAlunosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar?")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                        try {
                                            dao.delete(alunoSelecionado);
                                        } finally {
                                            dao.close();
                                        }
                                        carregaLista();
                                    }
                                })
                        .setNegativeButton("Não", null)
                        .show();

                        return false;
                    }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
