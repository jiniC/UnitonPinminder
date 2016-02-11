package com.example.pinminder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pinminder.api.ApiManager;
import com.example.pinminder.api.NullApiManager;
import com.example.pinminder.api.SeoulApiManager;
import com.example.pinminder.gps.GpsInfo;

public class SplashActivity2 extends Activity {
    public ImageView bg;
    public ApiManager apiManager;
    
    GpsInfo gpsInfo;
   
    int finishApi = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity2);
        
        //api를 받기로 하면 api를 받는다.
        
        

        if(getUsingApi() == true){
        	if(getMylogcation() == null){
        		apiManager = new NullApiManager();
        	}
	        else if(getMylogcation().equals("서울특별시") || getMylogcation().equals("Seoul")){
	        	apiManager = new SeoulApiManager(getApplicationContext(),0);
	        }
	        apiManager.getApi();
	        
        }
        else{
//        	apiSettingToast();
        }
        
        
        final int[] imageArray = { R.drawable.loading_app_1, 
        		R.drawable.loading_app_2,
        		R.drawable.loading_app_3,
        		R.drawable.loading_app_1, 
        		R.drawable.loading_app_2,
        		R.drawable.loading_app_3
        };
        
        
        
        
        bg = (ImageView)findViewById(R.id.splash_title);
        final Handler handler = new Handler();
        
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
            	bg.setImageResource(imageArray[i]);
                i++;
                if(i == imageArray.length)
                {
                	if(getUsingApi() == true){
                		try {
                			finishApi = apiManager.getFinishApi();
						} catch (Exception e) {
							finishApi = 0;
						}
                	}
                	if( finishApi == 6 || getUsingApi() == false){
	                	SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
	                    String s = pref.getString("tuto", "");
	                    
	                    
	                    if(s.isEmpty()){
	                    	
	                    	Intent intent = new Intent(SplashActivity2.this,ViewPagerActivity.class);
	                    	startActivity(intent);
	                    	
	                    }
	                    
	                    finish(); 
                	}
                    i = 0 ;
                }
                handler.postDelayed(this, 300);
            }
        };
        handler.postDelayed(runnable, 100);
    }
    
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
           return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
  
    
    //api 받아오기 여부
    private boolean getUsingApi(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getBoolean("usingApi", false);    
        
    }
    
    private String getMylogcation(){
    	String cityName = null;
    	gpsInfo = new GpsInfo(SplashActivity2.this);
        // GPS 사용유무 가져오기
        if (gpsInfo.isGetLocation()) {

            double latitude = gpsInfo.getLatitude();
            double longitude = gpsInfo.getLongitude();
            
            /*---------- 도시명 가져오기 ----------- */
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
 
            String s = longitude + "\n" + latitude + "\n\n당신의 현재 도시명 : "
                    + cityName;
            
            Log.i("ohdokingLocation",s);
             
        } else {
            // GPS 를 사용할수 없으므로
//        	gpsInfo.showSettingsAlert();
        }
		return cityName;
    }
    
}
