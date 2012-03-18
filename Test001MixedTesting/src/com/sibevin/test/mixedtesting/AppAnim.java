package com.sibevin.test.mixedtesting;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

public class AppAnim {
	public static enum Type {
		LFI, // Left Fade In
		LFO, // Left Fade Out
		RFI, // Right Fade In
		RFO, // Right Fade Out
		FI, //Fade In
		FO, //Fade Out
		MU, // Move Up
		MD // Move Down
	}

	public static AnimationSet create(Type type, int duration) {
		AnimationSet anim = new AnimationSet(true);
		int moveType = Animation.RELATIVE_TO_PARENT;
		MoveDis moveDis = new MoveDis();
		FadeType fadeType = FadeType.IN;
		switch (type) {
		case LFO:
			moveDis.toX = -1.0f;
			fadeType = FadeType.OUT;
			break;
		case RFI:
			moveDis.fromX = -1.0f;
			break;
		case RFO:
			moveDis.toX  = 1.0f;
			fadeType = FadeType.OUT;
			break;
		case FI:
			moveDis.isTranEnabled = false;
		case FO:
			moveDis.isTranEnabled = false;
			fadeType = FadeType.OUT;
		case MU:
			moveType = Animation.RELATIVE_TO_SELF;
			moveDis.fromY = -1.0f;
			fadeType = FadeType.NONE;
		case MD:
			moveType = Animation.RELATIVE_TO_SELF;
			moveDis.toY = -1.0f;
			fadeType = FadeType.NONE;
		case LFI:
		default:
			moveDis.fromX = 1.0f;
		}
		if(moveDis.isTranEnabled) {
			anim.addAnimation(new TranslateAnimation(
					moveType, moveDis.fromX, moveType, moveDis.toX,
					moveType, moveDis.fromY, moveType, moveDis.toY));
		}
		switch(fadeType) {
		case IN:
			anim.addAnimation(new AlphaAnimation(0, 1));
			break;
		case OUT:
			anim.addAnimation(new AlphaAnimation(1, 0));
			break;
		case NONE:
		default:
			// do nothing
		}
		anim.setInterpolator(new DecelerateInterpolator());
		anim.setDuration(duration);
		return anim;
	}
	
	private static class MoveDis {
		public MoveDis() {
			isTranEnabled = true;
			fromX = 0;
			toX = 0;
			fromY =0;
			toY = 0;
		}
		public boolean isTranEnabled;
		public float fromX;
		public float toX;
		public float fromY;
		public float toY;
	}
	
	private static enum FadeType {
		IN,
		OUT,
		NONE
	}
}
