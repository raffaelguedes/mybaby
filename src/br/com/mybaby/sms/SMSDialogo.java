package br.com.mybaby.sms;

import br.com.mybaby.DeviceControlActivity;
import br.com.mybaby.dialogo.Dialogo;
import br.com.mybaby.util.Util;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class SMSDialogo extends DialogFragment {
	private final static String TAG = Dialogo.class.getSimpleName();
	public final static String EXTRAS_DIALOGO_SMS = "EXTRAS_DIALOGO_SMS";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("SMS enviado para os contatos cadastrados.")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Ciente da desconexão por Dialogo" + Util.getDataAtual());
				
				((DeviceControlActivity)getActivity()).onOkClickDialogoSMS();
				
			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
