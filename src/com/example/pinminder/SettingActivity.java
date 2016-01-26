package com.example.pinminder;

import com.example.pinminder.list.SwipeActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);	
		ImageButton logoBtn = (ImageButton) findViewById(R.id.logoBtn);
		Button mailBtn = (Button) findViewById(R.id.mailBtn);
		Button tutorialBtn = (Button) findViewById(R.id.tutorialBtn);
		Button movieBtn = (Button) findViewById(R.id.movieBtn);
		Switch dataSwitch = (Switch) findViewById(R.id.dataSwitch);
		
		

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_dataonoff_view);
		dialog.setTitle("Custom Dialog");
		
		final TextView textView2 = (TextView) dialog.findViewById(R.id.textView2);
		
		
		
		logoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(SettingActivity.this, SwipeActivity.class);
				startActivity(it);
			}
		});
		
		mailBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("mailto:sksms4687@naver.com");
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				startActivity(Intent.createChooser(it, "Choose an Email client"));
				
			}
		});
		
		
		tutorialBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(SettingActivity.this, ViewPagerActivity.class);
				startActivity(it);
			}
		});
		
		movieBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/7z0ETQUgYDQ")); 
				  startActivity(it);
			}
		});
		
		dataSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					textView2.setText("지역데이터를 받으시겠습니까?");
					dialog.show();
				}
				else {
					textView2.setText("지역데이터를 받지 않으시겠습니까?");
					dialog.show();
				}
			}
		});

	}
}
