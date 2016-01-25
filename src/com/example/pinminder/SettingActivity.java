package com.example.pinminder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

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
		
		logoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 첫화면으로 돌아가기
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
				
			}
		});
		
		movieBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		dataSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					// 다이얼로그 창
				}
				else {
					// 다이얼로그 창
				}
			}
		});

	}
}
