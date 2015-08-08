package com.example.pinminder;

/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.pinminder.activities.SampleActivityBase;
import com.example.pinminder.db.MyDB;
import com.example.pinminder.dto.Dream;
import com.example.pinminder.write.PlaceAutocompleteAdapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WriteActivity extends SampleActivityBase
		implements OnClickListener, GoogleApiClient.OnConnectionFailedListener {

	private ImageButton cancelBtn, regionBtn, cat1, cat2, cat3, cat4, cat5, alarmBtn, memoBtn;
	Button deleteBtn, okBtn;
	private EditText todoEt, memoEt;
	LinearLayout r;

	private String zone, todo, location, memo, category;
	private float lat, lon;
	private int check, noti = 0;

	private int id;
	private int memoid = 0;

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

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));

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

		cat1 = (ImageButton) findViewById(R.id.cat1);
		cat2 = (ImageButton) findViewById(R.id.cat2);
		cat3 = (ImageButton) findViewById(R.id.cat3);
		cat4 = (ImageButton) findViewById(R.id.cat4);
		cat5 = (ImageButton) findViewById(R.id.cat5);

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

				todo = todoEt.getText().toString();
				memo = memoEt.getText().toString();

				MyDB my = new MyDB(getApplicationContext());
				// Dream d = new Dream(1, "test", "test2", 123.123,
				// 123.123,"test3", "test4", "test5", 1,0);
				Dream d = new Dream(0, zone, todo, lat, lon, location, memo, category, 0, noti);
				my.addDream(d);
				//Toast.makeText(getApplicationContext(), id + " " + zone + " " + " " + todo + " " + lat + " " + lon + " "
				//		+ " " + location + " " + memo + " " + check + " " + noti + " " + category, Toast.LENGTH_LONG)
				//		.show();

				finish();

			}
		});

		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		// private ImageButton
		// cancelBtn,regionBtn,cat1,cat2,cat3,cat4,cat5,alarmBtn,deleteBtn,okBtn,memoBtn;

		memoEt = (EditText) findViewById(R.id.memoEt);
		regionBtn = (ImageButton) findViewById(R.id.regionBtn);
		alarmBtn = (ImageButton) findViewById(R.id.alarmBtn);
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
		cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				todoEt.setText("");
			}
		});
		memoBtn = (ImageButton) findViewById(R.id.memoBtn);
		r = (LinearLayout) findViewById(R.id.r);
		memoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (memoid == 0) {
					r.setBackgroundColor(Color.WHITE);
					memoEt.setEnabled(false);

					memoBtn.setImageResource(R.drawable.icon_02);
					memoid = 1;
				} else {
					r.setBackgroundColor(Color.rgb(88, 128, 193));
					memoEt.setEnabled(true);

					memoBtn.setImageResource(R.drawable.icon_01);
					memoid = 0;
				}
			}
		});

		todoEt = (EditText) findViewById(R.id.todoEt);
		memoEt = (EditText) findViewById(R.id.memoEt);

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

			Toast.makeText(getApplicationContext(), "Clicked: " + item.description, Toast.LENGTH_SHORT).show();
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
			// Get the Place object from the buffer.
			final Place place = places.get(0);

			// Format details of the place for display and show it in a
			// TextView.
			/*
			 * mPlaceDetailsText.setText(formatPlaceDetails(getResources(),
			 * place.getName(), place.getId(), place.getAddress(),
			 * place.getPhoneNumber(), place.getWebsiteUri()));
			 * 
			 * // Display the third party attributions if set. final
			 * CharSequence thirdPartyAttribution = places.getAttributions(); if
			 * (thirdPartyAttribution == null) {
			 * mPlaceDetailsAttribution.setVisibility(View.GONE); } else {
			 * mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
			 * mPlaceDetailsAttribution.setText(Html.fromHtml(
			 * thirdPartyAttribution.toString())); }
			 */

			location = place.getAddress().toString();
			zone = location.substring(11, 14);

			String latLng = place.getLatLng().toString();
			String[] split = latLng.split(",");
			lat = Float.parseFloat(split[0].substring(10, split[0].length()));
			lon = Float.parseFloat(split[1].substring(0, split[1].length() - 1));

			Marker mark = map
					.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()));

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

			map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

			Log.i(TAG, "Place details received: " + place.getName());

			places.release();
		}
	};

	/*
	 * private static Spanned formatPlaceDetails(Resources res, CharSequence
	 * name, String id, CharSequence address, CharSequence phoneNumber, Uri
	 * websiteUri) { Log.e(TAG, res.getString(R.string.place_details, name, id,
	 * address, phoneNumber, websiteUri)); return
	 * Html.fromHtml(res.getString(R.string.place_details, name, id, address,
	 * phoneNumber, websiteUri));
	 * 
	 * }
	 */
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
	 * �ּҷκ��� ��ġ���� ��� address �ּ�
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
	 * ����
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

		Log.d("myLog", "�浵:" + lon); // ����/�浵 ��� ���

		return lon;
	}

	/*
	 * �浵
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

		Log.d("myLog", "����:" + lat);

		return lat;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cat1:
			category = "����";
			//Marker mark = map
			//		.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(location)).setIcon(R.drawable.mapicon1);
			

			cat1.setImageResource(R.drawable.writeicon1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.inactive5);
			break;
		case R.id.cat2:
			category = "����";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.writeicon2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.inactive5);
			break;
		case R.id.cat3:
			category = "Ȱ��";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.writeicon3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.inactive5);
			break;
		case R.id.cat4:
			category = "�� ��";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.writeicon4);
			cat5.setImageResource(R.drawable.inactive5);
			break;
		case R.id.cat5:
			category = "��Ÿ";
			cat1.setImageResource(R.drawable.inactive1);
			cat2.setImageResource(R.drawable.inactive2);
			cat3.setImageResource(R.drawable.inactive3);
			cat4.setImageResource(R.drawable.inactive4);
			cat5.setImageResource(R.drawable.writeicon5);
			break;
		}

	}

}