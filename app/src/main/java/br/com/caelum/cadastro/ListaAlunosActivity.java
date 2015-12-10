package br.com.caelum.cadastro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import br.com.caelum.cadastro.adapter.ListaAlunosAdapter;
import br.com.caelum.cadastro.converter.AlunoConverter;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.support.WebClient;


public class ListaAlunosActivity extends ActionBarActivity {

    public static final String ALUNO_SELECIONADO = "alunoSelecionado";

    private ListView listaAlunos;
    private List<Aluno> alunos;

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        alunos = dao.getLista();
        dao.close();

        ListaAlunosAdapter adapter = new ListaAlunosAdapter(this, alunos);
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

        MenuItem ligar = menu.add("Ligar");
        MenuItem sms = menu.add("Enviar SMS");
        MenuItem mapa = menu.add("Achar no mapa");
        MenuItem navegar = menu.add("Navegar no site");
        MenuItem deletar = menu.add("Deletar");

        ligar(ligar, alunoSelecionado);
        enviarSMS(sms, alunoSelecionado);
        abrirMapa(mapa, alunoSelecionado);
        navegar(navegar, alunoSelecionado);
        deletar(deletar, alunoSelecionado);
    }

    private void ligar(MenuItem menuItem, Aluno aluno) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + aluno.getTelefone()));
        menuItem.setIntent(intent);
    }

    private void enviarSMS(MenuItem menuItem, Aluno aluno) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + aluno.getTelefone()));
        menuItem.setIntent(intent);
    }

    private void abrirMapa(MenuItem menuItem, Aluno aluno) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?z=14&q=:" + aluno.getEndereco()));
        menuItem.setIntent(intent);
    }

    private void navegar(MenuItem menuItem, Aluno aluno) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String site = aluno.getSite();

        if (!site.startsWith("http://")) {
            site = "http://" + site;
        }
        intent.setData(Uri.parse(site));
        menuItem.setIntent(intent);
    }

    private void deletar(MenuItem menuItem, final Aluno alunoSelecionado) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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

        switch (id) {
            case  R.id.menu_enviar_notas:
                AlunoDAO dao = new AlunoDAO(this);
                List<Aluno> alunos = dao.getLista();
                dao.close();

                String json = new AlunoConverter().toJson(alunos);
                WebClient client = new WebClient();
                String resposta = client.post(json);
                Toast.makeText(this, resposta, Toast.LENGTH_LONG).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
