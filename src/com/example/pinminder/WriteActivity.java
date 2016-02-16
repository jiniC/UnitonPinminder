package com.example.pinminder;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.pinminder.activities.SampleActivityBase;
import com.example.pinminder.db.MyDB;
import com.example.pinminder.dialog.DialogActivity;
import com.example.pinminder.dto.Dream;
import com.example.pinminder.write.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class WriteActivity extends SampleActivityBase
		implements OnClickListener, OnMapClickListener, GoogleApiClient.OnConnectionFailedListener, AnimationListener {

	private ImageButton cancelBtn, regionBtn, cat1, cat2, cat3, cat4, cat5, alarmBtn, memoBtn;
	Button deleteBtn, okBtn;
	private EditText todoEt, memoEt;
	LinearLayout r;

	ScrollView mainScrollView;
	ImageView transparentImageView;

	private String zone, todo, location, memo, category;
	private double lat, lon;
	private int check, noti = 0;

	private int id;
	private int memoid = 0;

	Marker mark;

	int code;
	String todoDB;
	int idDB;
	MyDB db;

	Animation animation, slideUp_animation;

	/**
	 * GoogleApiClient wraps our service connection to Google Play Services and
	 * provides access to the user's sign in state as well as the Google's APIs.
	 */
	protected GoogleApiClient mGoogleApiClient;

	private PlaceAutocompleteAdapter mAdapter;

	private AutoCompleteTextView mAutocompleteView;

	private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(new LatLng(-34.041458, 150.790100),
			new LatLng(-33.682247, 151.383362));

	static final LatLng SEOUL = new LatLng(37.56, 126.97);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using
		// AutoManage
		// functionality, which automatically sets up the API client to handle
		// Activity lifecycle
		// events. If your activity does not extend FragmentActivity, make sure
		// to call connect()
		// and disconnect() explicitly.
		mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0 /* clientId */, this)
				.addApi(Places.GEO_DATA_API).build();

		setContentView(R.layout.activity_write);

		Intent intent = getIntent();
		code = intent.getIntExtra("code", 0);
		todoDB = intent.getStringExtra("todo");
		idDB = intent.getIntExtra("id", -1);

		/*
		 * final ActionBar actionBar = getActionBar();
		 * actionBar.setBackgroundDrawable(new
		 * ColorDrawable(Color.parseColor("#5fc4d9"))); ActionBar.LayoutParams
		 * params = new
		 * ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.
		 * LayoutParams.WRAP_CONTENT, Gravity.CENTER );
		 * 
		 * actionBar.setIcon(R.drawable.logo3);
		 * actionBar.setDisplayShowTitleEnabled(false);
		 */
		LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.actionbar, null);
		
		ImageView write_logo = (ImageView)view.findViewById(R.id.write_logo);
		write_logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		getActionBar().setDisplayShowCustomEnabled(true);
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#ffffff")));
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_navi));
		getActionBar().setCustomView(view, params);

		db = new MyDB(getApplicationContext());

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);

		// Retrieve the AutoCompleteTextView that will display Place
		// suggestions.
		mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);

		// Register a listener that receives callbacks when a suggestion has
		// been selected
		mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

		// Set up the adapter that will retrieve suggestions from the Places Geo
		// Data API that cover
		// the entire world.
		mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient,
				BOUNDS_GREATER_SYDNEY, null);
		mAutocompleteView.setAdapter(mAdapter);

		// 초기화
		cat1 = (ImageButton) findViewById(R.id.cat1);
		cat2 = (ImageButton) findViewById(R.id.cat2);
		cat3 = (ImageButton) findViewById(R.id.cat3);
		cat4 = (ImageButton) findViewById(R.id.cat4);
		cat5 = (ImageButton) findViewById(R.id.cat5);
		okBtn = (Button) findViewById(R.id.okBtn);
		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		todoEt = (EditText) findViewById(R.id.todoEt);
		memoEt = (EditText) findViewById(R.id.memoEt);
		regionBtn = (ImageButton) findViewById(R.id.regionBtn);
		alarmBtn = (ImageButton) findViewById(R.id.alarmBtn);
		cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);
		memoBtn = (ImageButton) findViewById(R.id.memoBtn);
		r = (LinearLayout) findViewById(R.id.r);
		lat = 37.56;
		lon = 126.97;
		category = "";
		// 클릭리스너
		cat1.setOnClickListener(this);
		cat2.setOnClickListener(this);
		cat3.setOnClickListener(this);
		cat4.setOnClickListener(this);
		cat5.setOnClickListener(this);

		// 맵 클릭리스너
		map.setOnMapClickListener(this);

		animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
		slideUp_animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		// set animation listener
		animation.setAnimationListener(this);
		slideUp_animation.setAnimationListener(this);

		// 버튼 이름 설정
		if (code == 0) { // code=0 : 처음 등록할 때
			okBtn.setText("확인");
			deleteBtn.setText("취소");

		} else { // 수정할 때
			okBtn.setText(""); // 수정 글씨 없앰
			okBtn.setEnabled(false);
			deleteBtn.setText("삭제");

			BitmapDescriptor b = null;
			Dream dream = db.getDreamTodo(todoDB);

			LatLng moveLatLng = new LatLng(dream.getLat(), dream.getLon());
			lat = dream.getLat();
			lon = dream.getLon();

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

			todoEt.setText(dream.getTodo().toString());
			mAutocompleteView.setText(dream.getLocation().toString());

			String c = dream.getCategory().toString();
			category = c;
			if (c.equals("음식")) {
				cat1.setImageResource(R.drawable.writeicon1);
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon1);
			} else
				cat1.setImageResource(R.drawable.inactive1);

			if (c.equals("관람")) {
				cat1.setImageResource(R.drawable.writeicon2);
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon2);
			} else
				cat1.setImageResource(R.drawable.inactive2);

			if (c.equals("활동")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon3);
				cat1.setImageResource(R.drawable.writeicon3);
			} else
				cat1.setImageResource(R.drawable.inactive3);

			if (c.equals("할 것")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon4);
				cat1.setImageResource(R.drawable.writeicon4);
			} else
				cat1.setImageResource(R.drawable.inactive4);

			if (c.equals("기타")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon5);
				cat1.setImageResource(R.drawable.writeicon5);
			} else
				cat1.setImageResource(R.drawable.inactive5);

			if (b != null)
				mark = map.addMarker(new MarkerOptions().position(moveLatLng).icon(b));

			if (dream.getNoti() == 1) {
				alarmBtn.setImageResource(R.drawable.check_none_select05);
			} else {
				alarmBtn.setImageResource(R.drawable.check_none_select04);
			}
			memoEt.setText(dream.getMemo());
		}

		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 버튼 색 바꾸기

				okBtn.setBackgroundColor(Color.parseColor("#ededed"));
				todo = todoEt.getText().toString();
				memo = memoEt.getText().toString();
				location = mAutocompleteView.getText().toString();
				String x = "";
				String todo_s = "";
				String location_s = "";
				String category_s = "";

				if (todo.getBytes().length <= 0) {
					todo_s = "'할 일' ";
				}
				if (location.getBytes().length <= 0) {
					location_s = "'지역' ";
				}
				if (category.getBytes().length <= 0) {
					category_s = "'카테고리' ";
				}

				if (todo.getBytes().length <= 0 || location.getBytes().length <= 0 || category.getBytes().length <= 0) {
					Toast.makeText(getApplicationContext(), todo_s + location_s + category_s + "을(를) 입력해 주세요.",
							Toast.LENGTH_LONG).show();
					okBtn.setBackgroundColor(Color.parseColor("#ffffff"));
				} else {
					okBtn.setBackgroundColor(Color.parseColor("#ededed"));
					if (code == 0) { // code=0 : 처음 등록할 때
						Dream d = new Dream(0, zone, todo, lat, lon, location, memo, category, 0, noti, 1);
						db.addDream(d);
					}
					finish();
				}
			}
		});

		deleteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteBtn.setBackgroundColor(Color.parseColor("#ededed"));

				if (code == 0) {
					showDialog();
				} else {
					showDeleteDialog();
					//Dream dream = db.getDreamTodo(todoDB);
					//db.deleteDream(dream);
				}
			}
		});

		alarmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (noti == 0) {
					alarmBtn.setImageResource(R.drawable.check_none_select05);
					noti = 1;
				} else {
					alarmBtn.setImageResource(R.drawable.check_none_select04);
					noti = 0;
				}

			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				todoEt.setText("");
			}
		});
		memoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (memoid == 0) {
					r.startAnimation(slideUp_animation);

					/* 변경하고 싶은 레이아웃의 파라미터 값을 가져 옴 */
					// LinearLayout.LayoutParams rControl =
					// (LinearLayout.LayoutParams) r.getLayoutParams();
					/* 해당 margin값 변경 */
					// rControl.height=0;
					r.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0));

					memoBtn.setImageResource(R.drawable.icon_02);
					memoid = 1;
				} else {
					// r.startAnimation(animation);
					// r.setLayoutParams(
					// new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					// LayoutParams.WRAP_CONTENT));

					startAnimation();
					memoBtn.setImageResource(R.drawable.icon_01);
					memoid = 0;
				}
			}
		});

		if (memoEt.isFocused()) {

			mainScrollView.post(new Runnable() {
				@Override
				public void run() {
					mainScrollView.fullScroll(View.FOCUS_DOWN);
				}

			});
		}

		regionBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mAutocompleteView.setText("");
			}
		});

		// MapFragment in ScrollView
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);

		memoEt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				view.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_UP:
					view.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				return false;
			}
		});

		transparentImageView = (ImageView) findViewById(R.id.transparent_image);
		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					mainScrollView.requestDisallowInterceptTouchEvent(true);
					// Disable touch on transparent view
					return false;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					mainScrollView.requestDisallowInterceptTouchEvent(false);
					return true;

				case MotionEvent.ACTION_MOVE:
					mainScrollView.requestDisallowInterceptTouchEvent(true);
					return false;

				default:
					return true;
				}
			}
		});

		// EditText Event
		// EditText 값 변경 이벤트 탐지
		TextWatcher watcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// 텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성.
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 텍스트가 변경될때마다 발생할 이벤트를 작성.
				if (todoEt.getText().length() > 0)
					cancelBtn.setVisibility(View.VISIBLE);
				else
					cancelBtn.setVisibility(View.INVISIBLE);

				if (mAutocompleteView.getText().toString().length() > 0)
					regionBtn.setVisibility(View.VISIBLE);
				else
					regionBtn.setVisibility(View.INVISIBLE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		};

		// 호출
		todoEt.addTextChangedListener(watcher);
		mAutocompleteView.addTextChangedListener(watcher);

		// 공유하기가 됬을때 읽는곳!!

		Intent intent2 = getIntent();
		String action = intent2.getAction();
		String type = intent2.getType();

		// 인텐트 정보가 있는 경우 실행
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent2.getStringExtra(Intent.EXTRA_TEXT); // 가져온
																				// 인텐트의
																				// 텍스트
																				// 정보

				if (isVaildUrl(sharedText) == true) {
					new ContentUrlTask(sharedText).execute(null, null, null);
				} else {
					memoEt.setText(sharedText);
				}
			}

		}

	}

	private void startAnimation() {

		r.startAnimation(animation);
		r.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		scrolldown();

	}

	private void scrolldown() {
		mainScrollView.post(new Runnable() {
			@Override
			public void run() {
				mainScrollView.fullScroll(View.FOCUS_DOWN);
			}

		});
	}

	/**
	 * Listener that handles selections from suggestions from the
	 * AutoCompleteTextView that displays Place suggestions. Gets the place id
	 * of the selected item and issues a request to the Places Geo Data API to
	 * retrieve more details about the place.
	 *
	 * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
	 *      String...)
	 */
	private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			map.clear();
			/*
			 * Retrieve the place ID of the selected item from the Adapter. The
			 * adapter stores each Place suggestion in a PlaceAutocomplete
			 * object from which we read the place ID.
			 */
			final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
			final String placeId = String.valueOf(item.placeId);
			Log.i(TAG, "Autocomplete item selected: " + item.description);

			/*
			 * Issue a request to the Places Geo Data API to retrieve a Place
			 * object with additional details about the place.
			 */
			PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
			placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

			Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
		}
	};

	/**
	 * Callback for results from a Places Geo Data API query that shows the
	 * first place result in the details view on screen.
	 */
	private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
		@Override
		public void onResult(PlaceBuffer places) {
			if (!places.getStatus().isSuccess()) {
				// Request did not complete successfully
				Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
				places.release();
				return;
			}

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.hideSoftInputFromWindow(regionBtn.getWindowToken(), 0);

			// Get the Place object from the buffer.
			final Place place = places.get(0);

			// Format details of the place for display and show it in a
			// location = place.getAddress().toString();
			location = mAutocompleteView.getText().toString();
			try {
				String[] split = location.split(" ");
				zone = split[2].toString();
				Log.i(zone, "hyunhye1");
			} catch (Exception e) {
				zone = "대한민국";
			}
			Log.i(zone, "hyunhye");

			String latLng = place.getLatLng().toString();
			String[] split = latLng.split(",");
			lat = Float.parseFloat(split[0].substring(10, split[0].length()));
			lon = Float.parseFloat(split[1].substring(0, split[1].length() - 1));

			BitmapDescriptor b;
			if (category.equals(CommonValue.writeCategorys[0])) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon1);
				mark = map.addMarker(
						new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()).icon(b));
			} else if (category.equals("관람")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon2);
				mark = map.addMarker(
						new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()).icon(b));
			} else if (category.equals("활동")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon3);
				mark = map.addMarker(
						new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()).icon(b));
			} else if (category.equals("할 것")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon4);
				mark = map.addMarker(
						new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()).icon(b));
			} else if (category.equals("기타")) {
				b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon5);
				mark = map.addMarker(
						new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()).icon(b));
			} else if (category.equals("")) {
				mark = map.addMarker(
						new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()));
			}
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
			map.animateCamera(CameraUpdateFactory.zoomTo(18), 1000, null);

			Log.i(TAG, "Place details received: " + place.getName());

			places.release();
		}
	};

	/**
	 * Called when the Activity could not connect to Google Play services and
	 * the auto manager could resolve the error automatically. In this case the
	 * API is not available and notify the user.
	 *
	 * @param connectionResult
	 *            can be inspected to determine the cause of the failure
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

		// TODO(Developer): Check error code and notify the user of error state
		// and resolution.
		Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 주소로부터 위치정보 취득 address 주소
	 */
	public static JSONObject getLocationInfo(String address) {

		HttpGet httpGet = new HttpGet(
				"http://maps.google.com/maps/api/geocode/json?address=" + address + "&ka&sensor=false");

		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

	/*
	 * 위도
	 */
	public static Double getGeoLon(JSONObject jsonObject) {

		Double lon = new Double(0);
		try {
			lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lng");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("myLog", "경도:" + lon); // 위도/경도 결과 출력

		return lon;
	}

	/*
	 * 경도
	 */
	public static Double getGeoLat(JSONObject jsonObject) {
		Double lat = new Double(0);

		try {

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lat");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("myLog", "위도:" + lat);

		return lat;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cat1:
			try {
				map.clear();
				mark.remove();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "지역을 설정해주세요.", Toast.LENGTH_LONG).show();
			} finally {
				category = "음식";
				BitmapDescriptor bmp = BitmapDescriptorFactory.fromResource(R.drawable.mapicon1);
				mark = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(location).icon(bmp));
				cat1.setImageResource(R.drawable.writeicon1);
				cat2.setImageResource(R.drawable.inactive2);
				cat3.setImageResource(R.drawable.inactive3);
				cat4.setImageResource(R.drawable.inactive4);
				cat5.setImageResource(R.drawable.inactive5);
			}
			break;
		case R.id.cat2:
			try {
				map.clear();
				mark.remove();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "지역을 설정해주세요.", Toast.LENGTH_LONG).show();
			} finally {
				category = "관람";
				BitmapDescriptor bmp2 = BitmapDescriptorFactory.fromResource(R.drawable.mapicon2);
				mark = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(location).icon(bmp2));
				cat1.setImageResource(R.drawable.inactive1);
				cat2.setImageResource(R.drawable.writeicon2);
				cat3.setImageResource(R.drawable.inactive3);
				cat4.setImageResource(R.drawable.inactive4);
				cat5.setImageResource(R.drawable.inactive5);
			}
			break;

		case R.id.cat3:
			try {
				map.clear();
				mark.remove();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "지역을 설정해주세요.", Toast.LENGTH_LONG).show();
			} finally {
				category = "활동";
				BitmapDescriptor bmp3 = BitmapDescriptorFactory.fromResource(R.drawable.mapicon3);
				mark = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(location).icon(bmp3));
				cat1.setImageResource(R.drawable.inactive1);
				cat2.setImageResource(R.drawable.inactive2);
				cat3.setImageResource(R.drawable.writeicon3);
				cat4.setImageResource(R.drawable.inactive4);
				cat5.setImageResource(R.drawable.inactive5);
			}
			break;
		case R.id.cat4:
			try {
				map.clear();
				mark.remove();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "지역을 설정해주세요.", Toast.LENGTH_LONG).show();
			} finally {
				category = "할 것";
				BitmapDescriptor bmp4 = BitmapDescriptorFactory.fromResource(R.drawable.mapicon4);
				mark = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(location).icon(bmp4));
				cat1.setImageResource(R.drawable.inactive1);
				cat2.setImageResource(R.drawable.inactive2);
				cat3.setImageResource(R.drawable.inactive3);
				cat4.setImageResource(R.drawable.writeicon4);
				cat5.setImageResource(R.drawable.inactive5);
			}
			break;
		case R.id.cat5:
			try {
				map.clear();
				mark.remove();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "지역을 설정해주세요.", Toast.LENGTH_LONG).show();
			} finally {
				category = "기타";
				BitmapDescriptor bmp5 = BitmapDescriptorFactory.fromResource(R.drawable.mapicon5);
				mark = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(location).icon(bmp5));
				cat1.setImageResource(R.drawable.inactive1);
				cat2.setImageResource(R.drawable.inactive2);
				cat3.setImageResource(R.drawable.inactive3);
				cat4.setImageResource(R.drawable.inactive4);
				cat5.setImageResource(R.drawable.writeicon5);
			}
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.write, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		map.clear();
		lat = point.latitude;
		lon = point.longitude;
		// map.addMarker(new
		// MarkerOptions().position(point).title(point.toString()));

		BitmapDescriptor b;
		if (category.equals("음식")) {
			b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon1);
			mark = map.addMarker(new MarkerOptions().position(point).title(point.toString()).icon(b));
		} else if (category.equals("관람")) {
			b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon2);
			mark = map.addMarker(new MarkerOptions().position(point).title(point.toString()).icon(b));
		} else if (category.equals("활동")) {
			b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon3);
			mark = map.addMarker(new MarkerOptions().position(point).title(point.toString()).icon(b));
		} else if (category.equals("할 것")) {
			b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon4);
			mark = map.addMarker(new MarkerOptions().position(point).title(point.toString()).icon(b));
		} else if (category.equals("기타")) {
			b = BitmapDescriptorFactory.fromResource(R.drawable.mapicon5);
			mark = map.addMarker(new MarkerOptions().position(point).title(point.toString()).icon(b));
		} else if (category.equals("")) {
			mark = map.addMarker(new MarkerOptions().position(point).title(point.toString()));
		}

	}

	/**
	 * On selecting action bar icons
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.top_confirm:

			okBtn.setBackgroundColor(Color.parseColor("#ededed"));
			todo = todoEt.getText().toString();
			memo = memoEt.getText().toString();
			location = mAutocompleteView.getText().toString();
			String todo_s = "";
			String location_s = "";
			String category_s = "";
			if (todo.getBytes().length <= 0) {
				todo_s = "'할 일' ";
			}
			if (location.getBytes().length <= 0) {
				location_s = "'지역' ";
			}
			if (category.getBytes().length <= 0) {
				category_s = "'카테고리' ";
			}

			if (todo.getBytes().length <= 0 || location.getBytes().length <= 0 || category.getBytes().length <= 0) {
				Toast.makeText(getApplicationContext(), todo_s + location_s + category_s + "을(를) 입력해 주세요.",
						Toast.LENGTH_LONG).show();
			} else {
				Dream d = new Dream(idDB, zone, todo, lat, lon, location, memo, category, 0, noti, 1);
				Log.d(category, "cat");
				if (code == 0) {
					db.addDream(d);
				} else {
					db.updateDream(d);

				}
				finish();
			}

			// search action
			return true;
		case R.id.action_location_found:
			Intent i = new Intent(WriteActivity.this, DialogActivity.class);
			startActivity(i);
			// location found
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	// url check
	public boolean isVaildUrl(String url) {
		String reg = "^" +
				// protocol identifier
				"(?:(?:https?|ftp)://)" +
				// user:pass authentication
				"(?:\\S+(?::\\S*)?@)?" + "(?:" +
				// IP address exclusion
				// private & local networks
		"(?!(?:10|127)(?:\\.\\d{1,3}){3})" + "(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})"
				+ "(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})" +
				// IP address dotted notation octets
				// excludes loopback network 0.0.0.0
				// excludes reserved space >= 224.0.0.0
				// excludes network & broacast addresses
				// (first & last IP address of each class)
		"(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])" + "(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}"
				+ "(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))" + "|" +
				// host name
				"(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)" +
				// domain name
				"(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*" +
				// TLD identifier
				"(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))" + ")" +
				// port number
				"(?::\\d{2,5})?" +
				// resource path
				"(?:/?\\S*)?" + "$";

		return url.matches(reg);
	}

	private class ContentUrlTask extends AsyncTask<Void, Void, Void> {

		Document doc = null;
		Elements newsHeadlines;
		String url;

		public ContentUrlTask(String url) {
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				doc = Jsoup.connect(url).get();
				newsHeadlines = doc.select("title");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			todoEt.setText(newsHeadlines.get(0).text());
			memoEt.setText(url);

		}

	}
	
	@Override
    public void onBackPressed() {
		showDialog();
    }
	
	public void showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = getLayoutInflater();

		final View view = inflater.inflate(R.layout.activity_backpress_dialog,null);

		builder.setView(view);

		builder.setNegativeButton("유지",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		builder.create();
		builder.show();
	}
	public void showDeleteDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = getLayoutInflater();

		final View view = inflater.inflate(R.layout.activity_delete_dialog,null);

		builder.setView(view);

		builder.setNegativeButton("취소",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Dream dream = db.getDreamTodo(todoDB);
				db.deleteDream(dream);
				finish();
			}
		});

		builder.create();
		builder.show();
	}
}