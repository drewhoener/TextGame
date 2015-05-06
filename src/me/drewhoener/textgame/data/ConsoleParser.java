package me.drewhoener.textgame.data;

import me.drewhoener.textgame.GameManager;
import me.drewhoener.textgame.story.StoryPage;
import me.drewhoener.util.Util;

public class ConsoleParser {

	private final GameManager gameManager;

	public ConsoleParser(GameManager gameManager) {

		this.gameManager = gameManager;
	}

	public void parseCommand(String command) {

		String[] strings = command.toLowerCase().split(" ");

		for (int i = 0; i < strings.length; i++) {

			String s = strings[i];

			switch (s) {

				case "ne":
				case "neast":
				case "northe":
					strings[i] = "northeast";
					break;
				case "se":
				case "seast":
				case "southe":
					strings[i] = "southeast";
					break;
				case "nw":
				case "nwest":
				case "northw":
					strings[i] = "northwest";
					break;
				case "sw":
				case "swest":
				case "southw":
					strings[i] = "southwest";
					break;
				case "n":
					strings[i] = "north";
					break;
				case "s":
					strings[i] = "south";
					break;
				case "e":
					strings[i] = "east";
					break;
				case "w":
					strings[i] = "west";
					break;

			}

		}

		this.runCommand(Util.joinArray(strings, " "));
	}

	public void runCommand(String command) {

		if ("quit".equalsIgnoreCase(command)) {

			this.gameManager.textPane.setText("");
			this.gameManager.quitGame();
			return;
		}

		if (command.toLowerCase().startsWith("option")) {
			Integer integer = Integer.parseInt(command.substring(7));

			StoryPage nextPage = this.gameManager.dataHolder.getPage(this.gameManager.dataHolder.currentPage.getOptions().getOptionMap().get(this.gameManager.dataHolder.currentPage.getOptions().getOptions().get(integer - 1).toLowerCase()));

			if (nextPage != null) {

				this.gameManager.dataHolder.lastPage = this.gameManager.dataHolder.currentPage;
				this.gameManager.dataHolder.currentPage = nextPage;
				this.gameManager.displayCurPage();
				return;

			}

		}

		if (Util.isInteger(command)) {

			Integer number = Integer.parseInt(command);
			System.out.println(number);

			StoryPage nextPage = this.gameManager.dataHolder.getPage(this.gameManager.dataHolder.currentPage.getOptions().getOptionMap().get(this.gameManager.dataHolder.currentPage.getOptions().getOptions().get(Integer.parseInt(command) - 1).toLowerCase()));

			if (nextPage != null) {

				this.gameManager.dataHolder.lastPage = this.gameManager.dataHolder.currentPage;
				this.gameManager.dataHolder.currentPage = nextPage;
				this.gameManager.displayCurPage();
				return;

			}

		}

		for (String string : this.gameManager.dataHolder.currentPage.getOptions().getOptionMap().keySet()) {

			if (string.equalsIgnoreCase(command)) {

				Integer page = this.gameManager.dataHolder.currentPage.getOptions().getOptionMap().get(string);

				if (page == null) {

					this.gameManager.appendString("That command is not an option!\n");
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

		this.gameManager.appendString("I don't understand this.\n");

	}

}


