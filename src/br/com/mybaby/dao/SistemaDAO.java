package br.com.mybaby.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mybaby.Constantes;

public class SistemaDAO extends SQLiteOpenHelper {

	private static final int VERSAO = 1;
	private static final String DATABASE = "Mybaby";
	private static final String TABELA = "sistema";

	public SistemaDAO(Context context) {
		super(context, DATABASE, null, VERSAO);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String ddl = "CREATE TABLE " + TABELA + " (chave TEXT PRIMARY KEY NOT NULL, valor TEXT);";
		database.execSQL(ddl);
		database.execSQL("INSERT INTO " + TABELA + " (chave, valor) values('" + Constantes.NOTIFICACAO_ENVIO + "', 'true');");
		database.execSQL("INSERT INTO " + TABELA + " (chave, valor) values('" + Constantes.NOTIFICACAO_ATIVO + "', 'false');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		if(oldVersion != newVersion){
			String sql = "DROP TABLE IF EXISTS " + TABELA;
			database.execSQL(sql);
			onCreate(database);
		}
	}
	
	public void inserir(String chave, String valor){
		ContentValues values = new ContentValues();
		values.put("chave", chave);
		values.put("valor", valor);
		
        getWritableDatabase().insert(TABELA, null, values);
	}
	
	public void update(String chave, String valor){
		ContentValues values = new ContentValues();
		values.put("valor", valor);
		
		String[] args = {chave};
		getWritableDatabase().update(TABELA, values, "chave=?", args);
	}
	
	public String getValor(String chave){
		String[] args = {chave};
		String sql = "SELECT * FROM " + TABELA +" where chave=?;";
		
		Cursor cursor = getReadableDatabase().rawQuery(sql, args);
		
		String retorno = null;
		while(cursor.moveToNext()){
			retorno = cursor.getString(cursor.getColumnIndex("valor"));
			
		}
		return retorno;
	}

}
