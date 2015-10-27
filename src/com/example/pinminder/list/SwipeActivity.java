package com.example.pinminder.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.pinminder.R;
import com.example.pinminder.SplashActivity;
import com.example.pinminder.SplashActivity2;
import com.example.pinminder.WriteActivity;
import com.example.pinminder.db.MyDB;
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

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

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

	EditText editsearch;
	SearchView searchView;
	ImageView addtutorial;
	
	LinearLayout dummyLayer;
	private InputMethodManager imm;
	GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);

		db = new MyDB(getApplicationContext());
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		if (splash == 0) {

			startActivity(new Intent(this, SplashActivity2.class));
			splash++;
		}

		serviceStart();

/*		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#5fc4d9")));
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER );
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.drawable.logo3);*/
		LayoutInflater inflater = (LayoutInflater)getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.actionbar, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER );
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		getActionBar().setDisplayShowCustomEnabled(true);
		//getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		getActionBar().setCustomView(view, params);
		
		cmn_list_view = (ListView) findViewById(R.id.cmn_list_view);
		dummyLayer = (LinearLayout) findViewById(R.id.dummyLayout);
		listdata = new ArrayList<Dream>();
		InitializeValues();
		final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
				cmn_list_view, swipeListener, this);
		touchListener.SwipeType = ListViewSwipeGesture.Double; // Set two
																// options at
																// background of
																// list item
		
		/*****************list 아이템이 하나도 없는 경우 추가 튜토리얼 보이기********************/
		addtutorial = (ImageView) findViewById(R.id.addtutorial);
		if(cmn_list_view.getCount()==0){
			addtutorial.setVisibility(View.VISIBLE);
		}
		else{
			addtutorial.setVisibility(View.INVISIBLE);
		}
		
		cmn_list_view.setOnTouchListener(touchListener);

		plusBtn = (ImageButton) findViewById(R.id.todolist_addbtn);
		plusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SwipeActivity.this, WriteActivity.class);
				i.putExtra("code", 0);
				i.putExtra("pos", -1);
				i.putExtra("id", -1);
				startActivity(i);
			}
		});

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Move the camera instantly to hamburg with a zoom of 15.

		// Zoom in, animating the camera.

		// Locate the EditText in listview_main.xml

		/*
		 * EditText editText = new EditText(getApplicationContext());
		 * getActionBar().setCustomView(editText);
		 * 
		 * // Capture Text in EditText editsearch.addTextChangedListener(new
		 * TextWatcher() {
		 * 
		 * @Override public void afterTextChanged(Editable arg0) { // TODO
		 * Auto-generated method stub String text =
		 * editsearch.getText().toString().toLowerCase(Locale.getDefault());
		 * listAdapter.filter(text); }
		 * 
		 * @Override public void beforeTextChanged(CharSequence arg0, int arg1,
		 * int arg2, int arg3) { // TODO Auto-generated method stub }
		 * 
		 * @Override public void onTextChanged(CharSequence arg0, int arg1, int
		 * arg2, int arg3) { // TODO Auto-generated method stub } });
		 */

	}

	public void initMap() {
		if (map != null) {

			gpsTracker = new GPSTracker(getApplicationContext());

			if (!listdata.isEmpty()) {
				map.clear();
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
			if (location != null) {

				LatLng moveLatLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				map.moveCamera(CameraUpdateFactory
						.newLatLngZoom(moveLatLng, 10));
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			}

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

		/*listdata = db.getAllDreams();
		listAdapter = new ListAdapter(this, listdata);
		cmn_list_view.setAdapter(listAdapter);*/
		
		Set<String> hashset = getPreferences();
		ArrayList<String> list = new ArrayList<String>(hashset);
        
		listdata.clear();
		listdata.addAll(db.getDreamCate(list));
		listAdapter = new ListAdapter(this, listdata);
		cmn_list_view.setAdapter(listAdapter);
		
		
		
		
	}
	
	private Set getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        Set<String> hash = new HashSet<String>();
        hash.add("관람");
        hash.add("할 것");
        hash.add("음식");
        hash.add("기타");
        hash.add("활동");
        
        return pref.getStringSet("categoryList", hash);
    }

	public void serviceStart() {
		Intent i = new Intent(SwipeActivity.this, PushEvent.class);
		startService(i);
	}

	@Override
	public void onResume() {
		super.onRestart();
		
		/*****************list 아이템이 하나도 없는 경우 추가 튜토리얼 보이기********************/
		addtutorial = (ImageView) findViewById(R.id.addtutorial);
		if(cmn_list_view.getCount()==0){
			addtutorial.setVisibility(View.VISIBLE);
		}
		else{
			addtutorial.setVisibility(View.INVISIBLE);
		}
		
		db = new MyDB(getApplicationContext());
		InitializeValues();
		initMap();
		// listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRestart() {
		super.onRestart();

		/*****************list 아이템이 하나도 없는 경우 추가 튜토리얼 보이기********************/
		addtutorial = (ImageView) findViewById(R.id.addtutorial);
		if(cmn_list_view.getCount()==0){
			addtutorial.setVisibility(View.VISIBLE);
		}
		else{
			addtutorial.setVisibility(View.INVISIBLE);
		}
		
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

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(true);

		SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				
				dummyLayer.setVisibility(View.GONE);
				listAdapter.filterData(newText);
				return true;
			}
			
			

			@Override
			public boolean onQueryTextSubmit(String query) {
				// this is your adapter that will be filtered
				dummyLayer.setVisibility(View.VISIBLE);
				Log.i("ohdokingQuery", query);
				listAdapter.filterData(query);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

				return true;
			}
		};
		
		SearchView.OnCloseListener cancel = new SearchView.OnCloseListener(){

			@Override
			public boolean onClose() {
				dummyLayer.setVisibility(View.VISIBLE);
				return false;
			}
			
		};
		searchView.setOnQueryTextListener(textChangeListener);
		searchView.setOnCloseListener(cancel);
		/*searchView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event != null
						&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					dummyLayer.setVisibility(View.VISIBLE);

				}
				return onKey(v, keyCode, event);
			}
		});*/

				
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		/*
		 * case R.id.action_search: Intent i2 = new Intent(SwipeActivity.this,
		 * DeleteActivity.class); startActivity(i2); // search action return
		 * true;
		 */
		case R.id.action_location_found:

			Intent i = new Intent(SwipeActivity.this, DialogActivity.class);
			startActivityForResult(i, 1);
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/*
	 * 뒤로 가기
	 */
	@Override
	public void onBackPressed() {
		if (!searchView.isIconified()) {
			searchView.onActionViewCollapsed();
	        dummyLayer.setVisibility(View.VISIBLE);
	    } else {
	        super.onBackPressed();
	    }
	}

	/*
	 * 수정 , 삭제, 리스트 클릭시에 지도 이동
	 */
	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void FullSwipeListView(int position) {
			// 수정하기
			// TODO Auto-generated method stub
			Intent i = new Intent(SwipeActivity.this, WriteActivity.class);
			i.putExtra("code", 1);

			Dream dream = db.getDreamTodo(listdata.get(position).getTodo());
			i.putExtra("todo", listdata.get(position).getTodo());
			i.putExtra("id", dream.getId());

			Log.d(dream.getId() + "", "id");
			startActivity(i);
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
			dummyLayer.setVisibility(View.VISIBLE);
			imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
			Dream dream = db.getDreamId(listdata.get(position).getId());
			LatLng moveLatLng = new LatLng(dream.getLat(), dream.getLon());
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

		}
	};
	
	/*
	 * 다이얼로그 필터 처리
	 */
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (1) : { 
	      if (resultCode == Activity.RESULT_OK) { 
	      ArrayList<String> newText = data.getStringArrayListExtra("filter");
	      
		  	listdata.clear();
			listdata.addAll(db.getDreamCate(newText));
			listAdapter.notifyDataSetChanged();
	      } 
	      break; 
	    } 
	  } 
	}

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
