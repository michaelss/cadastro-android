package br.com.caelum.cadastro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;


public class FormularioActivity extends ActionBarActivity {

    private FormularioHelper helper;
    private String localArquivoFoto;

    private static final int REQUEST_CODE_CAMERA = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        this.helper = new FormularioHelper(this);

        if (getIntent().hasExtra(ListaAlunosActivity.ALUNO_SELECIONADO)) {
            Aluno aluno = (Aluno) getIntent().getSerializableExtra(ListaAlunosActivity.ALUNO_SELECIONADO);

            this.helper.colocaNoFormulario(aluno);
        }

        Button foto = helper.getFotoButton();
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri localFoto = Uri.fromFile(new File(localArquivoFoto));
                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);

                startActivityForResult(irParaCamera, REQUEST_CODE_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                helper.carregaImagem(this.localArquivoFoto);
            }
            else {
                this.localArquivoFoto = null;
            }
        }
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
                if (this.helper.isValido()) {
                    Aluno aluno = this.helper.pegaAlunoDoFormulario();
                    AlunoDAO dao = new AlunoDAO(this);
                    if (aluno.getId() != null) {
                        dao.altera(aluno);
                    }
                    else {
                        dao.insere(aluno);
                    }
                    dao.close();
                    finish();
                }
                else {
                    this.helper.mostraErro();
                }
                return true;
            case R.id.formulario_menu_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
