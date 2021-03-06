package com.minder.pinminder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.minder.pinminder.db.MyDB;
import com.minder.pinminder.dialog.DeleteActivity;
import com.minder.pinminder.dialog.DialogActivity;
import com.minder.pinminder.dto.Dream;
import com.minder.pinminder.model.PushEvent;

public class MainActivity extends Activity {

	private SQLiteDatabase database; 
	public static int splash = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(splash == 0){
			
			startActivity(new Intent(this, SplashActivity2.class));
			splash++;
		}
		
		Button filterBtn = (Button)findViewById(R.id.filterButton);
		Button addFtn = (Button)findViewById(R.id.addButton);
		Button pushBtn = (Button)findViewById(R.id.pushButton);
		Button gpsBtn = (Button)findViewById(R.id.gpsButton);
		Button shh = (Button) findViewById(R.id.button1);
		
		shh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,FilterAcitivy.class);
				startActivity(i);
			}
		});
		filterBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,FilterAcitivy.class);
				startActivity(i);
			}
		});
		
		addFtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,WriteActivity.class);
				startActivity(i);
			}
		});
		pushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,CreateNotificationActivity.class);
				startActivity(i);
			}
		});
		gpsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		        String s = pref.getString("tuto", "");
		        
		        
		        if(s.isEmpty()){
		        	
		        	Intent i = new Intent(MainActivity.this,ViewPagerActivity.class);
		        	startActivity(i);
		        }
			}
		});
		
		  
		serviceStart();
		
		MyDB my = new MyDB(this);
		Dream d = new Dream(1, "test", "test2", 123.123, 123.123,
				"test3", "test4", "test5", 1,0,1);
		my.addDream(d);
		/*Toast toast = Toast.makeText(getApplicationContext(),
				my.getDream(0).getCategory(), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();*/
		
		
	}
	
	public void serviceStart(){
		Intent i = new Intent(MainActivity.this,PushEvent.class);
        startService(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
 
        return super.onCreateOptionsMenu(menu);
	}

	/**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_search:
        	Intent i2 = new Intent(MainActivity.this,DeleteActivity.class);
        	startActivity(i2);
            // search action
            return true;
        case R.id.action_location_found:
        	
        	Intent i = new Intent(MainActivity.this,DialogActivity.class);
        	startActivity(i);
            // location found
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
 // ?��?��?�� �겫�뜄�쑎?��?��궎疫?�옙
    private void getPreferences(){
        
    }
    
    
    
}
