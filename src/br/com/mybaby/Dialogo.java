package br.com.mybaby;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;


public class Dialogo extends DialogFragment {

	private final static String TAG = Dialogo.class.getSimpleName();
	public final static String EXTRAS_DIALOGO = "EXTRAS_DIALOGO";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("MyBaby! desconectado.")
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Ciente da desconex�o por Dialogo" + Util.getDataAtual());
				
				((DeviceControlActivity)getActivity()).onOkClickDialogo();
				
			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}

}