package com.korovyansk.android.sample.slideout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

import com.korovyansk.android.slideout.SlideoutHelper;

public class Sample2Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample2);
		findViewById(R.id.sample_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
						SlideoutHelper.prepare(Sample2Activity.this, width);
						startActivity(new Intent(Sample2Activity.this,
								MenuActivity.class));
						overridePendingTransition(0, 0);
					}
				});
	}

}
