package com.korovyansk.android.slideout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.korovyansk.android.slideout.utils.ScreenShot;

public class SlideoutHelper {

	private static Bitmap sCoverBitmap = null;
	private static int sWidth = -1;
	public static boolean isEnter;
	
	private boolean isBack = false;
	private int shadowWidth;

	public static void prepare(Activity activity, int width) {
		isEnter = true;
		if (sCoverBitmap != null) {
			sCoverBitmap.recycle();
		}
		final ScreenShot screenShot = new ScreenShot(activity);
		sCoverBitmap = screenShot.snap();
		sWidth = width;
	}

	public SlideoutHelper(Activity activity) {
		this(activity, false);
	}
	
	public SlideoutHelper(Activity activity, boolean reverse) {
		mActivity = activity;
		mReverse = reverse;
		shadowWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				(float) 20, activity.getResources().getDisplayMetrics());
	}

	public void activate() {
		mActivity.setContentView(R.layout.slideout);
		mCover = mActivity.findViewById(R.id.slidedout_cover_container);
		((ImageView) mCover.findViewById(R.id.slidedout_cover)).setImageBitmap(sCoverBitmap);
		mCover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isRunning(mStartAnimation) && !isRunning(mStopAnimation)) {
//					close();
				}
			}
		});
		mCover.setOnTouchListener(new MyOnTouchListener());
		int x = (int) (sWidth * 1.2f);
		int displayWidth = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		int width = displayWidth;// - sWidth;
		if (mReverse) {
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(width, LayoutParams.FILL_PARENT, x, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		} else{
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(width, LayoutParams.FILL_PARENT, 0, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		}
		initAnimations();
	}

	public void open() {
		mCover.startAnimation(mStartAnimation);
	}
	
	public void close() {
		close(false);
	}

	public void close(boolean isBack) {
		this.isBack = isBack;
		if (!isRunning(mStopAnimation) && !isEnter && !isRunning(mStartAnimation)) {
			mCover.startAnimation(mStopAnimation);
		}
	}
	
	public boolean isRunning(Animation animation){
		return !animation.hasEnded() && animation.hasStarted();
	}
	
	public void setSlideOutEndCallback(SlideOutEndCallback callback){
		this.mCallback = callback;
	}

	@SuppressWarnings("deprecation")
	protected void initAnimations() {
		final int displayWidth = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		final int shift = (mReverse ? -1 : 1) * (sWidth+shadowWidth - displayWidth);
		mCover.findViewById(R.id.slidedout_cover).setLayoutParams(new LinearLayout.LayoutParams(displayWidth, ViewGroup.LayoutParams.FILL_PARENT));
		final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(displayWidth+shadowWidth, LayoutParams.FILL_PARENT, -shadowWidth, 0);
		mCover.setLayoutParams(lp);
		mStartAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -shift+shadowWidth,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

//		mStartAnimation.setInterpolator(AnimationUtils.loadInterpolator(mActivity, android.R.anim.overshoot_interpolator));
		mStartAnimation.setDuration(DURATION_MS);
		mStartAnimation.setFillAfter(true);
		mStartAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mCover.setAnimation(null);
				final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(displayWidth+shadowWidth, LayoutParams.FILL_PARENT, -shift, 0);
				mCover.setLayoutParams(lp);
				isEnter = false;
			}
		});
		initStopAnimation(0);
	}
	
	void initStopAnimation(int offset){
		final int displayWidth = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		final int shift = (mReverse ? -1 : 1) * (sWidth+shadowWidth - displayWidth);
		mStopAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, shift-shadowWidth-offset,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0 
				);
		mStopAnimation.setDuration(DURATION_MS);
		mStopAnimation.setFillAfter(true);
		mStopAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
//				mActivity.finish();
				isEnter = false;
				mActivity.overridePendingTransition(0, 0);
				if (mCallback != null) {
					mCallback.run(isBack);
				}
			}
		});
	}
	
	class MyOnTouchListener implements OnTouchListener{
		float mDownX;
		float mOffsetX;
		int mStartX;
		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			AbsoluteLayout.LayoutParams lp = (android.widget.AbsoluteLayout.LayoutParams) mCover.getLayoutParams();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = event.getX();
				mStartX = lp.x;
				mOffsetX = 0;
				Log.d("Slideout", "startX:"+mStartX);
				break;
			case MotionEvent.ACTION_MOVE:
				lp.x += event.getX() - mDownX;
				mOffsetX = lp.x - mStartX;
				if (lp.x > mStartX) {
					lp.x = mStartX;
				}
				mCover.setLayoutParams(lp);
				Log.d("Slideout", "moveX:"+event.getX());
				break;
			case MotionEvent.ACTION_UP:
				if (mOffsetX <= 0) {
					if (!isRunning(mStopAnimation)) {
						initStopAnimation(lp.x - mStartX);
						close(true);
					}
				}
				break;
			default:
				break;
			}
			return false;
		}
		
	}

	private static final int DURATION_MS = 400;
	private View mCover;
	private Activity mActivity;
	private boolean mReverse = false;
	private Animation mStartAnimation;
	private Animation mStopAnimation;
	private SlideOutEndCallback mCallback;
}
