package com.korovyansk.android.slideout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class SlideoutHelper {

	private static Bitmap sCoverBitmap = null;
	private static int sWidth = -1;

	public static void prepare(Activity activity, int id, int width) {
		if (sCoverBitmap != null) {
			sCoverBitmap.recycle();
		}
		Rect rectgle = new Rect();
		Window window = activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int statusBarHeight = rectgle.top;

		ViewGroup v1 = (ViewGroup) activity.findViewById(id).getRootView();
		v1.setDrawingCacheEnabled(true);
		Bitmap source = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);
		if (statusBarHeight != 0) {
			sCoverBitmap = Bitmap.createBitmap(source, 0, statusBarHeight, source.getWidth(), source.getHeight() - statusBarHeight);
			source.recycle();
		} else {
			sCoverBitmap = source;
		}
		sWidth = width;
	}

	public SlideoutHelper(Activity activity) {
		this(activity, false);
	}
	
	public SlideoutHelper(Activity activity, boolean reverse) {
		mActivity = activity;
		mReverse = reverse;
	}

	public void activate() {
		mActivity.setContentView(R.layout.slideout);
		mCover = (ImageView) mActivity.findViewById(R.id.slidedout_cover);
		mCover.setImageBitmap(sCoverBitmap);
		mCover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
		int x = (int) (sWidth * 1.2f);
		if (mReverse) {
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, x, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		} else{
			@SuppressWarnings("deprecation")
			final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);
			mActivity.findViewById(R.id.slideout_placeholder).setLayoutParams(lp);
		}
		initAnimations();
	}

	public void open() {
		mCover.startAnimation(mStartAnimation);
	}

	public void close() {
		mCover.startAnimation(mStopAnimation);
	}

	protected void initAnimations() {
		int displayWidth = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		final int shift = (mReverse ? -1 : 1) * (sWidth - displayWidth);
		mStartAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -shift,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);

		mStopAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, shift,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);
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
				@SuppressWarnings("deprecation")
				final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, -shift, 0);
				mCover.setLayoutParams(lp);
			}
		});

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
				mActivity.finish();
				mActivity.overridePendingTransition(0, 0);
			}
		});
	}

	private static final int DURATION_MS = 400;
	private ImageView mCover;
	private Activity mActivity;
	private boolean mReverse = false;
	private Animation mStartAnimation;
	private Animation mStopAnimation;
}
