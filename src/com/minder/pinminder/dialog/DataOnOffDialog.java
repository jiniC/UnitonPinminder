package com.minder.pinminder.dialog;

import com.minder.pinminder.R;
import com.minder.pinminder.db.MyDB;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DataOnOffDialog  extends Activity {
Button okBtn;
Button cancelBtn;

@Override
protected void onCreate(Bundle savedInstanceState) {
	requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_dataonoff_view);
    
//    final Dream dream = null;
    
    okBtn = (Button)findViewById(R.id.okBtn);
    cancelBtn = (Button)findViewById(R.id.cancelBtn);
    
    okBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MyDB my = new MyDB(getApplicationContext());
//			my.deleteDream(dream);
			
		}
	});
    cancelBtn.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		finish();
    	}
    });

}
}