package com.kimkong.obligfem.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kimkong.obligfem.ManageStations;
import com.kimkong.obligfem.R;
import com.kimkong.obligfem.Station;

public class NewFragment extends Fragment implements OnClickListener
{
	public static final String FILE_NAME_SATIONS = "stations_list";
	
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_new, container, false);
		Button button = (Button) view.findViewById(R.id.button_add);
		button.setOnClickListener(this);
		this.view = view;
		return view;
	}
	
	@Override
	public void onClick(View v) 
	{
		EditText urlField = (EditText) view.findViewById(R.id.editText_url);
		EditText nameField = (EditText) view.findViewById(R.id.editText_name);
		EditText numberField = (EditText) view.findViewById(R.id.editText_number);

		Station s = new Station(Integer.parseInt(numberField.getText().toString()), 
			nameField.getText().toString(), urlField.getText().toString());
		
		ManageStations.addStation(getActivity(), s);
		
		urlField.setText("http://www.yr.no/sted/Land/Fylke/Kommune/Sted/varsel.xml");
		nameField.setText("");
		numberField.setText("");
	}
}


















