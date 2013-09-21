package com.kimkong.obligfem.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kimkong.obligfem.Configuration;
import com.kimkong.obligfem.ManageStations;
import com.kimkong.obligfem.R;

public class ConfigurationsFragment extends Fragment implements OnClickListener
{
	public static final int RANGE_BETWEEN_TEMPERATURES = 2;
	public static final double MIN_DEGREE = 0.0;
	public static final double MAX_DEGREE = 0.1;
	public static final boolean SERVICE_RUNNING = false;
	
	private int rangeBetweenTemperatures = RANGE_BETWEEN_TEMPERATURES;
	private double minDegree = MIN_DEGREE;
	private double maxDegree = MAX_DEGREE;
	
	
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_configurations, container, false);
		this.view = view;
		updateConfigurations();
		sendConfigurationsToView();
		Button saveButton = (Button)view.findViewById(R.id.button_save);
		saveButton.setOnClickListener(this);
		
		return view;
	}
	
	private void updateConfigurations()
	{
		Configuration currentConf = ManageStations.getConfiguration(getActivity());
		if(currentConf != null)
		{
			rangeBetweenTemperatures = currentConf.getRange();
			minDegree = currentConf.getMinTemp();
			maxDegree = currentConf.getMaxTemp();
		} 
	}
	
	private void sendConfigurationsToView()
	{
		EditText range = (EditText) view.findViewById(R.id.editText_range);
		EditText min = (EditText) view.findViewById(R.id.editText_min);
		EditText max = (EditText) view.findViewById(R.id.editText_max);
		
		range.setText(rangeBetweenTemperatures + "");
		min.setText(minDegree + "");
		max.setText(maxDegree + "");
	}

	@Override
	public void onClick(View v) 
	{
		EditText range =(EditText) view.findViewById(R.id.editText_range);
		EditText min = (EditText) view.findViewById(R.id.editText_min);
		EditText max = (EditText) view.findViewById(R.id.editText_max);
		
		Configuration newConf = new Configuration();
		newConf.setMaxTemp(Double.parseDouble(max.getText().toString()));
		newConf.setMinTemp(Double.parseDouble(min.getText().toString()));
		newConf.setRange(Integer.parseInt(range.getText().toString()));
		ManageStations.setConfiguration(getActivity(), newConf);
		
		updateConfigurations();
		sendConfigurationsToView();
	}
	
	
	
	
	
	
	
	
}