package com.sibevin.test.mixedtesting;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class AnimeTest extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anime_test_page);
		Animation anim = null;  
        anim = new RotateAnimation(0.0f,+360.0f);  
        anim.setInterpolator(new AccelerateDecelerateInterpolator());  
        anim.setDuration(3000);  
        findViewById(R.id.animeTarget).startAnimation(anim);  
	}
}
