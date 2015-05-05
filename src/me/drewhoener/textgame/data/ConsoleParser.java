package me.drewhoener.textgame.data;

import me.drewhoener.textgame.GameManager;
import me.drewhoener.textgame.story.StoryPage;

public class ConsoleParser {

	private final GameManager gameManager;

	public ConsoleParser(GameManager gameManager) {

		this.gameManager = gameManager;
	}

	public void parseCommand(String command) {

		for (String string : this.gameManager.dataHolder.currentPage.getOptions().getOptionMap().keySet()) {

			if (string.equalsIgnoreCase(command)) {


				Integer page = this.gameManager.dataHolder.currentPage.getOptions().getOptionMap().get(string);

				if (page == null) {

					this.gameManager.textPane.append("\nThat command is not an option!");
					return;
				}

				StoryPage nextPage = this.gameManager.dataHolder.getPage(page);

				if (nextPage != null) {

					this.gameManager.dataHolder.lastPage = this.gameManager.dataHolder.currentPage;
					this.gameManager.dataHolder.currentPage = nextPage;
					this.gameManager.displayCurPage();
					return;

				}
			}

		}

		this.gameManager.textPane.append("\nI don't understand this.");
	}

}
