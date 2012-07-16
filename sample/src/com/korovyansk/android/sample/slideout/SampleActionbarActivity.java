package com.korovyansk.android.sample.slideout;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;

import com.korovyansk.android.slideout.SlideoutActivity;

public class SampleActionbarActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
	    	finish();
	    }
	    setContentView(R.layout.sample_actionbar);
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
			SlideoutActivity.prepare(SampleActionbarActivity.this, R.id.inner_content, width);
			startActivity(new Intent(SampleActionbarActivity.this, MenuActivity.class));
			overridePendingTransition(0, 0);
		}
		return true;
	}
	
}
