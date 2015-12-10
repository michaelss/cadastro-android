package br.com.caelum.cadastro.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import br.com.caelum.cadastro.converter.AlunoConverter;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.support.WebClient;

/**
 * Created by android5843 on 10/12/15.
 */
public class EnviaAlunosTask extends AsyncTask<Object, Object, String> {

    private final Context context;
    private ProgressDialog progress;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... params) {
        String json = getAlunosJSON();
        WebClient client = new WebClient();
        return client.post(json);
    }

    private String getAlunosJSON() {
        AlunoDAO dao = new AlunoDAO(this.context);
        List<Aluno> alunos = dao.getLista();
        dao.close();

        return new AlunoConverter().toJson(alunos);
    }

    @Override
    protected void onPostExecute(String result) {
        this.progress.dismiss();

        try {
            JSONObject object = new JSONObject(result);
            Toast.makeText(this.context, "Média: " + object.getString("media") + " (" + object.getInt("quantidade") + " alunos)", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        this.progress = ProgressDialog.show(this.context, "Aguarde...", "Envio de dados para a Web.", true, false);
    }
}
