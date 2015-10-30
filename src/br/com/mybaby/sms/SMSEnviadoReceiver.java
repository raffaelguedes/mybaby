package br.com.mybaby.sms;

import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.util.Constantes;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSEnviadoReceiver extends BroadcastReceiver {
	
	private final String TAG = "SMSEnviadoReceiver";
	
	private SistemaDAO sistemaDAO;
	
	public SMSEnviadoReceiver(Context context) {
		sistemaDAO = new SistemaDAO(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "SMSEnviadoReceiver");
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.d(TAG, "RESULT_OK");
			sistemaDAO.update(Constantes.SMS_ENVIADO, Boolean.TRUE.toString());
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Log.d(TAG, "RESULT_ERROR_GENERIC_FAILURE");


			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Log.d(TAG, "RESULT_ERROR_NO_SERVICE");


			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Log.d(TAG, "RESULT_ERROR_NULL_PDU");
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Log.d(TAG, "RESULT_ERROR_RADIO_OFF");


			break;
		}
	}
}
