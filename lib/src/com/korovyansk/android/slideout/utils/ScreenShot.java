package com.korovyansk.android.slideout.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class ScreenShot {
	private final View view;
	boolean clipStatusBar;
	int statusBarHeight;

	/** Create snapshots based on the view and its children. */
	public ScreenShot(View root) {
		this.view = root;
	}

	/** Create snapshot handler that captures the root of the whole activity. */
	public ScreenShot(Activity activity) {
//		final View contentView = activity.findViewById(android.R.id.content);
//		this.view = contentView.getRootView();
		this.view = ((ViewGroup)activity.getWindow().getDecorView());
		clipStatusBar = true;
		Rect rectgle= new Rect();
		this.view.getWindowVisibleDisplayFrame(rectgle);
		this.statusBarHeight = rectgle.top;
	}

	/** Create snapshot handler that captures the view with target id of the activity. */
	public ScreenShot(Activity activity, int id) {
		this(activity.findViewById(id));
	}
	
	/** Take a snapshot of the view. */
	public Bitmap snap() {
		Bitmap bitmap = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		
		if (clipStatusBar) {
			return Bitmap.createBitmap(bitmap, 0, this.statusBarHeight, this.view.getWidth(), this.view.getHeight()-this.statusBarHeight, null, false);
		}
		return bitmap;
	}
}
