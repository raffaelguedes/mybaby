package br.com.mybaby.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mybaby.util.Constantes;

public class DAOHelper extends SQLiteOpenHelper {
	
	private final static String TAG = DAOHelper.class.getSimpleName();
	private static final int VERSAO = 1;
	private static final String DATABASE = "Mybaby";
	public static final String TABELA_SISTEMA = "sistema";
	public static final String TABELA_LOG = "log";
	public static final String TABELA_CONTATO = "contato";
	public static final String TABELA_TELEFONE = "telefone";
	
	private static final String CREATE_TABLE_SISTEMA = "CREATE TABLE " + TABELA_SISTEMA + " (chave TEXT PRIMARY KEY NOT NULL, valor TEXT, data TEXT);";
	private static final String CREATE_TABLE_LOG = "CREATE TABLE " + TABELA_LOG + " (_id INTEGER PRIMARY KEY, data TEXT, status TEXT, observacao TEXT);";
	private static final String CREATE_TABLE_CONTATO = "CREATE TABLE " + TABELA_CONTATO + " (id INTEGER, nome TEXT, email TEXT, tipo TEXT);";
	private static final String CREATE_TABLE_TELEFONE = "CREATE TABLE " + TABELA_TELEFONE + " (id INTEGER, ddd TEXT, numero TEXT, contato INTEGER);";
	
	public DAOHelper(Context context) {
		super(context, DATABASE, null, VERSAO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_TABLE_SISTEMA);
		db.execSQL("INSERT INTO " + TABELA_SISTEMA + " (chave, valor, data) values('" + Constantes.KEEP_ENVIO_ALERTA + "', 'true', '');");
		db.execSQL("INSERT INTO " + TABELA_SISTEMA + " (chave, valor, data) values('" + Constantes.NOTIFICACAO_ATIVO + "', 'false', '');");
		db.execSQL("INSERT INTO " + TABELA_SISTEMA + " (chave, valor, data) values('" + Constantes.SMS_ENVIADO + "', 'false', '');");
		db.execSQL("INSERT INTO " + TABELA_SISTEMA + " (chave, valor, data) values('" + Constantes.SMS_ENTREGUE + "', 'false', '');");
		db.execSQL("INSERT INTO " + TABELA_SISTEMA + " (chave, valor, data) values('" + Constantes.DISPOSITIVO_CONECTADO_SINCRONIZADO + "', 'false', '');");
		db.execSQL("INSERT INTO " + TABELA_SISTEMA + " (chave, valor, data) values('" + Constantes.SISTEMA_OK + "', 'false', '');");
		
		
		db.execSQL(CREATE_TABLE_LOG);
		db.execSQL(CREATE_TABLE_CONTATO);
		db.execSQL(CREATE_TABLE_TELEFONE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
