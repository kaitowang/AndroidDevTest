package com.sibevin.test.mixedtesting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EntryPageActivity extends ListActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_page);
		File sdRootDir = Environment.getExternalStorageDirectory();
		// File sdRootDir = Environment.getRootDirectory();
		pathRow = (LinearLayout) findViewById(R.id.pathRow);
		pathViewer = (HorizontalScrollView) findViewById(R.id.pathViewer);
		updateList(sdRootDir);
	}

	public void onPrevFolderBtnClick(View target) {
		if (currentDir.getParent() != null) {
			File prevPath = new File(currentDir.getParent());
			updateList(prevPath);
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		File selectedFile = (File) adapter.getItem(position);
		if (selectedFile.isDirectory() && selectedFile.canRead()) {
			updateList(selectedFile);
		}
	}

	private void updateList(File rootDir) {
		if (rootDir != null) {
			String[] pathList = rootDir.getAbsolutePath().split("/");
			pathRow.removeAllViews();
			if (pathList.length == 0) {
				TextView textView = new TextView(this);
				textView.setText("/");
				pathRow.addView(textView);
			} else {
				String linkPath = "/";
				for (int i = 0; i < pathList.length - 1; i++) {
					Button btn = new Button(this);
					String currentDir = pathList[i] + "/";
					btn.setText(currentDir);
					linkPath = linkPath + currentDir;
					btn.setTag(linkPath);
					btn.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							String linkPath = (String)v.getTag();
							File linkDir = new File(linkPath);
							updateList(linkDir);
						}
					});
					pathRow.addView(btn);
				}
				TextView textView = new TextView(this);
				textView.setText(pathList[pathList.length - 1] + "/");
				pathRow.addView(textView);
			}
			new Handler().postDelayed(new Runnable() {
				public void run() {
					pathViewer.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}
			}, 100L);
			adapter = new FileListAdapter(this, rootDir);
			currentDir = rootDir;
			setListAdapter(adapter);
		}
	}

	private static class FileListAdapter extends BaseAdapter {

		public FileListAdapter(Context context, File rootDir) {
			inflater = LayoutInflater.from(context);
			textFileIcon = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.text_file_icon);
			nonTextFileIcon = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.non_text_file_icon);
			dirIcon = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.dir_icon);
			initDirFileList(rootDir);
		}

		private void initDirFileList(File rootDir) {
			File[] files = rootDir.listFiles();
			List<File> dirList = new ArrayList<File>();
			List<File> fileList = new ArrayList<File>();
			for (File file : files) {
				if (file.isDirectory()) {
					dirList.add(file);
				} else {
					fileList.add(file);
				}
			}
			Collections.sort(dirList);
			Collections.sort(fileList);
			dirFileList = new ArrayList<File>(dirList);
			dirFileList.addAll(fileList);
		}

		public Object getItem(int position) {
			return dirFileList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public int getCount() {
			return dirFileList.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.icon_text_item, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView
						.findViewById(R.id.icon_view);
				holder.text = (TextView) convertView
						.findViewById(R.id.text_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			File file = (File) getItem(position);
			holder.text.setText(file.getName());
			if (file.isDirectory()) {
				holder.icon.setImageBitmap(dirIcon);
			} else if (AppUtil.isATextFile(file)) {
				holder.icon.setImageBitmap(textFileIcon);
			} else {
				holder.icon.setImageBitmap(nonTextFileIcon);
			}
			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView text;
		}

		private LayoutInflater inflater;
		private Bitmap textFileIcon;
		private Bitmap nonTextFileIcon;
		private Bitmap dirIcon;
		private List<File> dirFileList;
	}

	private FileListAdapter adapter;
	private LinearLayout pathRow;
	private HorizontalScrollView pathViewer;
	private File currentDir;
}
