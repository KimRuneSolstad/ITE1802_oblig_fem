package com.kimkong.obligfem;

public class Configuration 
{
	private double minTemp;
	private double maxTemp;
	private int range;
	private boolean serviceRunning;
	
	public Configuration()
	{
		
	}
	
	public Configuration(double minTemp, double maxTemp, int range)
	{
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.range = range;
	}
	
	public void setRange(int range){ this.range = range; }
	public void setMinTemp(double minTemp){ this.minTemp = minTemp; }
	public void setMaxTemp(double maxTemp){ this.maxTemp = maxTemp; }
	public void setServiceRunning(boolean serviceRunning){ this.serviceRunning = serviceRunning; }
	
	public int getRange(){ return range; }
	public double getMinTemp(){ return minTemp; }
	public double getMaxTemp(){ return maxTemp; }
	public boolean getServiceRunning(){ return serviceRunning; }

}
