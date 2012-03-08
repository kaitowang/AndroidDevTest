package com.sibevin.test.mixedtesting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class PageSwitcher extends Gallery {
	
	public PageSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// force to swipe one page each time
		return false;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// add the gesture sensitivity
		final int MAX_MOVE_DIS = 50;
		float realMoveDis = distanceX*5;
		if(realMoveDis > MAX_MOVE_DIS) {
			realMoveDis = MAX_MOVE_DIS;
		} else if(realMoveDis < -MAX_MOVE_DIS){
			realMoveDis = -MAX_MOVE_DIS;
		}
		return super.onScroll(e1, e2, realMoveDis, distanceY);
	}

}
