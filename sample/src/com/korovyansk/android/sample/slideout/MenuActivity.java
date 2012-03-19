package com.korovyansk.android.sample.slideout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.korovyansk.android.slideout.SlideoutHelper;

public class MenuActivity extends FragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mSlideoutHelper = new SlideoutHelper(this, false);
	    mSlideoutHelper.activate();
	    getSupportFragmentManager().beginTransaction().add(com.korovyansk.android.slideout.R.id.slideout_placeholder, new MenuFragment(), "menu").commit();
	    mSlideoutHelper.open();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			mSlideoutHelper.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//mSlideoutHelper.close();
	}
	
	public SlideoutHelper getSlideoutHelper(){
		return mSlideoutHelper;
	}
	
	private SlideoutHelper mSlideoutHelper;

}
