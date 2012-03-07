package com.korovyansk.android.slideout;

import android.app.Activity;
import android.os.Bundle;

public class SlideoutActivity extends Activity {

	public static void prepare(Activity activity, int id, int width){
		SlideoutHelper.prepare(activity, id, width);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mSlideoutHelper.activate();
	    mSlideoutHelper.open();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mSlideoutHelper.close();
	}
	
	private SlideoutHelper mSlideoutHelper;
}
