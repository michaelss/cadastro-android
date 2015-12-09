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
        this.aluno.setNome(this.nome.getText().toString());
        this.aluno.setEndereco(this.endereco.getText().toString());
        this.aluno.setTelefone(this.telefone.getText().toString());
        this.aluno.setSite(this.site.getText().toString());
        this.aluno.setNota((double)this.nota.getProgress());

        return this.aluno;
    }

    public void colocaNoFormulario(Aluno aluno) {
        this.aluno = aluno;

        this.nome.setText(this.aluno.getNome());
        this.endereco.setText(this.aluno.getEndereco());
        this.telefone.setText(this.aluno.getTelefone());
        this.site.setText(this.aluno.getSite());
        this.nota.setProgress((this.aluno.getNota() != null) ? this.aluno.getNota().intValue() : 0);
    }

    public boolean isValido() {
        if (this.nome.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public void mostraErro() {
        this.nome.setError("Campo nome obrigatório");
    }
}
