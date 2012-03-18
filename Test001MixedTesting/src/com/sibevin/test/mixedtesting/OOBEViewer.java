package com.sibevin.test.mixedtesting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class OOBEViewer extends FullScreenActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oobe_viewer02);
		
		helpArr = new HelpPicStrPair[5];
		helpArr[0] = new HelpPicStrPair(
			BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon_300xx),
			getResources().getDrawable(R.drawable.page_index_5_1st_200x48),
			R.string.app_name);
		helpArr[1] = new HelpPicStrPair(
			BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_tap_300x150),
			getResources().getDrawable(R.drawable.page_index_5_2nd_200x48),
			R.string.help_tap);
		helpArr[2] = new HelpPicStrPair(
			BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_hold_300x150),
			getResources().getDrawable(R.drawable.page_index_5_3rd_200x48),
			R.string.help_hold);
		helpArr[3] = new HelpPicStrPair(
			BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_swipe_300x150),
			getResources().getDrawable(R.drawable.page_index_5_4th_200x48),
			R.string.help_swipe);
		helpArr[4] = new HelpPicStrPair(
			BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_morehelp_300x150),
			getResources().getDrawable(R.drawable.page_index_5_5th_200x48),
			R.string.help_more);
		
		oobePageSwitcher = (HorizontalScrollView) findViewById(R.id.oobePageSwitcher);
		
		/*
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		*/
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		movePageDis = metrics.widthPixels;

		LinearLayout oobePageRow = (LinearLayout) findViewById(R.id.oobePageRow);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < helpArr.length; i++) {
			View helpView = inflater.inflate(R.layout.help_page, null);
			TextView text = (TextView) helpView.findViewById(R.id.helpPageText);
			ImageView icon = (ImageView) helpView
					.findViewById(R.id.helpPageImage);
			text.setText(helpArr[i].strId);
			icon.setImageBitmap(helpArr[i].pic);
			oobePageRow.addView(helpView);
			//setup the child width same as the screen width
			LayoutParams helpLparams = helpView.getLayoutParams();
			helpLparams.width = movePageDis;
			helpView.setLayoutParams(helpLparams);
		}
		
		pageIndexSwitcher = (ImageSwitcher) findViewById(R.id.oobePageIndex);
		pageIndexSwitcher.setFactory(new ViewFactory() {
			public View makeView() {
				return new ImageView(OOBEViewer.this);
			}
		});
		
		currentIndex = 0;
		
	}

	public void onNextBtnClicked(View target) {
		goToNextPage();
	}

	public void onBackBtnClicked(View target) {
		goToBackPage();
	}
	
	private void goToNextPage() {
		if (currentIndex + 1 < helpArr.length) {
			goToPage(currentIndex + 1);
		}
	}
	
	private void goToBackPage() {
		if (currentIndex - 1 >= 0) {
			goToPage(currentIndex - 1);
		}
	}
	
	private void goToPage(int targetPageIndex) {
		currentIndex = targetPageIndex;
		oobePageSwitcher.smoothScrollTo(currentIndex*movePageDis, 0);
	}
	
	public class HelpPicStrPair {
		public HelpPicStrPair(Bitmap initPic, Drawable initPageIndexPic, int initStrId) {
			pic = initPic;
			pageIndexPic = initPageIndexPic;
			strId = initStrId;
		}
		public Bitmap pic;
		public Drawable pageIndexPic;
		public int strId;
	}
	
	private HorizontalScrollView oobePageSwitcher;
	private ImageSwitcher pageIndexSwitcher;
	private HelpPicStrPair[] helpArr;
	private int currentIndex;
	private int movePageDis;
}
