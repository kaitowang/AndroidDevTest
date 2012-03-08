package com.sibevin.test.mixedtesting;

import android.app.Activity;
//import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
//import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ViewSwitcher.ViewFactory;
//import android.widget.Gallery;
//import android.widget.ImageSwitcher;
import android.widget.ImageView;
//import android.widget.RelativeLayout;
import android.widget.TextView;

public class OOBEActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.oobe_viewer);
		
		//Bundle bundle = this.getIntent().getExtras();
		//here: should use bundle to get the mode
		isOOBEmodeOn = true;//bundle.getBoolean(AppConst.OOBE_MODE, false);
		
		if (isOOBEmodeOn) {
			// show welcome and more help page
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
		} else {
			helpArr = new HelpPicStrPair[3];
			helpArr[0] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_tap_300x150),
				getResources().getDrawable(R.drawable.page_index_3_1st_200x48),
				R.string.help_tap);
			helpArr[1] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_hold_300x150),
				getResources().getDrawable(R.drawable.page_index_3_2nd_200x48),
				R.string.help_hold);
			helpArr[2] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_swipe_300x150),
				getResources().getDrawable(R.drawable.page_index_3_3rd_200x48),
				R.string.help_swipe);
		}

		/*
		Gallery oobeGallery = (Gallery) findViewById(R.id.oobeGallery);
		oobeGallery.setAdapter(new OOBEAdapter(this));
		*/

		nextBtn = (Button) findViewById(R.id.nextBtn);
		backBtn = (Button) findViewById(R.id.backBtn);

		//here: the ImageSwitcher cause the PageSwitcher wrong anim
		pageIndexSwitcher = (ImageSwitcher) findViewById(R.id.oobePageIndex);
		pageIndexSwitcher.setFactory(new ViewFactory() {
			public View makeView() {
				return new ImageView(OOBEActivity.this);
			}
		});
		Animation fadeInAnim = null;
		fadeInAnim = new AlphaAnimation(0.0f,1.0f);
		fadeInAnim.setInterpolator(new LinearInterpolator());
		fadeInAnim.setDuration(500);
		pageIndexSwitcher.setInAnimation(fadeInAnim);
		Animation fadeOutAnim = null;
		fadeOutAnim = new AlphaAnimation(1.0f,0.0f);
		fadeOutAnim.setInterpolator(new LinearInterpolator());
		fadeOutAnim.setDuration(500);
		pageIndexSwitcher.setOutAnimation(fadeOutAnim);

		oobePageSwitcher = (PageSwitcher) findViewById(R.id.oobeGallery);
		oobePageSwitcher.setAdapter(new OOBEAdapter(this));
		oobePageSwitcher.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//get the current page index
				currentPageIndex = arg2;
				setPageDisplay();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				currentPageIndex = 0;
			}

		});

		//ImageSwitcher oobePageIndex = (ImageSwitcher) findViewById(R.id.oobePageIndex);
		//RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//here
		//nextBtn.setLayoutParams(param);
		//backBtn.setLayoutParams(param);
		
	}

	public void onNextBtnClicked(View target) {
		gotoNextPage();
	}

	public void onBackBtnClicked(View target) {
		gotoPrevPage();
	}

	private void gotoNextPage() {
		//here: to use key event to handle swipe, the next view must be visible,
		//      i.e., the next view must appear on the page switcher and we
		//      need to setup width of page switcher and the side shadow
		oobePageSwitcher.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		//setPageDisplay();
	}

	private void gotoPrevPage() {
		oobePageSwitcher.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		//setPageDisplay();
	}

	private void setPageDisplay() {

		if (isOOBEmodeOn) {
			if (currentPageIndex == helpArr.length - 1) {
				// here: should go to entry page
				nextBtn.setVisibility(View.INVISIBLE);
				backBtn.setVisibility(View.VISIBLE);
			} else if (currentPageIndex == 0) {
				nextBtn.setVisibility(View.VISIBLE);
				backBtn.setVisibility(View.INVISIBLE);
			} else {
				nextBtn.setVisibility(View.VISIBLE);
				backBtn.setVisibility(View.VISIBLE);
			}
		}

		pageIndexSwitcher
				.setImageDrawable(helpArr[currentPageIndex].pageIndexPic);

	}

	private static class OOBEAdapter extends BaseAdapter {
		
		public OOBEAdapter(OOBEActivity initOOBEactivity) {
			oobeActivity = initOOBEactivity;
			inflater = LayoutInflater.from(initOOBEactivity);
		}

		public int getCount() {
			return oobeActivity.helpArr.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.help_page, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.helpPageText);
				holder.icon = (ImageView) convertView.findViewById(R.id.helpPageImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(oobeActivity.helpArr[position].strId);
			holder.icon.setImageBitmap(oobeActivity.helpArr[position].pic);
			return convertView;
		}
		
		static class ViewHolder {
			TextView text;
			ImageView icon;
		}
		
		private OOBEActivity oobeActivity;
		private LayoutInflater inflater;
		
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
	
	private Button nextBtn;
	private Button backBtn;
	private PageSwitcher oobePageSwitcher;
	private ImageSwitcher pageIndexSwitcher;
	private HelpPicStrPair[] helpArr;
	private int currentPageIndex;
	private boolean isOOBEmodeOn;
}
