package com.sibevin.test.mixedtesting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class OOBEViewer3 extends Activity implements OnGestureListener,
		OnTouchListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oobe_viewer03);

		// init OOBE pages
		helpArr = new HelpPicStrPair[5];
		helpArr[0] = new HelpPicStrPair(
				BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_300xx),
				BitmapFactory.decodeResource(getResources(), R.drawable.page_index_5_1st_200x48),
				R.string.app_name);
		helpArr[1] = new HelpPicStrPair(
				BitmapFactory.decodeResource(getResources(), R.drawable.oobe_tap_300x150),
				BitmapFactory.decodeResource(getResources(), R.drawable.page_index_5_2nd_200x48),
				R.string.help_tap);
		helpArr[2] = new HelpPicStrPair(
				BitmapFactory.decodeResource(getResources(), R.drawable.oobe_hold_300x150),
				BitmapFactory.decodeResource(getResources(), R.drawable.page_index_5_3rd_200x48),
				R.string.help_hold);
		helpArr[3] = new HelpPicStrPair(
				BitmapFactory.decodeResource(getResources(), R.drawable.oobe_swipe_300x150),
				BitmapFactory.decodeResource(getResources(), R.drawable.page_index_5_4th_200x48),
				R.string.help_swipe);
		helpArr[4] = new HelpPicStrPair(
				BitmapFactory.decodeResource(getResources(), R.drawable.oobe_morehelp_300x150),
				BitmapFactory.decodeResource(getResources(), R.drawable.page_index_5_5th_200x48), 
				R.string.help_more);

		gestureDetector = new GestureDetector(this);

		// add OOBE pages to PageFlipper
		oobePageFlipper = (ViewFlipper) findViewById(R.id.oobePageFlipper);
		pageIndexSwitcher = (ViewFlipper) findViewById(R.id.oobePageIndexSwitcher);
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < helpArr.length; i++) {
			LinearLayout helpView = (LinearLayout) inflater.inflate(R.layout.help_page, null);
			TextView text = (TextView) helpView.findViewById(R.id.helpPageText);
			ImageView icon = (ImageView) helpView
					.findViewById(R.id.helpPageImage);
			text.setText(helpArr[i].strId);
			icon.setImageBitmap(helpArr[i].pic);

			// FIXME: cannot vertical center the children in ViewFlipper
			/*
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.CLIP_HORIZONTAL;
			helpView.setLayoutParams(params);
			*/
			
			oobePageFlipper.addView(helpView);
			ImageView index = new ImageView(this);
			index.setImageBitmap(helpArr[i].pageIndexPic);
			pageIndexSwitcher.addView(index);
		}

		// setup touch listener
		oobePageFlipper.setOnTouchListener(this);
		
		// init animation listener
		animListener = new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				isAnimRunning = true;
			}

			public void onAnimationEnd(Animation animation) {
				isAnimRunning = false;
			}

			public void onAnimationRepeat(Animation animation) {
				// Not used.
			}

		};

		// init flipper animation
		isMoveToLeft = true;
		AnimationSet inAnim = AppAnim.create(AppAnim.Type.LFI,
				OOBE_FLING_ANIME_TIME);
		AnimationSet outAnim = AppAnim.create(AppAnim.Type.LFO,
				OOBE_FLING_ANIME_TIME);
		inAnim.setAnimationListener(animListener);
		outAnim.setAnimationListener(animListener);
		oobePageFlipper.setInAnimation(inAnim);
		oobePageFlipper.setOutAnimation(outAnim);
		
		//init page index switcher animation
		AnimationSet fadeInAnim = AppAnim.create(AppAnim.Type.FI, OOBE_FLING_ANIME_TIME);
		AnimationSet fadeOutAnim = AppAnim.create(AppAnim.Type.FO, OOBE_FLING_ANIME_TIME);
		pageIndexSwitcher.setInAnimation(fadeInAnim);
		pageIndexSwitcher.setInAnimation(fadeOutAnim);
		
	}

	// ---Implement OnGestureListener---

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > e2.getX()) {
			moveToNext();
		} else if (e1.getX() < e2.getX()) {
			moveToPrevious();
		} else {
			return false;
		}
		return true;
	}

	public boolean onDown(MotionEvent e) {
		// Not used
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// Not used
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// Not used.
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// Not used.
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// Not used.
		return false;
	}

	// ---Implement OnTouchListener---

	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	// ---Implement UI Button onClick---

	public void onNextBtnClicked(View target) {
		moveToNext();
	}

	public void onBackBtnClicked(View target) {
		moveToPrevious();
	}

	// ---Private functions---

	private void moveToNext() {
		if (isAnimRunning) {
			return;
		}
		if (!isMoveToLeft) {

			AnimationSet inAnim = AppAnim.create(AppAnim.Type.LFI,
					OOBE_FLING_ANIME_TIME);
			AnimationSet outAnim = AppAnim.create(AppAnim.Type.LFO,
					OOBE_FLING_ANIME_TIME);
			inAnim.setAnimationListener(animListener);
			outAnim.setAnimationListener(animListener);
			oobePageFlipper.setInAnimation(inAnim);
			oobePageFlipper.setOutAnimation(outAnim);
			isMoveToLeft = true;
		}
		oobePageFlipper.showNext();
		pageIndexSwitcher.showNext();
		oobePageFlipper.refreshDrawableState();
	}

	private void moveToPrevious() {
		if (isAnimRunning) {
			return;
		}
		if (isMoveToLeft) {
			AnimationSet inAnim = AppAnim.create(AppAnim.Type.RFI,
					OOBE_FLING_ANIME_TIME);
			AnimationSet outAnim = AppAnim.create(AppAnim.Type.RFO,
					OOBE_FLING_ANIME_TIME);
			inAnim.setAnimationListener(animListener);
			outAnim.setAnimationListener(animListener);
			oobePageFlipper.setInAnimation(inAnim);
			oobePageFlipper.setOutAnimation(outAnim);
			isMoveToLeft = false;
		}
		oobePageFlipper.showPrevious();
		pageIndexSwitcher.showPrevious();
		oobePageFlipper.refreshDrawableState();
	}

	public class HelpPicStrPair {
		public HelpPicStrPair(Bitmap initPic, Bitmap initPageIndexPic,
				int initStrId) {
			pic = initPic;
			pageIndexPic = initPageIndexPic;
			strId = initStrId;
		}

		public Bitmap pic;
		public Bitmap pageIndexPic;
		public int strId;
	}

	private HelpPicStrPair[] helpArr;
	private ViewFlipper oobePageFlipper;
	private GestureDetector gestureDetector;
	private AnimationListener animListener;
	private ViewFlipper pageIndexSwitcher;
	private boolean isMoveToLeft;
	private boolean isAnimRunning;

	private final int OOBE_FLING_ANIME_TIME = 300;

}
