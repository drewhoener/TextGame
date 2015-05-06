package me.drewhoener.util;

import java.util.List;

public class Util {

	protected Util() {
	}

	/**
	 * Determines if a string is an integer
	 *
	 * @param str The string to check
	 * @return If it is a valid integer
	 */
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}

	public static String joinArray(String[] str, String join) {

		String joined = str[0];

		for (int i = 1; i < str.length; i++) {
			joined += join + str[i];
		}

		return joined;
	}

	public static List<String> formatOptionList(List<String> list) {

		for (int i = 0; i < list.size(); i++) {

			list.set(i, "Option " + (i + 1) + ": " + list.get(i));

		}

		return list;
	}

}
