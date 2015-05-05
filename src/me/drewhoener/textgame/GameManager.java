package me.drewhoener.textgame;

import me.drewhoener.textgame.data.ConsoleParser;
import me.drewhoener.textgame.data.DataHolder;
import me.drewhoener.textgame.story.OptionList;
import me.drewhoener.textgame.story.StoryPage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class GameManager implements Runnable {

	public DataHolder dataHolder;
	public final JFrame mainFrame;
	public final JPanel textPanel;
	public final JPanel inputPanel;
	public final JTextField textField;
	public final JTextArea textPane;
	public JScrollPane scrollPane;
	public ConsoleParser consoleParser;

	public GameManager() {

		this.dataHolder = new DataHolder(this);

		this.mainFrame = new JFrame("Bolt");
		this.textPane = new JTextArea();
		this.textField = new JTextField();
		this.inputPanel = new JPanel();
		this.textPanel = new JPanel();
		this.consoleParser = new ConsoleParser(this);

	}

	public void init() {


		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.mainFrame.setPreferredSize(new Dimension(1000, 400));

		//this.textPane.setPreferredSize(new Dimension(950, 300));
		this.textPane.setEditable(false);
		this.textPane.setBackground(Color.BLACK);
		this.textPane.setForeground(Color.WHITE);
		//this.textPane.setAutoscrolls(true);

		this.textPane.append("Bolt: A Text-Based Game\n");

		this.scrollPane = new JScrollPane(this.textPane);
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		this.scrollPane.getViewport().setBackground(Color.BLACK);
		this.scrollPane.setAutoscrolls(true);
		this.scrollPane.setForeground(Color.WHITE);
		this.scrollPane.setViewportBorder(new BevelBorder(BevelBorder.RAISED, Color.GREEN, Color.BLACK));
		this.scrollPane.setPreferredSize(new Dimension(950, 300));


		this.textField.setBackground(Color.BLACK);
		this.textField.setPreferredSize(new Dimension(950, 50));
		this.textField.setCaretColor(Color.WHITE);
		this.textField.setForeground(Color.WHITE);

		this.textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String command = textField.getText();

				textPane.append("\n> " + command);
				textField.setText("");

				consoleParser.parseCommand(command);

			}
		});


		this.inputPanel.setBackground(Color.BLACK);

		this.inputPanel.add(this.textField);


		this.textPanel.setBackground(Color.BLACK);
		this.textPanel.add(this.scrollPane);

		this.mainFrame.add(this.textPanel, BorderLayout.CENTER);
		this.mainFrame.add(this.inputPanel, BorderLayout.PAGE_END);

		this.mainFrame.pack();
		this.mainFrame.setVisible(true);

		this.textField.requestFocus();
	}

	public void readPages() {

		Scanner scanner = new Scanner(this.getClass().getResourceAsStream("/resources/story.txt"));

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();

			//System.out.println(line);

			if ("-----".equals(line))
				continue;

			if (line.startsWith("#")) {

				String pageNum = line.substring(1);
				StoryPage page = new StoryPage(Integer.parseInt(pageNum));

				while (!line.equalsIgnoreCase("---End " + pageNum)) {

					line = scanner.nextLine();

					//System.out.println(line);

					if (line.equals("Text:")) {

						line = scanner.nextLine();

						while (!line.equalsIgnoreCase("---End Text")) {


							//System.out.println(line);

							page.addLine(line);
							line = scanner.nextLine();

						}

					}

					if (line.equalsIgnoreCase("Options:"))
						page.setOptions(new OptionList(scanner.nextLine()));

				}

				this.dataHolder.pageList.add(page);
				System.out.println(page);


			}
		}

		scanner.close();

	}

	@Override
	public void run() {

		this.init();
		this.readPages();

		StoryPage firstPage = this.dataHolder.getPage(1);

		if (firstPage != null)
			this.dataHolder.currentPage = firstPage;

		this.displayCurPage();

	}

	public void displayCurPage() {
		this.textPane.append("\n\n" + this.dataHolder.currentPage.getTotalText() + "\n");
	}
}
