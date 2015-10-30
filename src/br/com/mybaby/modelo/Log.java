package br.com.mybaby.modelo;

public class Log {
	
	private Long id;
	private String data;
	private String status;
	private String observacao;
	
	public Log(String data, String status, String observacao){
		this.data = data;
		this.status = status;
		this.observacao = observacao;
	}
	
	public Log(){
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
