package com.kimkong.obligfem;

import java.util.ArrayList;

import com.kimkong.obligfem.fragments.ConfigurationsFragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class ManageStations 
{
	private static final String STAT_KEY = "station";
	private static final String CONF_KEY = "configuration";
	
	//for å serialisere
	private static final String SEPARATE_STATIONS_TAG = " , ";
	private static final String SEPARATE_PARAMETERS_TAG = "#";
	//for å serializere configurations
	private static final int PARAMETER_CONFIGURATION_MAX = 0;
	private static final int PARAMETER_CONFIGURATION_MIN = 1;
	private static final int PARAMETER_CONFIGURATION_RANGE = 2;
	private static final int PARAMETER_CONFIGURATION_SERVICE_RUNNING = 3;
	//for å serialisere ArrayList
	private static final int PARAMETER_STATION_NAME = 0;
	private static final int PARAMETER_STATION_NUMBER = 1;
	private static final int PARAMETER_STATION_URL = 2;
	private static final int PARAMETER_STATION_TEMPERATURE = 3;
	
	public static void setConfiguration(Context context, Configuration conf)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putString(CONF_KEY, serializeConfiguration(conf));
		edit.commit();
	}
	
	public static Configuration getConfiguration(Context context)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String serConf = sp.getString(CONF_KEY, "");
		
		if(serConf.equals(""))
		{
			Configuration conf = new Configuration(ConfigurationsFragment.MAX_DEGREE, 
					ConfigurationsFragment.MIN_DEGREE, ConfigurationsFragment.RANGE_BETWEEN_TEMPERATURES);
			conf.setServiceRunning(ConfigurationsFragment.SERVICE_RUNNING);
			return conf;
		}
		
		return deSerializeConfiguration(serConf);
	}
	
	private static Configuration deSerializeConfiguration(String s)
	{
		String[] params = s.split(SEPARATE_PARAMETERS_TAG);
		Configuration conf = new Configuration();
		
		conf.setMaxTemp(Double.parseDouble(
				params[PARAMETER_CONFIGURATION_MAX]));
		conf.setMinTemp(Double.parseDouble(
				params[PARAMETER_CONFIGURATION_MIN]));
		conf.setRange(Integer.parseInt(
				params[PARAMETER_CONFIGURATION_RANGE]));
		conf.setServiceRunning(params
				[PARAMETER_CONFIGURATION_SERVICE_RUNNING].equals("true"));
		return conf;
	}
	
	private static String serializeConfiguration(Configuration conf)
	{
		String serializedConfiguration = "";
		
		String[] confParameters = new String[4];
		confParameters[PARAMETER_CONFIGURATION_MAX] = conf.getMaxTemp() + "";
		confParameters[PARAMETER_CONFIGURATION_MIN] = conf.getMinTemp() + "";
		confParameters[PARAMETER_CONFIGURATION_RANGE] = conf.getRange() + "";
		confParameters[PARAMETER_CONFIGURATION_SERVICE_RUNNING] = conf.getServiceRunning() + "";
		
		for(String s:confParameters)
			serializedConfiguration += s + SEPARATE_PARAMETERS_TAG;
		
		return serializedConfiguration;
	}
	
	public static void updateStation(Context context, Station station)
	{
		ArrayList<Station> stations = getStations(context);
		for(int i = 0; i < stations.size(); i++)
		{
			if(stations.get(i).getName().equals(station.getName()))
			{
				stations.remove(i);
				stations.trimToSize();
				stations.add(station);
			}
		}
		
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		String ser = toSerializedString(stations);
		edit.putString(STAT_KEY, ser);
		edit.commit();
	}
	
	public static void addStation(Activity activity, Station station)
	{
		ArrayList<Station> list = getStations(activity);
		list.add(station);
		String serialized = toSerializedString(list);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		Editor editor = sp.edit();
		editor.putString(STAT_KEY, serialized);
		editor.commit();
	}
	
	public static void removeStation(Activity activity, int index)
	{
		ArrayList<Station> stations = getStations(activity);
		stations.remove(index);
		stations.trimToSize();
		String ser = toSerializedString(stations);
		
		Editor edit = PreferenceManager.getDefaultSharedPreferences(activity).edit();
		edit.putString(STAT_KEY, ser);
		edit.commit();
	}
	
	public static ArrayList<Station> getStations(Context context)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context); //activity.getSharedPreferences(FILE_NAME_SATIONS, 0);

		String storedStations = sp.getString(STAT_KEY, "");
		ArrayList<Station> list;
		
		if(storedStations.equals(""))
			list = new ArrayList<Station>();
		else
			list = toArrayList(storedStations);
		
		return list;
	}
	
	private static ArrayList<Station> toArrayList(String s)
	{
		//Toast.makeText(this, "toArray", Toast.LENGTH_LONG).show();
		ArrayList<Station> stations = new ArrayList<Station>();
		
		String[] stationsSeparated = s.split(SEPARATE_STATIONS_TAG);
		for(String singleStation : stationsSeparated)
		{
			String[] parameters = singleStation.split(SEPARATE_PARAMETERS_TAG);
			Station station = new Station(Integer.parseInt(parameters[PARAMETER_STATION_NUMBER]),
					parameters[PARAMETER_STATION_NAME], parameters[PARAMETER_STATION_URL]);
			station.setTemperature(Double.parseDouble(parameters[PARAMETER_STATION_TEMPERATURE]));
			stations.add(station);
		}
			
		return stations;
	}
	
	private static String toSerializedString(ArrayList<Station> stations)
	{
		String serialized = "";
		
		for(Station s : stations)
		{
			String[] parameters = new String[4];
			parameters[PARAMETER_STATION_NUMBER] = s.getNumber() + "";
			parameters[PARAMETER_STATION_NAME] = s.getName();
			parameters[PARAMETER_STATION_URL] = s.getUrl();
			parameters[PARAMETER_STATION_TEMPERATURE] = s.getTemperature() + "";
			
			for(String parameter: parameters)
				serialized += parameter + SEPARATE_PARAMETERS_TAG;
			serialized += SEPARATE_STATIONS_TAG;
		}
		
		return serialized;
	}
	
}
