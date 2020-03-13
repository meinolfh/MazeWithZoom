package de.mherbst.maze.GUI;

public class Wall {

	private boolean top;
	private boolean right;
	private boolean bottom;
	private boolean left;

	public enum Border {
		TOP(0), RIGHT(1), BOTTOM(2), LEFT(3);

		private final int border;

		Border(int border) {
			this.border = border;
		}

		private int getBorder() {
			return border;
		}

		private static int valueOf(Border b) {
			return b.ordinal();
		}
	}

	public Wall() {
		this.top = true;
		this.right = true;
		this.bottom = true;
		this.left = true;
	}

	public void setInvisible(Border b) {
		if (b.getBorder() == Border.valueOf(Border.TOP)) {
			this.top = false;
		}
		if (b.getBorder() == Border.valueOf(Border.RIGHT)) {
			this.right = false;
		}
		if (b.getBorder() == Border.valueOf(Border.BOTTOM)) {
			this.bottom = false;
		}
		if (b.getBorder() == Border.valueOf(Border.LEFT)) {
			this.left = false;
		}
	}

	public boolean isVisible(Border b) {
		if (b.getBorder() == Border.valueOf(Border.TOP)) {
			return this.top;
		}
		if (b.getBorder() == Border.valueOf(Border.RIGHT)) {
			return this.right;
		}
		if (b.getBorder() == Border.valueOf(Border.BOTTOM)) {
			return this.bottom;
		}
		if (b.getBorder() == Border.valueOf(Border.LEFT)) {
			return this.left;
		}

		return false;
	}
}
