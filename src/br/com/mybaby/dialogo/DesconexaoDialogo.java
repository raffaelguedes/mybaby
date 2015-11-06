package br.com.mybaby.dialogo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import br.com.mybaby.DeviceControlActivity;
import br.com.mybaby.R;
import br.com.mybaby.util.Util;

public class DesconexaoDialogo extends DialogFragment {
	private final static String TAG = DesconexaoDialogo.class.getSimpleName();
	public final static String EXTRAS_DESCONEXAO_DIALOGO = "EXTRAS_DESCONEXAO_DIALOGO";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Qual intervalo de desconexão?")
		.setTitle("Desconectar")
		.setIcon(R.drawable.mybaby)
		.setPositiveButton("12horas", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Desconexão por 12 horas. " + Util.getDataAtual());
				
				((DeviceControlActivity)getActivity()).desconectar("12");

			}
		})
		.setNegativeButton("30sec", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Desconexão por 2 horas. " + Util.getDataAtual());
				
				((DeviceControlActivity)getActivity()).desconectar("2");

			}
		})
		.setNeutralButton("6horas", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Desconexão por 6 horas. " + Util.getDataAtual());
				
				((DeviceControlActivity)getActivity()).desconectar("6");

			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}



}
