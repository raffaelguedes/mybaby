package br.com.mybaby.dialogo;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import br.com.mybaby.preferences.PreferencesActivity;
import br.com.mybaby.util.Util;

public class PreferencesDialogo extends DialogFragment {
	private final static String TAG = PreferencesDialogo.class.getSimpleName();
	public final static String EXTRAS_DIALOGO_PREFERENCES = "EXTRAS_DIALOGO_PREFERENCES";
	private String mensagem;
	
	public PreferencesDialogo(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public PreferencesDialogo() {
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final TextView textView = new TextView(getActivity());
		textView.setText(mensagem);
		textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
		textView.setGravity(Gravity.CENTER);
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Info MyBaby!")
		.setIcon(R.drawable.ic_dialog_alert)
		.setView(textView)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Ok Configurações: " +  Util.getDataAtual());
				
				final Intent intent = new Intent(getActivity(), PreferencesActivity.class);
            	startActivity(intent);
			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
