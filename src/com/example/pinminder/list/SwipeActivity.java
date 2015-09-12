package com.example.pinminder.list;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pinminder.R;
import com.example.pinminder.SplashActivity;
import com.example.pinminder.WriteActivity;
import com.example.pinminder.db.MyDB;
import com.example.pinminder.dialog.DeleteActivity;
import com.example.pinminder.dialog.DialogActivity;
import com.example.pinminder.dto.Dream;
import com.example.pinminder.model.GPSTracker;
import com.example.pinminder.model.PushEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//08-09 03:01:04.349: E/AndroidRuntime(4062): 	Suppressed: java.lang.ClassNotFoundException: com.google.android.gms.maps.MapFragment

public class SwipeActivity extends Activity {

	private ListView cmn_list_view;
	private ListAdapter listAdapter;
	private List<Dream> listdata;
	private ImageButton plusBtn;
	MyDB db;
	public static int splash = 0;

	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	private GoogleMap map;

	GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		
		db = new MyDB(getApplicationContext());

		if (splash == 0) {

			startActivity(new Intent(this, SplashActivity.class));
			splash++;
		}

		serviceStart();

		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#5fc4d9")));
		actionBar.setIcon(R.drawable.icon);

		cmn_list_view = (ListView) findViewById(R.id.cmn_list_view);
		listdata = new ArrayList<Dream>();
		InitializeValues();
		final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
				cmn_list_view, swipeListener, this);
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

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Move the camera instantly to hamburg with a zoom of 15.

		// Zoom in, animating the camera.

	}

	public void initMap() {
		if (map != null) {

			gpsTracker = new GPSTracker(getApplicationContext());

			if (!listdata.isEmpty()) {
				for (Dream dream : listdata) {

					int id = 0;

					if (dream.getCategory().equals("음식")) {
						id = R.drawable.mapicon1;
					} else if (dream.getCategory().equals("관람")) {
						id = R.drawable.mapicon2;

					} else if (dream.getCategory().equals("활동")) {

						id = R.drawable.mapicon3;
					} else if (dream.getCategory().equals("할 것")) {

						id = R.drawable.mapicon5;
					} else {
						id = R.drawable.mapicon4;
					}

					LatLng tempLatLng = new LatLng(dream.getLat(),
							dream.getLon());
					Marker kiel = map.addMarker(new MarkerOptions()
							.position(tempLatLng).title(dream.getTodo())
							.snippet(dream.getMemo())
							.icon(BitmapDescriptorFactory.fromResource(id)));
				}
			}

			Location location = gpsTracker.getLocation();
			LatLng moveLatLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 10));
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

			/*
			 * Marker hamburg = map.addMarker(new MarkerOptions()
			 * .position(HAMBURG).title("Hamburg"));
			 */
			/*
			 * .icon(BitmapDescriptorFactory
			 * .fromResource(R.drawable.ic_launcher)));
			 */
		}

	}

	private void InitializeValues() {
		// TODO Auto-generated method stub

		

		/*
		 * for (Dream dream : listdata) { }
		 */
		/*
		 * listdata.add(new dumpclass("one"));
		 * listdata.add(newdumpclass("two")); listdata.add(new
		 * dumpclass("three")); listdata.add(new dumpclass("four"));
		 * listdata.add(new dumpclass("five")); listdata.add(new
		 * dumpclass("six")); listdata.add(new dumpclass("seven"));
		 * listdata.add(new dumpclass("Eight"));
		 */

		// listdata.add(new dumpclass("맛집 찾아 가기","음식"));

		/*
		 * for (int i = 0; i < db.getAllDreams().size(); i++) {
		 * 
		 * listdata.add(new
		 * dumpclass(db.getDream(i).getTodo().toString(),db.getDream
		 * (i).getCategory().toString()));
		 * 
		 * }
		 */

		listdata = db.getAllDreams();
		listAdapter = new ListAdapter(this, listdata);
		cmn_list_view.setAdapter(listAdapter);
	}

	public void serviceStart() {
		Intent i = new Intent(SwipeActivity.this, PushEvent.class);
		startService(i);
	}

	@Override
	public void onResume() {
		super.onRestart();
		
		db = new MyDB(getApplicationContext());
		InitializeValues();
		initMap();
		listAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void onRestart() {
		super.onRestart();
		
		db = new MyDB(getApplicationContext());
		InitializeValues();
		initMap();
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		listAdapter = new ListAdapter(this, db.getAllDreams());
		cmn_list_view.setAdapter(listAdapter);
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
			Intent i2 = new Intent(SwipeActivity.this, DeleteActivity.class);
			startActivity(i2);
			// search action
			return true;
		case R.id.action_location_found:

			Intent i = new Intent(SwipeActivity.this, DialogActivity.class);
			startActivity(i);
			// location found
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "수정", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void HalfSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_SHORT)
					.show();
			db = new MyDB(getApplicationContext());
			db.deleteDream(listdata.get(position));
			
			finish();
			startActivity(getIntent());
			// InitializeValues();
			// onRestart();
		}

		@Override
		public void LoadDataForScroll(int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Delete",
					Toast.LENGTH_SHORT).show();
			for (int i : reverseSortedPositions) {
				listdata.remove(i);
				listAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
			// startActivity(new Intent(getApplicationContext(),
			// TestActivity.class));

			Dream dream = db.getDreamTodo(listdata.get(position).getTodo());
			LatLng moveLatLng = new LatLng(dream.getLat(), dream.getLon());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

		}
	};

	/*
	 * ListViewSwipeGesture.TouchCallbacks swipeListener = new
	 * ListViewSwipeGesture.TouchCallbacks() {
	 * 
	 * @Override public void FullSwipeListView(int position) { // TODO
	 * Auto-generated method stub Toast.makeText(getApplicationContext(), "수정",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * @Override public void HalfSwipeListView(int position) { // TODO
	 * Auto-generated method stub Toast.makeText(getApplicationContext(), "삭제",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * @Override public void LoadDataForScroll(int count) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onDismiss(ListView listView, int[]
	 * reverseSortedPositions) { // TODO Auto-generated method stub
	 * Toast.makeText(getApplicationContext(), "Delete",
	 * Toast.LENGTH_SHORT).show(); for (int i : reverseSortedPositions) {
	 * listdata.remove(i); listAdapter.notifyDataSetChanged(); } }
	 * 
	 * @Override public void OnClickListView(int position) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * };
	 */

}
