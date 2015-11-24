package br.com.mybaby.modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class Contato {

	private Integer id;
	private String nome;
	private List<Telefone> telefones = new ArrayList<Telefone>();
	private String email;
	private String tipo;
	
	public static final String CONTATO_TIPO_SMS = "SMS";
	public static final String CONTATO_TIPO_TELEFONE = "TEL";
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contato other = (Contato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	 public static void main(String[] args) {  
	        // Data inicial  
	        java.text.DateFormat dfo = new java.text.SimpleDateFormat("HH:mm:ss.SSS");  
	        Calendar dataInicio = Calendar.getInstance();  
	      
	        for (int i = 0; i < 20000; i++) {  
	            System.out.println("teste");  
	        }  
	      
	        // Data final  
	        Calendar dataFinal = Calendar.getInstance();  
	        //long df = System.currentTimeMillis();  
	        long diferenca = dataFinal.getTimeInMillis() - dataFinal.getTimeInMillis();  
	        long diferenca1 = dataFinal.getTimeInMillis() - dataInicio.getTimeInMillis();  
	        System.out.println("diferenca ---> " + diferenca);  
	        //formato UTC do TimeZone - resolveu  
	        dfo.setTimeZone(TimeZone.getTimeZone("UTC"));  
	      
	        System.out.println("Diferenca: " + dfo.format(diferenca));  
	        System.out.println("Diferenca: " + dfo.format(diferenca1));  
	    }  

}
