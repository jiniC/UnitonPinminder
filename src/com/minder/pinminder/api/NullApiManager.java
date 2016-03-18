package com.minder.pinminder.api;

import android.content.Context;

import com.minder.pinminder.db.MyDB;

public class NullApiManager implements ApiManager {

	
	@Override
	public void getApi() {
	}

	@Override
	public void setApi() {

	}

	@Override
	public int getFinishApi() {
		return 6;
	}

}
