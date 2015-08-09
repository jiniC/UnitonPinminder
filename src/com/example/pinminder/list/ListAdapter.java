package com.example.pinminder.list;

import java.util.ArrayList;
import java.util.List;

import com.example.pinminder.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	private Activity activity;
	private List<com.example.pinminder.dto.Dream> data;

	public ListAdapter(Activity a, List<com.example.pinminder.dto.Dream> list) {
		activity = a;
		data = list;
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.test_layout, null);
			holder.text = (TextView) convertView.findViewById(R.id.mem_info_txt_id);
			holder.image = (ImageView) convertView.findViewById(R.id.mem_photo_img_id);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.text.setText(data.get(position).getTodo());
		if((data.get(position).getCategory()).equals("음식"))
			holder.image.setImageResource(R.drawable.listicon1);
		else if((data.get(position).getCategory()).equals("관람"))
			holder.image.setImageResource(R.drawable.listicon2);
		else if((data.get(position).getCategory()).equals("활동"))
			holder.image.setImageResource(R.drawable.listicon3);
		else if((data.get(position).getCategory()).equals("할 것"))
			holder.image.setImageResource(R.drawable.listicon4);
		else if((data.get(position).getCategory()).equals("기타"))
			holder.image.setImageResource(R.drawable.listicon5);

		return convertView;
	}

	public static class ViewHolder {
		TextView text;
		ImageView image;

	}
}
