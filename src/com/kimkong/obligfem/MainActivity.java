package com.kimkong.obligfem;

import java.util.ArrayList;

import android.R.drawable;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData.Item;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kimkong.obligfem.fragments.ConfigurationsFragment;
import com.kimkong.obligfem.fragments.NewFragment;
import com.kimkong.obligfem.fragments.RegistrationsFragment;
import com.kimkong.obligfem.service.TemperatureService;


public class MainActivity extends Activity
{
	private ArrayList<Station> list = new ArrayList<Station>();
	private boolean serviceRunning;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initActionBar();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{	
		getMenuInflater().inflate(R.menu.activity_main, menu);
		setCurrentConf(menu.getItem(0));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.service_powerbutton)
		{
			if(serviceRunning)
			{
				serviceRunning = false;
				stopService(new Intent(this, TemperatureService.class));
				
				item.setIcon(drawable.button_onoff_indicator_off);
			} else
			{
				startService(new Intent(this, TemperatureService.class));
				serviceRunning = true;
				item.setIcon(drawable.button_onoff_indicator_on);
			}
			Configuration conf = ManageStations.getConfiguration(this);
			conf.setServiceRunning(serviceRunning);
			ManageStations.setConfiguration(this, conf);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void setCurrentConf(MenuItem item)
	{
		Configuration conf = ManageStations.getConfiguration(this);
		boolean serviceRunning = conf.getServiceRunning();
		if(serviceRunning)
			item.setIcon(drawable.button_onoff_indicator_on);
		else
			item.setIcon(drawable.button_onoff_indicator_off);
	}
	
/****************************************** ACTION BAR ********************************************/
	
	private void initActionBar()
	{
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//skrur på faner
		bar.setDisplayShowTitleEnabled(true);//slår av tittel
		bar.setDisplayShowHomeEnabled(true);
		
		addTab(bar, "stasjoner", "registrations", RegistrationsFragment.class);
		addTab(bar, "ny statsjon", "new", NewFragment.class);
		addTab(bar, "instillinger", "configurations", ConfigurationsFragment.class);
	}
	
	/**
	 * Legger til fragment i actionbar
	 * @param actionBar
	 * @param title
	 * @param name
	 * @param c
	 */
	public <T extends Fragment> void addTab(ActionBar actionBar, String title, String name, Class<T> c)
	{	
		Tab tab = actionBar.newTab();
		tab.setText(title);
		tab.setTabListener(new ObligTabListener<T>(this, name, c));
		actionBar.addTab(tab);
	}
	

}
















































