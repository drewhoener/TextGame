package me.drewhoener.textgame.story;

import java.util.ArrayList;
import java.util.List;

public class StoryPage {

	private List<String> storyText = new ArrayList<>();
	private OptionList options;
	private int pageNum;
	private int meta;
	public int curLine = -1;

	public StoryPage(int pageNum, List<String> storyText, OptionList options) {
		this.pageNum = pageNum;
		this.storyText = storyText;
		this.options = options;
	}

	public StoryPage(int pageNum, List<String> storyText, String optionsText) {
		this.pageNum = pageNum;
		this.storyText = storyText;
		this.options = new OptionList(optionsText);
	}

	public StoryPage(int pageNum, List<String> storyText, String[] options) {
		this.pageNum = pageNum;
		this.storyText = storyText;
		this.options = new OptionList(options);
	}

	public StoryPage(int pageNum) {
		this.pageNum = pageNum;

	}

	public String getNextLine() {

		this.curLine++;
		int cur = curLine;

		if (this.curLine < this.storyText.size()) {

			this.curLine = -1;
			return this.storyText.get(cur);
		}

		return null;
	}

	public int getMeta() {
		return this.meta;
	}

	public void setOptions(OptionList options) {
		this.options = options;
	}

	public void setMeta(int meta) {
		this.meta = meta;
	}

	public void setStoryText(List<String> storyText) {
		this.storyText = storyText;
	}

	public OptionList getOptions() {
		return options;
	}

	public List<String> getStoryText() {
		return storyText;
	}

	public void addLine(String string) {

		this.storyText.add(string);
	}

	public int getPageNum() {
		return pageNum;
	}

	public String getTotalText() {

		String s = this.storyText.get(0);

		for (int i = 1; i < this.storyText.size(); i++) {

			s += "\n" + this.storyText.get(i);
		}

		return s;
	}

	public String toString() {

		return this.storyText.toString() + "\n" + this.options.toString();
	}

}
