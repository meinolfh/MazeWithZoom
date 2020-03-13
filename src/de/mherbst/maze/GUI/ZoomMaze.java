package de.mherbst.maze.GUI;

import java.awt.EventQueue;

public class ZoomMaze {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Table();
			}
		});
	}
}
