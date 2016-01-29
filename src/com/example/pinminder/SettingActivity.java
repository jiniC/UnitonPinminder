package com.example.pinminder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.pinminder.api.ApiManager;
import com.example.pinminder.api.NullApiManager;
import com.example.pinminder.api.SeoulApiManager;
import com.example.pinminder.db.MyDB;
import com.example.pinminder.gps.GpsInfo;
import com.example.pinminder.list.SwipeActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends Activity {
	
	MyDB myDB;
	Intent retIntent;
	
	GpsInfo gpsInfo;
	public ApiManager apiManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);	
		retIntent = new Intent();
		
		myDB = new MyDB(getApplicationContext());
		
		ImageButton logoBtn = (ImageButton) findViewById(R.id.logoBtn);
		Button mailBtn = (Button) findViewById(R.id.mailBtn);
		Button tutorialBtn = (Button) findViewById(R.id.tutorialBtn);
		Button movieBtn = (Button) findViewById(R.id.movieBtn);
		Switch dataSwitch = (Switch) findViewById(R.id.dataSwitch);
		
		
		if(getUsingApi() == true){
			dataSwitch.setChecked(true);
		}
		else{
			dataSwitch.setChecked(false);
		}
		
		
		
		logoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(SettingActivity.this, SwipeActivity.class);
				startActivity(it);
			}
		});
		
		mailBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("mailto:sksms4687@naver.com");
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				startActivity(Intent.createChooser(it, "Choose an Email client"));
				
			}
		});
		
		
		tutorialBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(SettingActivity.this, ViewPagerActivity.class);
				startActivity(it);
			}
		});
		
		movieBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/7z0ETQUgYDQ")); 
				  startActivity(it);
			}
		});
		
		dataSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(getUsingApi() == true){
					saveUsingApi(false);
					myDB.deleteTable();
					retIntent.putExtra("usingApi", "false");
				}
				else{
					saveUsingApi(true);
					
					if(getMylogcation() == null){
		        		apiManager = new NullApiManager();
		        	}
			        else if(getMylogcation().equals("����Ư����") || getMylogcation().equals("Seoul")){
			        	apiManager = new SeoulApiManager(SettingActivity.this,1);
			        }
			        apiManager.getApi();
				}
			}
		});

	}
	
	 private String getMylogcation(){
	    	String cityName = null;
	    	gpsInfo = new GpsInfo(SettingActivity.this);
	        // GPS ������� ��������
	        if (gpsInfo.isGetLocation()) {

	            double latitude = gpsInfo.getLatitude();
	            double longitude = gpsInfo.getLongitude();
	            
	            /*---------- ���ø� �������� ----------- */
	            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
	            List<Address> addresses;
	            try {
	                addresses = gcd.getFromLocation(latitude,
	                		longitude, 1);
	                if (addresses.size() > 0)
	                    System.out.println(addresses.get(0).getLocality());
	                cityName = addresses.get(0).getAdminArea();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	 
	            String s = longitude + "\n" + latitude + "\n\n����� ���� ���ø� : "
	                    + cityName;
	            
	            Log.i("ohdokingLocation",s);
	             
	        } else {
	            // GPS �� ����Ҽ� �����Ƿ�
//	        	gpsInfo.showSettingsAlert();
	        }
			return cityName;
	    }
	 
	 
	 /**
	  * SharedPrefernce
	  * @return
	  */
	
	
	
	// api �޾ƿ��� ����
	private boolean getUsingApi() {
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		return pref.getBoolean("usingApi", false);

	}
	
	 // api �޾ƿ��� ���� �����ϱ�
    private void saveUsingApi(Boolean value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("usingApi", value);
        editor.commit();
    }
		
		
}
