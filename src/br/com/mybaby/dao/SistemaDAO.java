package br.com.mybaby.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.mybaby.util.Util;

public class SistemaDAO extends DAOHelper {

	private final static String TAG = SistemaDAO.class.getSimpleName();

	public SistemaDAO(Context context) {
		super(context);
	}
	
	public void inserir(String chave, String valor){
		ContentValues values = new ContentValues();
		values.put("chave", chave);
		values.put("valor", valor);
		values.put("data", Util.getDataAtual());
		
        getWritableDatabase().insert(TABELA_SISTEMA, null, values);
        close();
	}
	
	public void update(String chave, String valor){
		Log.i(TAG, "VALOR ANTERIOR - CHAVE: " +chave+ " VALOR: " +getValor(chave));
		Log.i(TAG, "UPDATE - CHAVE: " +chave+ " VALOR: " +valor);
		ContentValues values = new ContentValues();
		values.put("valor", valor);
		values.put("data", Util.getDataAtual());
		
		String[] args = {chave};
		getWritableDatabase().update(TABELA_SISTEMA, values, "chave=?", args);
		close();
	}
	
	public String getValor(String chave){
		Cursor cursor = null;
		try{
			String[] args = {chave};
			String sql = "SELECT * FROM " + TABELA_SISTEMA +" where chave=?;";

			cursor = getReadableDatabase().rawQuery(sql, args);

			String retorno = null;
			while(cursor.moveToNext()){
				retorno = cursor.getString(cursor.getColumnIndex("valor"));

			}
			return retorno;
		}finally{
			if(cursor!=null){
				cursor.close();
			}
			close();
		}
	}

}
