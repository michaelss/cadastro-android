package br.com.caelum.cadastro;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;


public class FormularioActivity extends ActionBarActivity {

    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);

//        Button salvar = (Button) findViewById(R.id.formulario_botao);
//        salvar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.formulario_menu_ok:
                if (helper.isValido()) {
//                    String nome = helper.pegaAlunoDoFormulario().getNome();
                    Aluno aluno = helper.pegaAlunoDoFormulario();
                    AlunoDAO dao = new AlunoDAO(this);
                    dao.insere(aluno);
                    dao.close();
                    finish();
                }
                else {
                    helper.mostraErro();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
