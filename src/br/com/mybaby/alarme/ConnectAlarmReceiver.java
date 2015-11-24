package br.com.mybaby.alarme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import br.com.mybaby.dao.SistemaDAO;
import br.com.mybaby.util.Constantes;

public class ConnectAlarmReceiver extends WakefulBroadcastReceiver {
	// The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    private SistemaDAO sistemaDAO;
    
    @Override
    public void onReceive(Context context, Intent intent) {   
    	Intent service=null;
    	// BEGIN_INCLUDE(alarm_onreceive)
    	/* 
    	 * If your receiver intent includes extras that need to be passed along to the
    	 * service, use setComponent() to indicate that the service should handle the
    	 * receiver's intent. For example:
    	 * 
    	 * ComponentName comp = new ComponentName(context.getPackageName(), 
    	 *      MyService.class.getName());
    	 *
    	 * // This intent passed in this call will include the wake lock extra as well as 
    	 * // the receiver intent contents.
    	 * startWakefulService(context, (intent.setComponent(comp)));
    	 * 
    	 * In this example, we simply create a new intent to deliver to the service.
    	 * This intent holds an extra identifying the wake lock.
    	 */
    	service = new Intent(context, ConnectSchedulingService.class);
    	

    	// Start the service, keeping the device awake while it is launching.
    	startWakefulService(context, service);
    	// END_INCLUDE(alarm_onreceive)
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     * @throws ParseException 
     */
    public void setAlarm(Context context, String tempo) throws ParseException {
    	alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

    	Intent intent = new Intent(context, ConnectAlarmReceiver.class);
    	intent.putExtra(Constantes.ALARME_CONNECT, Constantes.ALARME_CONNECT);
    	alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

    	//BOOT
    	if(tempo.equals("BOOT")){
    		sistemaDAO = new SistemaDAO(context);
    		String tempoEscolhido = "0"; //sistemaDAO.getValor(Constantes.ALARME_CONNECT_TEMPO);
    		String horario = sistemaDAO.getValor(Constantes.ALARME_CONNECT_HORARIO);
    		Log.d("ConnectAlarmReceiver", "Horario: " + horario);
    		Log.d("ConnectAlarmReceiver", "Tempo: " + tempoEscolhido);
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
    		Date dataInicial = new Date(sdf.parse(horario).getTime());

    		Calendar cal = Calendar.getInstance(); // creates calendar
    		cal.setTime(dataInicial); // sets calendar time/date
    		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(tempoEscolhido)); // adds one hour

    		Log.d("ConnectAlarmReceiver", "Hora+Tempo: " + sdf.format(cal.getTime()));

    		long dif = getDateDiff(cal.getTime(), new Date(), TimeUnit.HOURS);

    		Log.d("ConnectAlarmReceiver", "Dif: " + dif);

    		if(dif >0){
    			alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + dif*3600000, alarmIntent);
    		}else{
    			alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), alarmIntent);
    		}

    	}else if(tempo.equals("2")){
    		//alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2*3600000, alarmIntent);
    		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30*1000, alarmIntent);
    	} else if(tempo.equals("6")){
    		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 6*3600000, alarmIntent);
    	}else if(tempo.equals("12")){
    		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 12*3600000, alarmIntent);
    	}
    	// Enable {@code SampleBootReceiver} to automatically restart the alarm when the
    	// device is rebooted.
    	ComponentName receiver = new ComponentName(context, ConnectBootReceiver.class);
    	PackageManager pm = context.getPackageManager();

    	pm.setComponentEnabledSetting(receiver,
    			PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
    			PackageManager.DONT_KILL_APP);           

    }
    // END_INCLUDE(set_alarm)
    
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date1.getTime() - date2.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, ConnectBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)
}
