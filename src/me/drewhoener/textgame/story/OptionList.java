package me.drewhoener.textgame.story;

import me.drewhoener.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class OptionList {

	private HashMap<String, Integer> optionMap = new HashMap<>();

	public OptionList(String data) {

		String[] options = data.split(Pattern.quote("_"));

		this.parseData(options);

	}

	public OptionList(String[] data) {

		this.parseData(data);

	}

	public HashMap<String, Integer> getOptionMap() {
		return optionMap;
	}

	public String toString() {

		return this.optionMap.toString();

	}

	public List<String> getOptions() {

		List<String> list = new ArrayList<>();

		for (String s : this.optionMap.keySet()) {

			list.add(s.toUpperCase());

		}
		return list;
	}

	public void parseData(String[] dataParts) {

		for (String s : dataParts) {

			String[] parts = s.split(Pattern.quote(";"));

			if (parts.length < 2) {
				continue;
			}

			if (!Util.isInteger(parts[1])) {
				continue;
			}

			this.optionMap.put(parts[0], Integer.parseInt(parts[1]));

		}

	}

}
