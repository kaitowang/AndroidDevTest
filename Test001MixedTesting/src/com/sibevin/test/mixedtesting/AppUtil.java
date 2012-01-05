package com.sibevin.test.mixedtesting;

import java.io.File;

public final class AppUtil {
	static public boolean isATextFile(File file) {
		String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
		for(String textExt : AppConst.TXT_FILE_EXTENSION) {
			if(textExt.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}
}
