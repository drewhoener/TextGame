package me.drewhoener.textgame;

import javax.swing.*;

public class MainGame {

	public static void main(String[] args) {

		System.out.println("A thing will go here eventually");
		GameManager manager = new GameManager();

		SwingUtilities.invokeLater(new GameManager());

	}

}
