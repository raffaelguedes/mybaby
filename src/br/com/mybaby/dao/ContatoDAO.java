package br.com.mybaby.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.mybaby.modelo.Contato;
import br.com.mybaby.modelo.Telefone;

public class ContatoDAO extends DAOHelper {

	private final static String TAG = ContatoDAO.class.getSimpleName();
	
	TelefoneDAO telefoneDAO;

	public ContatoDAO(Context context) {
		super(context);
		telefoneDAO = new TelefoneDAO(context);
	}
	
	public void inserir(Contato contato){
      long id = getWritableDatabase().insert(TABELA_CONTATO, null, toValues(contato));
      
      if(id != -1){
    	  telefoneDAO.deletar(contato.getId());
    	  for(Telefone telefone : contato.getTelefones()){
    		  telefone.setContatoId(contato.getId());
    		  telefoneDAO.inserir(telefone);
    	  }
      }
       
	}
	
	private ContentValues toValues(Contato contato){
		ContentValues values = new ContentValues();

		values.put("id", contato.getId());
        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        values.put("tipo", contato.getTipo());
        
        return values;
	}
	
	public List<Contato> getContatos(){
		Cursor cursor = null;
		try{
			String sql = "SELECT * FROM " + TABELA_CONTATO +" ;";
			cursor = getReadableDatabase().rawQuery(sql, null);

			List<Contato> contatos = new ArrayList<Contato>();

			while(cursor.moveToNext()){
				Contato contato = new Contato();
				contato.setId(cursor.getInt(cursor.getColumnIndex("id")));
				contato.setNome(cursor.getString(cursor.getColumnIndex("nome")));
				contato.setEmail(cursor.getString(cursor.getColumnIndex("email")));
				contato.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));

				contato.setTelefones(telefoneDAO.getTelefones(contato));

				contatos.add(contato);
			}
			return contatos;
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}
	
	public List<Contato> getContatos(String tipo){
		Cursor cursor = null;
		try{
			String[] args = {tipo};

			String[] colunas ={"id", "nome","email", "tipo"};

			cursor = getReadableDatabase().query(TABELA_CONTATO , colunas, "tipo=?", args, null, null, null);

			List<Contato> contatos = new ArrayList<Contato>();

			while(cursor.moveToNext()){
				Contato contato = new Contato();
				contato.setId((int) cursor.getInt(cursor.getColumnIndex("id")));
				contato.setNome(cursor.getString(cursor.getColumnIndex("nome")));
				contato.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
				contato.setEmail(cursor.getString(cursor.getColumnIndex("email")));

				contato.setTelefones(telefoneDAO.getTelefones(contato));

				contatos.add(contato);
			}
			return contatos;
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}
	
	public void update(Contato contato){
		ContentValues values = toValues(contato);		
		
		String[] args = {contato.getId().toString()};
		
		int rows = getWritableDatabase().update(TABELA_CONTATO, values, "id=?", args);
		
		if(rows > 0){
	    	  telefoneDAO.deletar(contato.getId());
	    	  for(Telefone telefone : contato.getTelefones()){
	    		  telefone.setContatoId(contato.getId());
	    		  telefoneDAO.inserir(telefone);
	    	  }
	      }
		
	}
	
	public void delete(Contato contato){
		String[] args = {contato.getId().toString()};
		getWritableDatabase().delete(TABELA_CONTATO, "id=?", args);
	}
	
	
}
