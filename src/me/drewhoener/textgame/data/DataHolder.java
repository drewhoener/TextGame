package me.drewhoener.textgame.data;

import me.drewhoener.textgame.GameManager;
import me.drewhoener.textgame.story.StoryPage;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {

	private final GameManager manager;
	public final List<StoryPage> pageList = new ArrayList<>();
	public final List<String> inventoryItems = new ArrayList<>();
	public StoryPage currentPage;
	public StoryPage lastPage = null;

	public DataHolder(GameManager manager) {
		this.manager = manager;
	}

	public StoryPage getPage(int number) {

		for (StoryPage page : this.pageList) {

			if (page.getPageNum() == number) {
				return page;
			}

		}

		return null;
	}

}
