package com.kimkong.obligfem;

import com.kimkong.obligfem.service.TemperatureService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ObligFemBroadcastReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context arg0, Intent arg1) 
	{
		Configuration conf = ManageStations.getConfiguration(arg0);
		
		if(conf.getServiceRunning())
		{
			Intent i = new Intent("com.kimkong.obligfem.service.TemperatureService");
			i.setClass(arg0, TemperatureService.class);
			arg0.startService(i);
		}
	}
}
