package com.soze.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileUtils {

	/**
	 * Attempts to find a file with given name.
	 * If it is empty, writes [] to it, so it is a valid json array.
	 */
	public static File getOrEmptyListFile(String name) throws IOException {
		File file = getOrEmptyFile(name);
		if (isEmpty(file)) {
			writeToFile(file, "[]");
		}
		return file;
	}

	public static File getOrEmptyFile(String name) throws IOException {
		File file = org.apache.commons.io.FileUtils.getFile(name);
		org.apache.commons.io.FileUtils.touch(file);
		return file;
	}

	public static void writeToFile(File file, String content) throws IOException {
		org.apache.commons.io.FileUtils.writeStringToFile(file, content, Charset.defaultCharset());
	}

	public static boolean isEmpty(File file) {
		return org.apache.commons.io.FileUtils.sizeOf(file) == 0;
	}

}
