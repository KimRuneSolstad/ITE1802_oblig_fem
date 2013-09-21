package com.kimkong.obligfem.fragments;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kimkong.obligfem.ManageStations;
import com.kimkong.obligfem.Station;

public class RegistrationsFragment extends ListFragment
{	
	private ArrayAdapter<String> adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		fillStations();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		ManageStations.removeStation(getActivity(), position);	
		fillStations();
	}
	
	private void fillStations()
	{
		Station[] stations = getStations();
		String[] values;
		if(stations.length != 0)
		{
			values = new String[stations.length];
			
			for(int i = 0; i < stations.length; i++)
				values[i] = stations[i].toString();
		} else
			values = new String[]{"Ingen stasjoner registrert"};
		
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}
	
	private Station[] getStations()
	{
		ArrayList<Station> list = ManageStations.getStations(getActivity());
		Station[] stations = new Station[list.size()];
		for(int i = 0; i < list.size(); i++)
			stations[i] = list.get(i);
		return stations;
			
	}
}