package com.example.pinminder.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pinminder.R;




public class DialogActivity extends Activity implements OnClickListener{
    
	private ImageButton regionBtn, cat1, cat2, cat3, cat4, cat5, alarmBtn, memoBtn;
	private String category;
	Button okBtn,cancelBtn;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view);
 
        /*CustomGrid adapter = new CustomGrid(DialogActivity.this, web, imageId);
        	grid=(GridView)findViewById(R.id.grid);
                grid.setAdapter(adapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(DialogActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
 
                    }
                });*/
                
        
        
        cat1 = (ImageButton) findViewById(R.id.cate1);
		cat2 = (ImageButton) findViewById(R.id.cate2);
		cat3 = (ImageButton) findViewById(R.id.cate3);
		cat4 = (ImageButton) findViewById(R.id.cate4);
		cat5 = (ImageButton) findViewById(R.id.cate5);
		
		// cat5.setVisibility(View.GONE);

		cat1.setOnClickListener(this);
		cat2.setOnClickListener(this);
		cat3.setOnClickListener(this);
		cat4.setOnClickListener(this);
		cat5.setOnClickListener(this);
		
		okBtn = (Button) findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// cate1일때
				// 메소드
				// cate2일때
				// 메소드
				// cate3일때
				// 메소드
				// cate4일때
				// 메소드
				// cate5일때
				// 메소드
				finish();

			}
		});

		cancelBtn = (Button) findViewById(R.id.deleteBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
 
    }
    
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cate1:
			category = "음식";
			cat1.setImageResource(R.drawable.writeicon1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.inactive5);
			Toast.makeText(DialogActivity.this,  category , Toast.LENGTH_SHORT).show();
			break;
		case R.id.cate2:
			category = "관람";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.writeicon2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.inactive5);
			Toast.makeText(DialogActivity.this, category , Toast.LENGTH_SHORT).show();
			break;
		case R.id.cate3:
			category = "활동";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.writeicon3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.inactive5);
			Toast.makeText(DialogActivity.this, category , Toast.LENGTH_SHORT).show();
			break;
		case R.id.cate4:
			category = "할 것";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.writeicon4);
			cat5.setImageResource(R.drawable.inactive5);
			Toast.makeText(DialogActivity.this, category , Toast.LENGTH_SHORT).show();
			break;
		case R.id.cate5:
			category = "기타";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.writeicon5);
			Toast.makeText(DialogActivity.this, category , Toast.LENGTH_SHORT).show();
			break;
		}

	}
    
    
    public void fillter() {
    	
    }
}