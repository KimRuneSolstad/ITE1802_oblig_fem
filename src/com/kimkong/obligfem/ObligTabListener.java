package com.kimkong.obligfem;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class ObligTabListener<T extends Fragment> implements TabListener
{
	private Fragment fragment;
	private final Activity activity;
	private String tag;
	private Class<T> fClass;
	
	public ObligTabListener(Activity activity, String tag, Class<T> clz)
	{
		this.activity = activity;
		this.tag = tag;
		this.fClass = clz;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		if(fragment == null)
		{
			fragment = Fragment.instantiate(activity, fClass.getName());
			ft.add(android.R.id.content, fragment, tag);
		} else
		{
			ft.attach(fragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		if(fragment != null)
		{
			ft.detach(fragment);
		}
		
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

}
