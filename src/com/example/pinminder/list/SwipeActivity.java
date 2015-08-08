package com.example.pinminder.list;

import java.util.ArrayList;

import com.example.pinminder.R;
import com.example.pinminder.WriteActivity;
import com.example.pinminder.db.MyDB;
import com.example.pinminder.dto.Dream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
//08-09 03:01:04.349: E/AndroidRuntime(4062): 	Suppressed: java.lang.ClassNotFoundException: com.google.android.gms.maps.MapFragment

public class SwipeActivity extends Activity {

	private ListView cmn_list_view;
	private ListAdapter listAdapter;
	private ArrayList<dumpclass> listdata;
	private ImageButton plusBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0090e9")));
		
		
		cmn_list_view = (ListView) findViewById(R.id.cmn_list_view);
		listdata = new ArrayList<dumpclass>();
		InitializeValues();
		final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(cmn_list_view, swipeListener, this);
		touchListener.SwipeType = ListViewSwipeGesture.Double; // Set two
																// options at
																// background of
																// list item

		cmn_list_view.setOnTouchListener(touchListener);

		plusBtn = (ImageButton) findViewById(R.id.todolist_addbtn);
		plusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SwipeActivity.this, WriteActivity.class);
				startActivity(i);
			}
		});
		
		

	}

	private void InitializeValues() {
		// TODO Auto-generated method stub
		/*
		 * listdata.add(new dumpclass("one")); 
		 * listdata.add(newdumpclass("two")); 
		 * listdata.add(new dumpclass("three"));
		 * listdata.add(new dumpclass("four")); listdata.add(new
		 * dumpclass("five")); listdata.add(new dumpclass("six"));
		 * listdata.add(new dumpclass("seven")); listdata.add(new
		 * dumpclass("Eight"));
		 */
		//MyDB db = new MyDB(getApplicationContext());
		listdata.add(new dumpclass("�븰�������� 8��ȣ �꾾 ���� ��� Ȯ��","Ȱ��"));
		listdata.add(new dumpclass("���� ã�� ����","����"));
		
		
		/*
		for (int i = 0; i < db.getAllDreams().size(); i++) {
			
			listdata.add(new dumpclass(db.getDream(i).getTodo().toString(),db.getDream(i).getCategory().toString()));
			
		}*/
		listAdapter = new ListAdapter(this, listdata);
		cmn_list_view.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.swipe, menu);
		return true;
	}

	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void HalfSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
			for (int i : reverseSortedPositions) {
				listdata.remove(i);
				listAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
			
		}

	};

}
