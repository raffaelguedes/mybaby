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

public class TemCertezaDialogo extends DialogFragment {

	private final static String TAG = TemCertezaDialogo.class.getSimpleName();
	public final static String EXTRAS_DIALOGO_TEM_CERTEZA = "EXTRAS_DIALOGO_TEM_CERTEZA";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Tem certeza que deseja desconectar?")
		.setTitle("Info MyBaby!")
		.setIcon(R.drawable.mybaby)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.i(TAG, "Ciente da desconexão por Dialogo Home. " + Util.getDataAtual());

				((DeviceControlActivity)getActivity()).onOkClickTemCertezaDialogo();

			}
		})
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "Ciente do cancelamento desconexão por Dialogo Home. " + Util.getDataAtual());
				dialog.dismiss();
			}
		});

		// Create the AlertDialog object and return it
		return builder.create();
	}

}
