package com.example.pinminder.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pinminder.R;
import com.example.pinminder.SplashActivity2;
import com.example.pinminder.db.MyDB;
import com.example.pinminder.dialog.CustomProgressDialog;
import com.example.pinminder.dto.Dream;

public class SeoulApiManager implements ApiManager {

	public MyDB db;
	public int finishApi = 0;
	private Context context;
	//0 : splash, 1 : setting
	public int where;
	
	CustomProgressDialog pDialog;
	
	public SeoulApiManager(Context context,int where) {
		this.context = context;
		this.where = where;
		 db = new MyDB(context);
	}
	@Override
	public void setApi() {

	}
	
	@Override
	public void getApi(){
		String key = "kbOUead1jRb3%2BIJz3Z%2FFfYQQrTXxsxZhBxIhgIjeA3WXM83aAUGiPiUHefz3G7QObpRxaZnffelPT8oNMLcH1g%3D%3D";
		String serviceKey;
		String count = "10";
		String type = "C";
		String type2 = "B";
		
		if(where == 1 ){
			
			pDialog = new CustomProgressDialog(context);
			pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			showpDialog();
			
		}
		db.deleteTable();
		
		try {
			serviceKey = URLEncoder.encode(key,"UTF-8");
		
		String urlTour = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&arrange=" + type2
				+ "&contentTypeId=12"
				+ "&areaCode=1"
				+ "&cat1=A02"
				+ "&cat2=A0205"
				+ "&numOfRows=" + count
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		String urlTour2 = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&arrange=" + type2
				+ "&contentTypeId=12"
				+ "&areaCode=1"
				+ "&cat1=A02"
				+ "&cat2=A0202"
				+ "&numOfRows=" + count
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		String urlTour3 = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&arrange=" + type2
				+ "&contentTypeId=12"
				+ "&areaCode=1"
				+ "&cat1=A02"
				+ "&cat2=A0201"
				+ "&numOfRows=" + count
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		
		String urlShow = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&arrange=" + type
				+ "&contentTypeId=15"
				+ "&areaCode=1"
				+ "&cat1=A02"
				+ "&cat2=A0207"
				+ "&numOfRows=30" 
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		
		String urlFestival = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&arrange=" + type
				+ "&contentTypeId=15"
				+ "&areaCode=1"
				+ "&cat1=A02"
				+ "&cat2=A0208"
				+ "&numOfRows=30" 
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		
		
		String urlFood = " http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
				+ "ServiceKey=" + key
				+ "&arrange=" + type2
				+ "&contentTypeId=39"
				+ "&areaCode=1"
				+ "&numOfRows=30"
				+ "&pageNo=1"
				+ "&MobileOS=AND"
				+ "&MobileApp=ohdoking"
				+ "&_type=json";
		
		JsonObjectRequest jsonRequestTour = new JsonObjectRequest
		        (Request.Method.GET, urlTour, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		            	inputApiResult(response);
		            	Log.i("ohdoking99",response.toString());
		            	finishApi = finishApi+ 1;
		            	
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		JsonObjectRequest jsonRequestTour2 = new JsonObjectRequest
		        (Request.Method.GET, urlTour2, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		            	inputApiResult(response);
		            	finishApi = finishApi+ 1;
		            	
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		JsonObjectRequest jsonRequestTour3 = new JsonObjectRequest
		        (Request.Method.GET, urlTour3, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		            	inputApiResult(response);
		            	finishApi = finishApi+ 1;
		            	
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		
		JsonObjectRequest jsonRequestFestival = new JsonObjectRequest
		        (Request.Method.GET, urlFestival, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		            	
		            	inputApiResult(response);
		            	finishApi = finishApi+ 1;
		            	
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		JsonObjectRequest jsonRequestShow = new JsonObjectRequest
		        (Request.Method.GET, urlShow, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		            	
		            	inputApiResult(response);
		            	finishApi = finishApi+ 1;
		            	
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		
		JsonObjectRequest jsonRequestFood = new JsonObjectRequest
		        (Request.Method.GET, urlFood, null, new Response.Listener<JSONObject>() {
		        	
		        	
		            @Override
		            public void onResponse(JSONObject response) {
		                // the response is already constructed as a JSONObject!
		            	inputApiResult(response);
		            	finishApi = finishApi+ 1;
		            }
		        }, new Response.ErrorListener() {
		 
		            @Override
		            public void onErrorResponse(VolleyError error) {
		                error.printStackTrace();
		            }
		        });
		
		
		Volley.newRequestQueue(context).add(jsonRequestFestival);
		Volley.newRequestQueue(context).add(jsonRequestShow);
		Volley.newRequestQueue(context).add(jsonRequestFood);
		Volley.newRequestQueue(context).add(jsonRequestTour);
		Volley.newRequestQueue(context).add(jsonRequestTour2);
		Volley.newRequestQueue(context).add(jsonRequestTour3);
		
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
	}
	
	
	void inputApiResult(JSONObject response){
		 try {
             response = response.getJSONObject("response").getJSONObject("body").getJSONObject("items");
             JSONArray rowArray = response.getJSONArray("item");
             
             Log.i("ohdokingtest",rowArray.getJSONObject(0).getString("cat2").toString());
             
             for(int i=0;i<rowArray.length();i++){
             	
                 JSONObject jresponse = rowArray.getJSONObject(i);
                 
                 try{
                 	jresponse.getString("mapx");
                 }
                 catch(Exception e){
                 	continue;
                 }
                 String zone = "대한민국";
                 String todo = new String(jresponse.getString("title").getBytes("8859_1"), Charset.forName("UTF-8"));
                 double lat = Double.valueOf(jresponse.getString("mapy"));
                 double lon = Double.valueOf(jresponse.getString("mapx"));
                 
                 String location = new String(jresponse.getString("addr1").getBytes("8859_1"), Charset.forName("UTF-8"));
                 
                 String memo = ""; 
                 if(!jresponse.has("tel")){
                	 memo = "";
                 }
                 else{
                	 
                	 memo = new String(jresponse.getString("tel").getBytes("8859_1"), Charset.forName("UTF-8"));
                 }
                 String category = checkCategory(jresponse.getString("cat2").toString());
                 Integer noti = 1;
                 
                
                 
                /* Dream(Integer id, String zone, String todo, double lat, double lon,
     			String location, String memo, String category, Integer check,
     			Integer noti)*/
                 
                 Dream d = new Dream(0, zone, todo, lat, lon, location, memo, category, 0, noti,0);
					db.addDream(d);
             }
//             System.out.println("Site: "+site+"\nNetwork: "+network);
             
             
         } catch (JSONException e) {
             e.printStackTrace();
         } catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
		 if(where == 1){
			 hidepDialog();
		 }

	}
	String checkCategory(String c){
		if(c.equals("A0502")){
			return "음식"; 
		}else if(c.equals("A0208")){
			return "관람"; //미술
		}
		else if(c.equals("A0207")){
			return "활동"; //축제
		}else if(c.equals("A0201") || c.equals("A0202") || c.equals("A0205")){
			return "할 것"; // 관광지 
		}
		else{
			return "기타";
			
		}
	}
	
	@Override
	public int getFinishApi() {
		return finishApi;
	}
	
	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

}
