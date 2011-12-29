package com.sibevin.test.mixedtesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Test001MixedTestingActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onGoBtnClick(View target) {
    	startActivity(new Intent(Test001MixedTestingActivity.this,EntryPageActivity.class));
    }
    
    public void onGoLayoutWeightBtnClick(View target) {
    	startActivity(new Intent(Test001MixedTestingActivity.this,LayoutWeightActivity.class));
    }
}