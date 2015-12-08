package br.com.caelum.cadastro;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.caelum.cadastro.modelo.Aluno;

/**
 * Created by android5843 on 08/12/15.
 */
public class FormularioHelper {

    private EditText nome;
    private EditText endereco;
    private EditText telefone;
    private EditText site;
    private RatingBar nota;

    private Aluno aluno;

    public FormularioHelper(FormularioActivity formularioActivity) {

        this.aluno = new Aluno();

        this.nome = (EditText) formularioActivity.findViewById(R.id.formulario_name);
        this.endereco = (EditText) formularioActivity.findViewById(R.id.formulario_address);
        this.telefone = (EditText) formularioActivity.findViewById(R.id.formulario_phone);
        this.site = (EditText) formularioActivity.findViewById(R.id.formulario_site);
        this.nota = (RatingBar) formularioActivity.findViewById(R.id.formulario_stars);
    }

    public Aluno pegaAlunoDoFormulario() {
        aluno.setNome(this.nome.getText().toString());
        aluno.setEndereco(this.endereco.getText().toString());
        aluno.setTelefone(this.telefone.getText().toString());
        aluno.setSite(this.site.getText().toString());
        aluno.setNota((double)this.nota.getProgress());

        return aluno;
    }

    public boolean isValido() {
        if (nome.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public void mostraErro() {
        nome.setError("Campo nome obrigatório");
    }
}
