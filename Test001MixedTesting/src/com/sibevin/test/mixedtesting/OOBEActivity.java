package com.sibevin.test.mixedtesting;

import android.app.Activity;
//import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
//import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
//import android.widget.Button;
import android.widget.Gallery;
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
		boolean isOOBEmodeOn = true;//bundle.getBoolean(AppConst.OOBE_MODE, false);
		
		if (isOOBEmodeOn) {
			// show welcome and more help page
			helpArr = new HelpPicStrPair[5];
			helpArr[0] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon_300xx),
				R.string.app_name);
			helpArr[1] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_tap_300x150),
				R.string.help_tap);
			helpArr[2] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_hold_300x150),
				R.string.help_hold);
			helpArr[3] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_swipe_300x150),
				R.string.help_swipe);
			helpArr[4] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_morehelp_300x150),
				R.string.help_more);
		} else {
			helpArr = new HelpPicStrPair[3];
			helpArr[0] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_tap_300x150),
				R.string.help_tap);
			helpArr[1] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_hold_300x150),
				R.string.help_hold);
			helpArr[2] = new HelpPicStrPair(
				BitmapFactory.decodeResource(this.getResources(), R.drawable.oobe_swipe_300x150),
				R.string.help_swipe);
		}
		
		//Button nextBtn = (Button) findViewById(R.id.nextBtn);
		//Button backBtn = (Button) findViewById(R.id.backBtn);

		Gallery oobeGallery = (Gallery) findViewById(R.id.oobeGallery);
		oobeGallery.setAdapter(new OOBEAdapter(this));
		//ImageSwitcher oobePageIndex = (ImageSwitcher) findViewById(R.id.oobePageIndex);
		//RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		//here
		//nextBtn.setLayoutParams(param);
		//backBtn.setLayoutParams(param);
		
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
		public HelpPicStrPair(Bitmap initPic, int initStrId) {
			pic = initPic;
			strId = initStrId;
		}
		public Bitmap pic;
		public int strId;
	}
	
	private HelpPicStrPair[] helpArr;
}
