package br.com.mybaby.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.com.mybaby.util.Constantes;

public class EmailService extends Authenticator {
	private String mailhost = "smtp.gmail.com";   
	private Session session;   
	private String assunto;
	private String corpo;
	private String remetente;
	private String destinatario;
	private SharedPreferences sharedPref;

	static {   
		Security.addProvider(new JSSEProvider());   
	}  

	public EmailService(final String user, final String password, Context context) {   

		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		preencheDadosEmail();
		
		Properties props = new Properties();   
		props.setProperty("mail.transport.protocol", "smtp");   
		props.setProperty("mail.host", mailhost);   
		props.put("mail.smtp.auth", "true");   
		props.put("mail.smtp.port", "465");   
		props.put("mail.smtp.socketFactory.port", "465");   
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
		props.put("mail.smtp.socketFactory.fallback", "false");   
		props.setProperty("mail.smtp.quitwait", "false");   

		session = Session.getInstance(props, new javax.mail.Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(user, password);
		    }
		});
		
	}   


	private void preencheDadosEmail() {
		this.assunto = "Alerta MyBaby!";
		this.corpo = "MyBaby! Mensagem automática de emergência! Entre em contato!";
		this.remetente = "MyBaby@MyBaby.com.br";

		destinatario = sharedPref.getString(Constantes.EMAIL_PRINCIPAL, "");

		if(sharedPref.getString(Constantes.EMAIL_SECUNDARIO, "")!=null){
			destinatario += ", " + sharedPref.getString(Constantes.EMAIL_SECUNDARIO, "");
		}
	}


	public synchronized void sendMail() throws Exception {   
		try{
			MimeMessage message = new MimeMessage(session);   
			DataHandler handler = new DataHandler(new ByteArrayDataSource(corpo.getBytes(), "text/plain"));   
			message.setSender(new InternetAddress(remetente));   
			message.setSubject(assunto);   
			message.setDataHandler(handler);   
			if (destinatario.indexOf(',') > 0)   
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));   
			else  
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   
			Transport.send(message);   
		}catch(Exception e){
			e.printStackTrace();
		}
	}   

	public class ByteArrayDataSource implements DataSource {   
		private byte[] data;   
		private String type;   

		public ByteArrayDataSource(byte[] data, String type) {   
			super();   
			this.data = data;   
			this.type = type;   
		}   

		public ByteArrayDataSource(byte[] data) {   
			super();   
			this.data = data;   
		}   

		public void setType(String type) {   
			this.type = type;   
		}   

		public String getContentType() {   
			if (type == null)   
				return "application/octet-stream";   
			else  
				return type;   
		}   

		public InputStream getInputStream() throws IOException {   
			return new ByteArrayInputStream(data);   
		}   

		public String getName() {   
			return "ByteArrayDataSource";   
		}   

		public OutputStream getOutputStream() throws IOException {   
			throw new IOException("Not Supported");   
		}
	}   
}
