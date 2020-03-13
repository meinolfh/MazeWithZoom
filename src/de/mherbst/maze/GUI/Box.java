package de.mherbst.maze.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import de.mherbst.maze.GUI.Wall.Border;

public class Box {

	public static int SIZE = 20;

	private int x;
	private int y;
	private boolean visited;
	private boolean otherColor;

	private Wall wall;

	public Box(final int x, final int y) {
		this.x = x;
		this.y = y;
		this.visited = false;
		this.otherColor = false;

		this.wall = new Wall();
	}

	public void show(Graphics2D g2d) {
		g2d.setColor(Color.RED);

		if (wall.isVisible(Border.TOP)) {
				g2d.drawLine(x * SIZE, y * SIZE, (x + 1) * SIZE, y * SIZE);
		}

		if (wall.isVisible(Border.RIGHT)) {
			if (x != Table.cols - 1)			
				g2d.drawLine((x + 1) * SIZE, y * SIZE, (x + 1) * SIZE, (y + 1) * SIZE);
			else
				g2d.drawLine((x + 1) * SIZE - 1, y * SIZE, (x + 1) * SIZE - 1, (y + 1) * SIZE);
		}

		if (wall.isVisible(Border.BOTTOM)) {
			if (y != Table.rows - 1)
				g2d.drawLine((x + 1) * SIZE, (y + 1) * SIZE, x * SIZE, (y + 1) * SIZE);
			else
				g2d.drawLine((x + 1) * SIZE, (y + 1) * SIZE - 1, x * SIZE, (y + 1) * SIZE - 1);
		}

		if (wall.isVisible(Border.LEFT)) {
			g2d.drawLine(x * SIZE, (y + 1) * SIZE, x * SIZE, y * SIZE);
		}

		if (this.otherColor) {
			g2d.setColor(Color.BLUE);
			g2d.fillRect(x * SIZE, y * SIZE, SIZE,  SIZE);
			this.otherColor = false;
		}
	}

	public void changeColor() {
		this.otherColor = true;
	}
	
	public void setVisited(final boolean visited) {
		this.visited = visited;
	}

	private int index(int x, int y) {
		if (x < 0 || y < 0 || x > Table.cols - 1 || y > Table.rows - 1) {
			return -1;
		}
		return x + y * Table.cols;
	}

	public Box checkNeighbours() {
		final ArrayList<Box> neighbours = new ArrayList<>();
		int position = 0;

		position = index(this.x, this.y - 1);
		Box topBox = (position != -1) ? (Box) Table.grid.elementAt(position) : null;

		position = index(this.x + 1, this.y);
		Box rightBox = (position != -1) ? (Box) Table.grid.elementAt(position) : null;

		position = index(this.x, this.y + 1);
		Box bottomBox = (position != -1) ? (Box) Table.grid.elementAt(position) : null;

		position = index(this.x - 1, this.y);
		Box leftBox = (position != -1) ? (Box) Table.grid.elementAt(position) : null;

		if (topBox != null && !topBox.visited) {
			neighbours.add(topBox);
		}

		if (rightBox != null && !rightBox.visited) {
			neighbours.add(rightBox);
		}

		if (bottomBox != null && !bottomBox.visited) {
			neighbours.add(bottomBox);
		}

		if (leftBox != null && !leftBox.visited) {
			neighbours.add(leftBox);
		}

		if (neighbours.size() > 0) {
			int r = (int) Math.floor(Math.random() * neighbours.size());
			return neighbours.get(r);
		}

		return null;

	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void removeWall(final Wall.Border b) {
		switch (b) {
		case TOP:
			wall.setInvisible(Wall.Border.TOP);
			break;
		case RIGHT:
			wall.setInvisible(Wall.Border.RIGHT);
			break;
		case BOTTOM:
			wall.setInvisible(Wall.Border.BOTTOM);
			break;
		case LEFT:
			wall.setInvisible(Wall.Border.LEFT);
			break;
		}
	}
}
