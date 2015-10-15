package br.com.mybaby.dialogo;

import br.com.mybaby.DeviceControlActivity;
import br.com.mybaby.R;
import br.com.mybaby.Util;
import br.com.mybaby.R.drawable;
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
		builder.setMessage("\n\n		MyBaby! Em segurança! \n\n")
		.setIcon(R.drawable.mybaby)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Ciente da desconexão por Dialogo. " + Util.getDataAtual());
				
				((DeviceControlActivity)getActivity()).onOkClickDialogo();
				
			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}

}
