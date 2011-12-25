package com.sibevin.test.mixedtesting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;

public class EntryPageActivity extends ListActivity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_page);
        init();
        File sdRootDir = Environment.getExternalStorageDirectory();
        getFileList(sdRootDir);
    }

	private void init() {
		fileList = new ArrayList<String>();
	}

	private void getFileList(File rootDir) {
		fileList.clear();
		if(rootDir != null) {
        	File[] files = rootDir.listFiles();
        	for(File file : files) {
        		fileList.add(file.getName());
        	}
        }
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fileList));
		this.getListView().setTextFilterEnabled(true);
	}

	private List<String> fileList;
}
