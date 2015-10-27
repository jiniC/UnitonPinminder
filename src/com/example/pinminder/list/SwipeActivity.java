package com.example.pinminder.list;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pinminder.R;
import com.example.pinminder.SplashActivity;
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

public class SwipeActivity extends Activity {

	private ListView cmn_list_view;
	private ListAdapter listAdapter;
	private List<Dream> listdata;
	private ImageButton plusBtn;
	MyDB db;
	public static int splash = 0;

	public Map<Integer,Marker> markerList;
	private GoogleMap map;

	EditText editsearch;
	SearchView searchView;

	LinearLayout dummyLayer;
	private InputMethodManager imm;
	GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);

		db = new MyDB(getApplicationContext());
		testApi();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

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
		dummyLayer = (LinearLayout) findViewById(R.id.dummyLayout);
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
		
		/*
		 * gps check
		 */
		
		
		chkGpsService();
		
		

	}
	
	private boolean chkGpsService() {

		String gps = android.provider.Settings.Secure.getString(
				getContentResolver(),
				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		Log.d(gps, "aaaa");

		if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

			// GPS OFF 일때 Dialog 표시
			AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
			gsDialog.setTitle("위치 서비스 설정");
			gsDialog.setMessage("PIN Minder 알림을 받기 위해서는 내 위치 정보가 필요합니다.\n단말기의 설정에서 '위치 서비스' 사용을 허용해주세요.");
			gsDialog.setPositiveButton("설정하기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// GPS설정 화면으로 이동
							Intent intent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							startActivity(intent);
						}
					})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create().show();
			return false;

		} else {
			return true;
		}
	}

	public void initMap() {
		if (map != null) {

			gpsTracker = new GPSTracker(getApplicationContext());
			
			markerList = new HashMap<Integer,Marker>();
			
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
					Marker marker = map.addMarker(new MarkerOptions()
							.position(tempLatLng).title(dream.getTodo())
							.snippet(dream.getMemo())
							.icon(BitmapDescriptorFactory.fromResource(id)));
					
					markerList.put(dream.getId(),marker);
					
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
		
		db = new MyDB(getApplicationContext());
		InitializeValues();
		initMap();
		// listAdapter.notifyDataSetChanged();
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

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(
				R.id.action_search).getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
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
			
			Marker marker = markerList.get(dream.getId());
			marker.showInfoWindow();
			
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
	
	/*
	 * test api
	 */
	
	public void testApi(){
		String key = "kbOUead1jRb3%2BIJz3Z%2FFfYQQrTXxsxZhBxIhgIjeA3WXM83aAUGiPiUHefz3G7QObpRxaZnffelPT8oNMLcH1g%3D%3D";
		String serviceKey;
		String count = "20";
		
		db.deleteTable();
		
		try {
			serviceKey = URLEncoder.encode(key,"UTF-8");
		
		String url = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&areaCode=1"
				+ "&numOfRows=" + count
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		 
		
		Log.i("ohdoking",url);
		JsonObjectRequest jsonRequest = new JsonObjectRequest
		        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		                try {
		                	Log.i("ohdoking",response.toString());
		                    response = response.getJSONObject("response").getJSONObject("body").getJSONObject("items");
		                    JSONArray rowArray = response.getJSONArray("item");
		                    
		                    
		                    for(int i=0;i<rowArray.length();i++){
		                    	
	                            JSONObject jresponse = rowArray.getJSONObject(i);
	                            
	                            try{
	                            	jresponse.getString("mapx");
	                            }
	                            catch(Exception e){
	                            	continue;
	                            }
	                            String zone = "대한민국";
	                            String todo = jresponse.getString("title");
	                            double lat = Double.valueOf(jresponse.getString("mapy"));
	                            double lon = Double.valueOf(jresponse.getString("mapx"));
	                            
	                            String location = jresponse.getString("zipcode");
	                            String memo = "";
	                            String category = "음식";
	                            Integer noti = 1;
	                            
	                           
	                            
	                           /* Dream(Integer id, String zone, String todo, double lat, double lon,
	                			String location, String memo, String category, Integer check,
	                			Integer noti)*/
	                            
	                            Dream d = new Dream(0, zone, todo, lat, lon, location, memo, category, 0, noti);
	        					Log.d(zone, "zone");
	        					Log.d(location, "location");
	        					db.addDream(d);
			                }
//		                    System.out.println("Site: "+site+"\nNetwork: "+network);
		                    
		                    
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		Volley.newRequestQueue(this).add(jsonRequest);
		
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
	}
	
	String checkCategory(String c){
		if(c.equals("음식")){
			return "음식";
		}else if(c.equals("관람")){
			return "관림";
		}else if(c.equals("활동")){
			return "활동";
		}else if(c.equals("할 것")){
			return "할 것";
		}else if(c.equals("할 것")){
			return "운동";
		}
		return null;
	}

}
