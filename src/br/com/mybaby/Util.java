package br.com.mybaby;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

	public static String getDataAtual(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()); 
		Date date = new Date(); 
		return dateFormat.format(date);
	}
}
