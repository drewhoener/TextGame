package me.drewhoener.textgame;

import me.drewhoener.textgame.data.ConsoleParser;
import me.drewhoener.textgame.data.DataHolder;
import me.drewhoener.textgame.story.OptionList;
import me.drewhoener.textgame.story.StoryPage;
import me.drewhoener.util.Util;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
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
	public final JTextPane textPane;
	public JScrollPane scrollPane;
	public ConsoleParser consoleParser;
	public final Document paneDocument;
	public final StyledDocument paneStyle;

	public GameManager() {

		this.dataHolder = new DataHolder(this);

		this.mainFrame = new JFrame("Bolt");
		this.textPane = new JTextPane();
		this.textField = new JTextField();
		this.inputPanel = new JPanel();
		this.textPanel = new JPanel();
		this.consoleParser = new ConsoleParser(this);

		this.paneDocument = this.textPane.getDocument();
		this.paneStyle = this.textPane.getStyledDocument();

	}

	public void init() {


		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.mainFrame.setPreferredSize(new Dimension(1200, 650));

		//this.textPane.setPreferredSize(new Dimension(950, 300));
		this.textPane.setEditable(false);
		this.textPane.setBackground(Color.BLACK);
		this.textPane.setForeground(Color.GRAY);
		this.textPane.setFont(new Font("Gill Sans", Font.PLAIN, 18));
		this.textPane.setAutoscrolls(true);


		this.scrollPane = new JScrollPane(this.textPane);
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		this.scrollPane.getViewport().setBackground(Color.BLACK);
		this.scrollPane.setAutoscrolls(true);
		this.scrollPane.setForeground(Color.GRAY);
		this.scrollPane.setBorder(null);
		this.scrollPane.setPreferredSize(new Dimension(1150, 550));


		this.textField.setBackground(Color.BLACK);
		this.textField.setPreferredSize(new Dimension(1150, 50));
		this.textField.setFont(new Font("Gill Sans", Font.PLAIN, 18));
		this.textField.setBorder(null);
		this.textField.setCaretColor(Color.WHITE);
		this.textField.setForeground(Color.GRAY);

		this.textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!textField.getText().isEmpty()) {

					String command = textField.getText();

					appendString("> " + command);
					textField.setText("");

					consoleParser.parseCommand(command);

				}

			}
		});


		this.inputPanel.setBackground(Color.BLACK);

		this.inputPanel.add(this.textField);


		this.textPanel.setBackground(Color.BLACK);
		this.textPanel.add(this.scrollPane);

		this.mainFrame.add(this.textPanel, BorderLayout.CENTER);
		this.mainFrame.add(this.inputPanel, BorderLayout.PAGE_END);

		this.mainFrame.setResizable(false);
		this.mainFrame.pack();
		this.mainFrame.setVisible(true);
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

					if (line.equalsIgnoreCase("Meta:"))
						page.setMeta(Integer.parseInt(scanner.nextLine()));

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

		textPane.setFont(new Font("Gill Sans", Font.PLAIN, 80));
		this.setString("\n\nStory by Nate Kreiger\nProgrammed by Drew Hoener");

		this.readPages();

		this.textPanel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					dataHolder.shouldAdvance = true;
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});

		this.playIntro();

	}

	public void appendString(String string) {

		try {

			this.paneStyle.insertString(this.paneStyle.getLength(), string + "\n", null);
			SimpleAttributeSet centerAlign = new SimpleAttributeSet();
			StyleConstants.setAlignment(centerAlign, StyleConstants.ALIGN_CENTER);
			this.paneStyle.setParagraphAttributes(0, this.paneStyle.getLength(), centerAlign, false);

		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	public void setString(String string) {

		this.textPane.setText("");
		this.appendString(string);

	}

	public void displayCurPage() {

		this.dataHolder.shouldAdvance = false;
		this.textPane.setFont(new Font("Gill Sans", Font.PLAIN, 18));
		this.appendString("\n");
		this.textField.setEditable(false);
		this.textPanel.requestFocusInWindow();

		if (this.dataHolder.currentPage.getMeta() == 0) {

			this.appendString("\n" + this.dataHolder.currentPage.getTotalText() + "\n");

			appendString("\nYOUR OPTIONS:");
			runImmediateTimer(Util.formatOptionList(dataHolder.currentPage.getOptions().getOptions()), 1500, false, false);

		} else if (this.dataHolder.currentPage.getMeta() == 1) {

			this.textPane.setText("\n");
			this.runShortTimer(this.dataHolder.currentPage.getStoryText(), 100, false, true);

		} else if (this.dataHolder.currentPage.getMeta() == 2) {

			this.runShortTimer(this.dataHolder.currentPage.getStoryText(), 100, true, true);

		} else if (this.dataHolder.currentPage.getMeta() == 3) {

			this.runAdvanceTimer(this.dataHolder.currentPage.getStoryText(), 100, false);

		} else if (this.dataHolder.currentPage.getMeta() == 4) {

			this.textPane.setText("\n");
			this.runTextTimer(this.dataHolder.currentPage.getStoryText(), 100, true, 40, true);

		} else if (this.dataHolder.currentPage.getMeta() == 5) {

			this.runShortTimer(this.dataHolder.currentPage.getStoryText(), 100, false, true);

		}

	}

	public void runTextTimer(final List<String> list, final int delay, final boolean clearLines, final int textSize, final boolean showOptions) {

		textPane.setFont(new Font("Gill Sans", Font.PLAIN, textSize));

		this.appendString("\n\n\n\n" + list.get(0));

		new Timer(delay, new ActionListener() {
			int times = 1;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (dataHolder.shouldAdvance) {

					textPane.setFont(new Font("Gill Sans", Font.PLAIN, textSize));

					if (clearLines)
						textPane.setText("\n\n\n\n");

					if (times >= list.size() - 1) {
						((Timer) e.getSource()).stop();
						if (showOptions) {
							textPane.setFont(new Font("Gill Sans", Font.PLAIN, (int) (textSize * 1.5)));
							appendString(list.get(list.size() - 1));
							appendString("\nYOUR OPTIONS:");
							runImmediateTimer(Util.formatOptionList(dataHolder.currentPage.getOptions().getOptions()), 1500, false, false);
							textField.setEditable(true);
							textField.requestFocusInWindow();

						}
						return;

					}

					appendString("" + list.get(times));

					times++;

					dataHolder.shouldAdvance = false;

				}


			}
		}).start();

	}

	public void runAdvanceTimer(final List<String> list, final int delay, final boolean clearLines) {

		this.appendString(list.get(0));

		new Timer(delay, new ActionListener() {
			int times = 1;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (dataHolder.shouldAdvance) {

					if (clearLines)
						textPane.setText("");

					if (times >= list.size()) {
						((Timer) e.getSource()).stop();
						dataHolder.currentPage = dataHolder.getPage(dataHolder.currentPage.getPageNum() + 1);
						displayCurPage();
						return;
					}

					appendString("" + list.get(times));
					times++;

					dataHolder.shouldAdvance = false;

				}

			}
		}).start();

	}

	public void runImmediateTimer(final List<String> list, final int delay, final boolean clearLines, final boolean showOptions) {

		new Timer(delay, new ActionListener() {
			int times = 0;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (clearLines)
					textPane.setText("");

				if (times >= list.size()) {
					((Timer) e.getSource()).stop();
					if (showOptions) {

						appendString("\nYOUR OPTIONS:");
						runImmediateTimer(Util.formatOptionList(dataHolder.currentPage.getOptions().getOptions()), 1500, false, false);
						textField.setEditable(true);
						textField.requestFocusInWindow();
					}
					return;
				}

				appendString("" + list.get(times));
				times++;

			}
		}).start();

	}

	public void runShortTimer(final List<String> list, final int delay, final boolean clearLines, final boolean showOptions) {

		this.appendString(list.get(0));

		new Timer(delay, new ActionListener() {
			int times = 1;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (dataHolder.shouldAdvance) {

					if (clearLines)
						textPane.setText("");

					if (times >= list.size()) {
						((Timer) e.getSource()).stop();
						if (showOptions) {

							appendString("\nYOUR OPTIONS:");
							runImmediateTimer(Util.formatOptionList(dataHolder.currentPage.getOptions().getOptions()), 1500, false, false);
							textField.setEditable(true);
							textField.requestFocusInWindow();
						}
						return;
					}

					appendString("" + list.get(times));
					times++;

					dataHolder.shouldAdvance = false;

				}

			}
		}).start();

	}

	public void playIntro() {

		final StoryPage firstPage = this.dataHolder.getPage(0);

		if (firstPage != null)
			this.dataHolder.currentPage = firstPage;

		textPane.setFont(new Font("Gill Sans", Font.PLAIN, 68));

		Timer timer = new Timer((1000 * 5), new ActionListener() {
			int pos = 0;

			@Override
			public void actionPerformed(ActionEvent e) {

				setString("\n" + firstPage.getStoryText().get(pos));
				pos++;
				if (pos >= firstPage.getStoryText().size()) {
					((Timer) e.getSource()).stop();
					displayTitle();
				}

			}
		});
		timer.start();

	}

	public void displayTitle() {

		new Timer(5000, new ActionListener() {
			int times = 0;

			@Override
			public void actionPerformed(ActionEvent e) {


				if (times == 1) {
					((Timer) e.getSource()).stop();
					textPane.setText("");
					textPane.setFont(new Font("Gill Sans", Font.PLAIN, 18));
					dataHolder.currentPage = dataHolder.getPage(1);
					displayCurPage();
					return;
				}
				textPane.setText("\n");
				textPane.setFont(new Font("Gill Sans", Font.PLAIN, 150));
				appendString("BOLT");
				times++;

			}
		}).start();

	}

	public void quitGame() {

		new Timer(5000, new ActionListener() {
			int times = 0;

			@Override
			public void actionPerformed(ActionEvent e) {

				switch (times) {

					case 0:
						textPane.setText("\n\n");
						textPane.setFont(new Font("Gill Sans", Font.PLAIN, 110));
						appendString("THE END");
						break;
					case 1:
						textPane.setFont(new Font("Gill Sans", Font.PLAIN, 90));
						appendString("THANKS FOR PLAYING!");
						break;
					default:
						((Timer) e.getSource()).stop();
						System.exit(0);
						break;
				}
				times++;

			}
		}).start();

	}
}
