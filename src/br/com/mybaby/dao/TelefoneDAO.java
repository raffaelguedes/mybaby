package br.com.mybaby.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.mybaby.modelo.Contato;
import br.com.mybaby.modelo.Telefone;

public class TelefoneDAO extends DAOHelper {
	private final static String TAG = TelefoneDAO.class.getSimpleName();

	public TelefoneDAO(Context context) {
		super(context);
	}
	
	public void inserir(Telefone telefone){
        getWritableDatabase().insert(TABELA_TELEFONE, null, toValues(telefone));
	}
	
	
	private ContentValues toValues(Telefone telefone){
		ContentValues values = new ContentValues();

        values.put("id", telefone.getId());
        values.put("ddd", telefone.getDdd());
        values.put("numero", telefone.getNumero());
        values.put("contato", telefone.getContatoId());
        
        return values;
	}
	
	public List<Telefone> getTelefones(Contato contato){
		Cursor cursor = null;
		try{
			String[] args = {contato.getId().toString()};

			String[] colunas ={"id", "ddd","numero", "contato"};

			cursor =  getReadableDatabase().query(TABELA_TELEFONE , colunas, "contato=?", args, null, null, null);

			List<Telefone> telefones = new ArrayList<Telefone>();

			while(cursor.moveToNext()){
				Telefone telefone = new Telefone();
				telefone.setId((int) cursor.getInt(cursor.getColumnIndex("id")));
				telefone.setDdd(cursor.getString(cursor.getColumnIndex("ddd")));
				telefone.setNumero(cursor.getString(cursor.getColumnIndex("numero")));
				telefone.setContatoId((int)cursor.getInt(cursor.getColumnIndex("contato")));

				telefones.add(telefone);
			}
			return telefones;
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}
	
	public List<Telefone> getTelefones(){
		Cursor cursor = null;
		try{
			String sql = "SELECT * FROM " + TABELA_TELEFONE +" ;";
			cursor = getReadableDatabase().rawQuery(sql, null);

			List<Telefone> telefones = new ArrayList<Telefone>();

			while(cursor.moveToNext()){
				Telefone telefone = new Telefone();
				telefone.setId((int) cursor.getInt(cursor.getColumnIndex("id")));
				telefone.setDdd(cursor.getString(cursor.getColumnIndex("ddd")));
				telefone.setNumero(cursor.getString(cursor.getColumnIndex("numero")));
				telefone.setContatoId((int)cursor.getInt(cursor.getColumnIndex("contato")));		

				telefones.add(telefone);
			}

			return telefones;
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
	}
	
	public void update(Telefone telefone){
		ContentValues values = toValues(telefone);		
		
		String[] args = {telefone.getId().toString()};
		
		getWritableDatabase().update(TABELA_TELEFONE, values, "id=?", args);
	}
	
	public void deletar(Integer contatoID){
		String[] args = {contatoID.toString()};
        getWritableDatabase().delete(TABELA_TELEFONE, "contato=?", args);
	}

}
