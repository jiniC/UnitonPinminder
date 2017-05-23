package com.minder.pinminder.list;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minder.pinminder.R;
import com.minder.pinminder.dto.Dream;

public class ListAdapter extends BaseAdapter {
	private Activity activity;
	private List<Dream> data;
	private ArrayList<Dream> arraylist;
	private List<Dream> originalList;

	public ListAdapter(Activity a, List<Dream> list) {
		activity = a;
		data = list;
		originalList = new ArrayList<Dream>();
		originalList.addAll(list);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.test_layout, null);
			holder.text = (TextView) convertView
					.findViewById(R.id.mem_info_txt_id);
			holder.image = (ImageView) convertView
					.findViewById(R.id.mem_photo_img_id);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.text.setText(data.get(position).getTodo());
		if ((data.get(position).getCategory()).equals("음식"))
			holder.image.setImageResource(R.drawable.listicon1);
		else if ((data.get(position).getCategory()).equals("관람"))
			holder.image.setImageResource(R.drawable.listicon2);
		else if ((data.get(position).getCategory()).equals("활동"))
			holder.image.setImageResource(R.drawable.listicon3);
		else if ((data.get(position).getCategory()).equals("할 것"))
			holder.image.setImageResource(R.drawable.listicon4);
		else if ((data.get(position).getCategory()).equals("기타"))
			holder.image.setImageResource(R.drawable.listicon5);

		return convertView;
	}

	public static class ViewHolder {
		TextView text;
		ImageView image;

	}

	public void filterData(String query) {

//		query = query;
		
		data.clear();

		if (TextUtils.isEmpty(query)) {
			data.addAll(originalList);
		} else {

			for (Dream dream : originalList) {

				/*ArrayList<Dream> countryList = continent.getTodo();
				ArrayList<Dream> newList = new ArrayList<Dream>();
				for (Dream country : countryList) {
					if (country.getTodo().toLowerCase().contains(query)
							|| country.getMemo().toLowerCase().contains(query)) {
						newList.add(country);
					}
				}
				if (newList.size() > 0) {
					Dream nContinent = new Dream(dream.getName(), newList);
					data.add(nContinent);
				}*/
				
				if (dream.getTodo().contains(query))
				{
					
					data.add(dream);
				}
			}
		}

		notifyDataSetChanged();

	}
	
	public void filterCategory(String query) {

//		query = query;
		
		data.clear();

		if (TextUtils.isEmpty(query)) {
			data.addAll(originalList);
		} else {
//			Log.v("originalList1", String.valueOf(originalList.size()));
			for (Dream dream : originalList) {

				/*ArrayList<Dream> countryList = continent.getTodo();
				ArrayList<Dream> newList = new ArrayList<Dream>();
				for (Dream country : countryList) {
					if (country.getTodo().toLowerCase().contains(query)
							|| country.getMemo().toLowerCase().contains(query)) {
						newList.add(country);
					}
				}
				if (newList.size() > 0) {
					Dream nContinent = new Dream(dream.getName(), newList);
					data.add(nContinent);
				}*/
				
				if (dream.getCategory().equals(query))
				{
					
					data.add(dream);
				}
			}
		}

		notifyDataSetChanged();

	}

	/*
	 * public void filter(CharSequence categoryString) { // categoryString =
	 * categoryString.toLowerCase(Locale.getDefault()); data.clear(); if
	 * (categoryString.length() == 0) { data.addAll(arraylist); } else { for
	 * (Dream wp : arraylist) { if (wp.getTodo().contains(categoryString)) {
	 * data.add(wp); } } } notifyDataSetChanged(); }
	 */
}
