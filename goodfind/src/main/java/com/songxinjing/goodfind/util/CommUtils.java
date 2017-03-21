package com.songxinjing.goodfind.util;

public class CommUtils {

	/**
	 * 当前线程等待
	 * 
	 * @param millis
	 *            微秒
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkString(String str) {
		for (int i = 0; i < str.length(); i++) {
			Character c = str.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

}
