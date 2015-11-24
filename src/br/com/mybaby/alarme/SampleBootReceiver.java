package br.com.mybaby.alarme;

import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.util.Constantes;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class SampleBootReceiver extends BroadcastReceiver {
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    private SistemaDAO sistemaDAO;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
        	sistemaDAO = new SistemaDAO(context);
        	sistemaDAO.update(Constantes.ALARME_ALERTAS_BOOT, Boolean.TRUE.toString());
            alarm.setAlarm(context);
        }
    }
}
//END_INCLUDE(autostart)
