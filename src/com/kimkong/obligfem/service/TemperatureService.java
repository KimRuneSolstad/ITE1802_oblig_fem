package com.kimkong.obligfem.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import android.R;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.kimkong.obligfem.Configuration;
import com.kimkong.obligfem.ManageStations;
import com.kimkong.obligfem.Station;


public class TemperatureService extends Service
{
	public static final String STATION_LIST_PATH = "station_list_path";
	
	private Timer updateTimer;
	int updateFreq = 3600000;
	
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Configuration conf = ManageStations.getConfiguration(this);
		int freq = conf.getRange() + updateFreq;
		updateTimer = new Timer("temperatureUpdates");
		updateTimer.scheduleAtFixedRate(doRefresh, 0, freq);
		
		return Service.START_STICKY;
	}
	
	private TimerTask doRefresh = new TimerTask()
	{
		@Override
		public void run() {
			refreshTemperatures();
		}	
	};
	
	@Override
	public void onDestroy()
	{
		updateTimer.cancel();
	}
	
	@Override
	public void onCreate()
	{
		updateTimer = new Timer("temperatureUpdates");
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public void refreshTemperatures()
	{
		ArrayList<Station> stations = ManageStations.getStations(this);
		Configuration conf = ManageStations.getConfiguration(this);
		
//		updateFreq = conf.getRange() * 1000;
		String potentialNotification = "";
		for(Station s:stations)
		{
			s = extractTemperatureFromXml(s);
			ManageStations.updateStation(this, s);
			double temp = s.getTemperature();
			if(temp > conf.getMaxTemp() || temp < conf.getMinTemp())
				potentialNotification += s.toString() + "\n";
		}
		if(!potentialNotification.equals(""))
			makeNotification(potentialNotification);
	}
	
	private void makeNotification(String content)
	{
		Notification notification = new Builder(this)
			.setContentTitle("Tempratur registrert")
			.setContentText(content)
			.setSmallIcon(R.drawable.alert_light_frame)
			.build();
		
		NotificationManager notificationManager = (NotificationManager) 
				getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.notify(0, notification);
	}
	
	private Station extractTemperatureFromXml(Station s)
	{
	//weatherdata
		//observations
			//weatherstation
				//temperature
		Document dom;
		try 
		{
			String str = s.getUrl();
			dom = downloadXml(new URL(s.getUrl()));
			if(dom != null)
			{
				NodeList observations = dom.getElementsByTagName("observations");
				for(int i = 0; i < observations.getLength(); i++)
				{
					
					Node oNode = observations.item(i);
					if(oNode.getNodeType() == Node.ELEMENT_NODE)
					{
						
						Element observation = (Element) oNode;
						NodeList stations = observation.getElementsByTagName("weatherstation");
						for(int j = 0; j < stations.getLength(); j++)
						{
							Node sNode = stations.item(j);
							if(sNode.getNodeType() == Node.ELEMENT_NODE)
							{
								Element station = (Element) sNode;
								if(sNode.getAttributes().item(2).getNodeValue().equals(s.getName())
										&& sNode.getAttributes().item(0).getNodeValue().equals(s.getNumber() + ""))
								{
									NodeList temperatures = station.getElementsByTagName("temperature");
									NamedNodeMap map = temperatures.item(0).getAttributes();
									
									s.setTemperature(Double.parseDouble(map.item(1).getNodeValue()));
								}
							}
						}
					}
				}
			}
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		return s;
	}
	
	private Document downloadXml(URL url)
	{
		try 
		{
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			
			int response = httpConnection.getResponseCode();
			if(response == HttpURLConnection.HTTP_OK);
			{
				InputStream in = httpConnection.getInputStream();
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(in);
				return dom;
			}
			
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}