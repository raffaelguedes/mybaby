package br.com.mybaby.alarme;

import java.text.ParseException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectBootReceiver  extends BroadcastReceiver {
	ConnectAlarmReceiver alarm = new ConnectAlarmReceiver();
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
			try {
				alarm.setAlarm(context, "BOOT");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} 

}
