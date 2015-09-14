package br.com.mybaby;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Util {

	public static String getDataAtual(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()); 
		Date date = new Date(); 
		return dateFormat.format(date);
	}
	
	public static int getTempoPercorrido(Long tempoInicial){
		  
		Date dataInicial = new Date(tempoInicial);
		
		Calendar a = Calendar.getInstance();  
		a.setTime(new Date());//data maior  
		  
		Calendar b = Calendar.getInstance();  
		b.setTime(dataInicial);  
		  
		a.add(Calendar.DATE, - b.get(Calendar.DAY_OF_MONTH));  
		System.out.println(a.get(Calendar.DAY_OF_MONTH));  
		
		return a.get(Calendar.MINUTE);  
	}
	
	public static void main(String[] args) {
		Util.getTempoPercorrido(System.currentTimeMillis());
	}
}
