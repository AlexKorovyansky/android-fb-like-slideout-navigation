package com.korovyansk.android.slideout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.korovyansk.android.slideout.R;
import com.korovyansk.android.slideout.utils.ScreenShot;

public class SlideoutActivity extends Activity {

	private static Bitmap sCoverBitmap = null;
	private static int sWidth = -1;
	public static void prepare(Activity activity, int id, int width){
		if(sCoverBitmap != null){
			sCoverBitmap.recycle();
		}
		final ScreenShot screenShot = new ScreenShot(activity, id);
		sCoverBitmap = screenShot.snap();
		sWidth = width;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.slideout);
	    initViews();
	    showSidebar();
	}

	@Override
	public void onBackPressed() {
		closeSidebar();
	}

	protected void initViews(){
		cover = (ImageView)findViewById(R.id.slidedout_cover);
		cover.setImageBitmap(sCoverBitmap);
		cover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeSidebar();
			}
		});
		int x = (int)(sWidth * 1.2f);
		@SuppressWarnings("deprecation")
		final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, x, 0);
		findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
	}
	
	protected void showSidebar(){
		int displayWidth = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		final int shift =  displayWidth - sWidth;
		final Animation translateCover = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, -shift, TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);
	    translateCover.setDuration(400);
	    translateCover.setFillAfter(true);
	    translateCover.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				cover.setAnimation(null);
				@SuppressWarnings("deprecation")
				final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, -shift, 0);
				cover.setLayoutParams(lp);
			}
	    });
	    cover.startAnimation(translateCover);
	}
	
	protected void closeSidebar(){
		int displayWidth = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		final int shift =  displayWidth - sWidth;
		final Animation translateCover = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, shift, TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);
	    translateCover.setDuration(400);
	    translateCover.setFillAfter(true);
	    translateCover.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				finish();
				overridePendingTransition(0, 0);
			}
		});
	    cover.startAnimation(translateCover);
	}
	
	private ImageView cover;
}
