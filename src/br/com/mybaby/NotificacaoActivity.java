package br.com.mybaby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NotificacaoActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificacao);
		
		Button botaoOK = (Button) findViewById(R.id.ok_button);
		botaoOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NotificacaoActivity.this, DeviceControlActivity.class);
				intent.putExtra("onOkClick","onOkClick");
				startActivity(intent);
				
			}
		});
	}
}
