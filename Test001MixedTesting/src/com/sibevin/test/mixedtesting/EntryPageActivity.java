package com.sibevin.test.mixedtesting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sibevin.test.mixedtesting.CommandButton.CommandType;

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
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EntryPageActivity extends ListActivity implements FileExploreBehavior {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_page);
		File sdRootDir = Environment.getExternalStorageDirectory();
		pathRow = (LinearLayout) findViewById(R.id.pathRow);
		pathViewer = (HorizontalScrollView) findViewById(R.id.pathViewer);
		updateList(sdRootDir);
		initMenu();
	}

	public void onPrevFolderBtnClick(View target) {
		if (currentDir.getParent() != null) {
			File prevPath = new File(currentDir.getParent());
			updateList(prevPath);
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		FileItem selectedItem = (FileItem) adapter.getItem(position);
		if (selectedItem.file.isDirectory() && selectedItem.file.canRead()) {
			updateList(selectedItem.file);
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

	private void initMenu() {
		LinearLayout mainMenu = (LinearLayout) this.findViewById(R.id.mainMenu);
		CommandType[] commandList = {
				CommandType.COPY,
				CommandType.CUT,
				CommandType.DELETE,
				CommandType.PASTE,
				CommandType.RENAME};
		CommandHint hint = new CommandHint(this);
		for(CommandType type : commandList) {
			mainMenu.addView(CommandButton.createButton(type, this, this, hint));
		}
	}

	private class CommandHint implements FileExploreBehavior {

		public CommandHint(Context context) {
			parent = context;
		}

		public void copy() {
			showHint(CommandType.COPY);
		}

		public void cut() {
			showHint(CommandType.CUT);
		}

		public void delete() {
			showHint(CommandType.DELETE);
		}

		public void paste() {
			showHint(CommandType.PASTE);
		}

		public void rename() {
			showHint(CommandType.RENAME);
		}

		private void showHint(CommandType type) {
			Toast hint = Toast.makeText(parent, type.toString(), Toast.LENGTH_SHORT);
			hint.show();
		}

		Context parent;
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
			fileItemList = new ArrayList<FileItem>();
			for (File file : files) {
				if (file.isDirectory()) {
					dirList.add(file);
				} else {
					fileList.add(file);
				}
			}
			Collections.sort(dirList);
			Collections.sort(fileList);
			for(File file : dirList) {
				//TODO: use a global file list to store the checked results.
				fileItemList.add(new FileItem(file,false));
			}
			for(File file : fileList) {
				fileItemList.add(new FileItem(file,false));
			}
		}

		public Object getItem(int position) {
			return fileItemList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public int getCount() {
			return fileItemList.size();
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
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.checkbox_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			FileItem fileItem = (FileItem) getItem(position);
			holder.text.setText(fileItem.file.getName());
			if (fileItem.file.isDirectory()) {
				holder.icon.setImageBitmap(dirIcon);
			} else if (AppUtil.isATextFile(fileItem.file)) {
				holder.icon.setImageBitmap(textFileIcon);
			} else {
				holder.icon.setImageBitmap(nonTextFileIcon);
			}
			/*
			 * Because the checkbox is created dynamically, i.e. the status of
			 * a checkbox will not be stored. We store the checked info in the
			 * FileItem.
			 */
			holder.checkbox.setChecked(fileItem.checked);
			holder.checkbox.setTag(position);
			holder.checkbox.setFocusable(false);
			holder.checkbox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int position = (Integer)v.getTag();
					FileItem fileItem = fileItemList.get(position);
					if(fileItem.checked) {
						fileItem.checked = false;
					} else {
						fileItem.checked = true;
					}
				}
			});
			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView text;
			CheckBox checkbox;
		}

		private LayoutInflater inflater;
		private Bitmap textFileIcon;
		private Bitmap nonTextFileIcon;
		private Bitmap dirIcon;
		private List<FileItem> fileItemList;
	}

	static class FileItem
	{
		public FileItem(File inputFile, boolean inputChecked)
		{
			file = inputFile;
			checked = inputChecked;
		}
		public File file;
		public boolean checked;
	}

	public void copy() {
		Log.d("Menu","copy clicked.");
	}

	public void cut() {
		Log.d("Menu","cut clicked.");
	}

	public void delete() {
		Log.d("Menu","delete clicked.");
	}

	public void paste() {
		Log.d("Menu","paste clicked.");
	}

	public void rename() {
		Log.d("Menu","rename clicked.");
	}

	private FileListAdapter adapter;
	private LinearLayout pathRow;
	private HorizontalScrollView pathViewer;
	private File currentDir;
}
