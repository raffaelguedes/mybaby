package br.com.mybaby.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.mybaby.modelo.Log;

public class LogDAO extends DAOHelper {

	public LogDAO(Context context) {
		super(context);
	}
	
	public void inserir(Log log){
        getWritableDatabase().insert(TABELA_LOG, null, toValues(log));
	}
	
	public List<Log> getLogs(){
		String sql = "SELECT * FROM " + TABELA_LOG +" ;";
		
		List<Log> logs = new ArrayList<Log>();
		
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			Log log = new Log();
			log.setData(cursor.getString(cursor.getColumnIndex("data")));
			log.setStatus(cursor.getString(cursor.getColumnIndex("status")));
			log.setObservacao(cursor.getString(cursor.getColumnIndex("observacao")));
			
			logs.add(log);
			
		}
		
		return logs;
	}
	
	public void zerarLogs(){
		getWritableDatabase().delete(TABELA_LOG, null, null);
	}
	
	private ContentValues toValues(Log log){
		ContentValues values = new ContentValues();

        values.put("data", log.getData());
        values.put("status", log.getStatus());
        values.put("observacao", log.getObservacao());
        
        return values;
	}


	
}
