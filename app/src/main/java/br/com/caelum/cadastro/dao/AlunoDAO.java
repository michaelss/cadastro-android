package br.com.caelum.cadastro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.cadastro.modelo.Aluno;

/**
 * Created by android5843 on 08/12/15.
 */
public class AlunoDAO extends SQLiteOpenHelper {

    private final static int VERSAO = 2;
    private final static String TABELA = "Alunos";
    private final static String NOME_BD = "CadastroCaelum";

    public AlunoDAO(Context context) {
        super(context, NOME_BD, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + " (id INTEGER PRIMARY KEY, " +
                " nome TEXT NOT NULL," +
                " telefone TEXT," +
                " endereco TEXT," +
                " site TEXT," +
                " nota REAL," +
                " caminhoFoto TEXT);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, final int oldVersion, final int newVersion) {
        String sql = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto text;";
        switch(oldVersion) {
            case 1:
                db.execSQL(sql);
        }
    }

    public void insere(Aluno aluno) {
        ContentValues values = getContentValues(aluno);

        getWritableDatabase().insert(TABELA, null, values);
    }

    public void altera(Aluno aluno) {
        ContentValues values = getContentValues(aluno);

        Log.i("AlunoDAO", "nota: " + aluno.getNota());

        String[] id = {aluno.getId().toString()};

        getWritableDatabase().update(TABELA, values, "id = ?", id);
    }

    private ContentValues getContentValues(Aluno aluno) {
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("endereco", aluno.getEndereco());
        values.put("telefone", aluno.getTelefone());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());
        return values;
    }

    public List<Aluno> getLista() {
        List<Aluno> alunos = new ArrayList<>();

        // A diferença entre getReadableDatabase() e getWritableDatabase() é que o último é mais lento,
        // e usado para operações de escrita. Quando fizer leitura, usar o getReadableDatabase().
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

            while (cursor.moveToNext()) {
                Aluno aluno = new Aluno();

                aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
                aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
                aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
                aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
                aluno.setNota(cursor.getDouble(
                        cursor.getColumnIndex("nota")));
                aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

                alunos.add(aluno);
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return alunos;
    }

    public void delete(Aluno aluno) {
        String[] id = {aluno.getId().toString()};
        getWritableDatabase().delete(TABELA, "id = ?", id);
    }
}
