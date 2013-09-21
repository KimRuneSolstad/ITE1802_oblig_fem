package com.kimkong.obligfem;

public class Station 
{
	private int number;
	private String name;
	private String url;
	private double temperature;
	
	public Station()
	{
		temperature = -273.15;
	}
	
	public Station(int number, String name, String url)
	{
		this.number = number;
		this.name = name;
		this.url = url;
		temperature = -273.15;
	}
	
	public void setUrl(String url)		{ this.url = url; }
	public void setName(String name)	{ this.name = name; }
	public void setNumber(int number)	{ this.number = number; }
	public void setTemperature(double temperature) { this.temperature = temperature; }
	
	public String getUrl()	{ return url; }
	public String getName()	{ return name; }
	public int getNumber()	{ return number; }
	public double getTemperature(){ return temperature; }
	
	public String toString() { return name +"\ttemp: " + temperature; }

}
