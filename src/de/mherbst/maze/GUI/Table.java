package de.mherbst.maze.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import de.mherbst.maze.GUI.Wall.Border;

public class Table implements ActionListener {

	private static Table mTableInstance = null;

	private final JFrame mainFrame;
	private final SliderPanel sliderPanel;

	private final ImagePanel imagePanel;
	private final JScrollPane scrollPane;

	private final static int MAZEIMAGE_WIDTH = 1200;
	private final static int MAZEIMAGE_HEIGHT = 800;

	public final static int OUTER_FRAME_WIDTH = 1400;
	public final static int OUTER_FRAME_HEIGHT = 1000;

	public static int cols;
	public static int rows;

	public static double sliderWert;

	private Box currentBox;
	private Box nextBox;

	public static Vector<Box> grid = new Vector<>();
	private Stack<Box> stack = new Stack<>();

	// the current zoom level (100 means the image is shown in original size)
	private int zoom = 100;

	// the current scale (scale = zoom/100)
	private double scale = 1;

	// the last seen scale
	private double lastScale = 1;

	// true if currently executing setViewPosition
	private boolean blocked = false;

	private Timer timer;
	private boolean running;

	private int lastMousePosX = 0;
	private int lastMousePosY = 0;

	private int actMousePosX = 0;
	private int actMousePosY = 0;

	private static BufferedImage image;

	public Table() {

		mTableInstance = Table.this;

		Table.cols = (int) Math.floor(MAZEIMAGE_WIDTH / Box.SIZE);
		Table.rows = (int) Math.floor(MAZEIMAGE_HEIGHT / Box.SIZE);

		for (int y = 0; y < Table.rows; y++) {
			for (int x = 0; x < Table.cols; x++) {
				Table.grid.add(new Box(x, y));
			}
		}

		currentBox = (Box) grid.elementAt(0);
		currentBox.setVisited(true);

		this.mainFrame = new JFrame("Labyrinth mit Zoom");
		this.mainFrame.setLayout(new BorderLayout());
		this.mainFrame.setSize(OUTER_FRAME_WIDTH, OUTER_FRAME_HEIGHT);

		this.imagePanel = new ImagePanel();

		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());

		centerPanel.add(imagePanel);

		scrollPane = new JScrollPane();

		scrollPane.setViewport(new JViewport() {

			private static final long serialVersionUID = 1L;

			private boolean inCall = false;

			@Override
			public void setViewPosition(Point pos) {
				if (!inCall || !blocked) {
					inCall = true;
					super.setViewPosition(pos);
					inCall = false;
				}
			}
		});

		scrollPane.getViewport().add(centerPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.sliderPanel = new SliderPanel();

		this.mainFrame.add(this.scrollPane, BorderLayout.CENTER);
		this.mainFrame.add(this.sliderPanel, BorderLayout.SOUTH);

		this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.mainFrame.setLocationRelativeTo(null);

		this.mainFrame.setVisible(true);

		running = true;
		timer = new Timer(10, this);
		timer.start();
	}

	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = 1L;

		ImagePanel() {

			addMouseWheelListener(new MouseAdapter() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					super.mouseWheelMoved(e);

					boolean zoomed = false;

					int rotation = e.getWheelRotation();
					if (rotation > 0) {
						int newCurrentZoom = zoom - 50;
						if (newCurrentZoom > 0) {
							zoom = newCurrentZoom;
							sliderPanel.setSliderWert(zoom);
							zoomed = true;
						}
					} else {
						int newCurrentZoom = zoom + 50;
						if (newCurrentZoom <= 1000) {
							zoom = newCurrentZoom;
							sliderPanel.setSliderWert(zoom);
							zoomed = true;
						}
					}
					
					if (zoomed) {
						scale = (float) (zoom / 100f);
						alignViewPort(e.getPoint());

						imagePanel.revalidate();
						scrollPane.repaint();
					}
				}
			});

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					lastMousePosX = 0;
					lastMousePosY = 0;
				}

				@Override
				public void mousePressed(MouseEvent e) {
					lastMousePosX = e.getX();
					lastMousePosY = e.getY();
				}
			});

			addMouseMotionListener(new MouseMotionListener() {

				@Override
				public void mouseMoved(MouseEvent e) {
					sliderPanel.setMouseWert(e.getX(), e.getY());
				}

				@Override
				public void mouseDragged(MouseEvent e) {

					if (scrollPane.getHeight() < imagePanel.getPreferredSize().getHeight() ||
						scrollPane.getWidth()  < imagePanel.getPreferredSize().getWidth()) {

						actMousePosX = e.getX();
						actMousePosY = e.getY();

						if (lastMousePosX != 0 && lastMousePosY != 0) {
							int dx = actMousePosX - lastMousePosX;
							int dy = actMousePosY - lastMousePosY;

							int tmpX = scrollPane.getViewport().getViewPosition().x - dx;
							int tmpY = scrollPane.getViewport().getViewPosition().y - dy;
							
							Point newViewportPosition = new Point(tmpX, tmpY);
																  
							if (tmpX > 0 && tmpY > 0) {
								blocked = true;
								scrollPane.getViewport().setViewPosition(newViewportPosition);
								blocked = false;
							}
						}

						lastMousePosX = actMousePosX;
						lastMousePosY = actMousePosY;

						sliderPanel.setMouseWert(actMousePosX, actMousePosY);

					}
				}

			});

			setPreferredSize(new Dimension(MAZEIMAGE_WIDTH, MAZEIMAGE_HEIGHT));
		}

		private void setSliderWert(int wert) {
			zoom = wert;
			scale = (float) (zoom / 100f);
			imagePanel.revalidate();
			scrollPane.repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g.create();

			g2d.scale(scale, scale);
			g2d.drawImage(image, 0, 0, null);

			g2d.dispose();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension((int) Math.round(MAZEIMAGE_WIDTH * scale), (int) Math.round(MAZEIMAGE_HEIGHT * scale));
		}
	}

	public void updateGUI(int zoom) {
		imagePanel.setSliderWert(zoom);
	}

	static void genGUIupdate(int zoom) {
		mTableInstance.updateGUI(zoom);
	}

	public void alignViewPort(Point mousePosition) {
		// if the scale didn't change there is nothing we should do
		if (scale != lastScale) {
			// compute the factor by that the image zoom has changed
			double scaleChange = scale / lastScale;

			// compute the scaled mouse position
			Point scaledMousePosition = new Point((int) Math.round(mousePosition.x * scaleChange),
					(int) Math.round(mousePosition.y * scaleChange));

			// retrieve the current viewport position
			Point viewportPosition = scrollPane.getViewport().getViewPosition();

			// compute the new viewport position
			Point newViewportPosition = new Point(viewportPosition.x + scaledMousePosition.x - mousePosition.x,
					viewportPosition.y + scaledMousePosition.y - mousePosition.y);

			// update the viewport position
			blocked = true;
			scrollPane.getViewport().setViewPosition(newViewportPosition);
			blocked = false;

			// remember the last scale
			lastScale = scale;
		}
	}

	private void removeWall(final Box cur, final Box nxt) {
		final int dx = cur.getX() - nxt.getX();
		final int dy = cur.getY() - nxt.getY();

		if (dx == 1) {
			cur.removeWall(Border.LEFT);
			nxt.removeWall(Border.RIGHT);
		} else if (dx == -1) {
			nxt.removeWall(Border.LEFT);
			cur.removeWall(Border.RIGHT);
		}

		if (dy == 1) {
			cur.removeWall(Border.TOP);
			nxt.removeWall(Border.BOTTOM);
		} else if (dy == -1) {
			nxt.removeWall(Border.TOP);
			cur.removeWall(Border.BOTTOM);
		}

	}

	public void getNextBox() {
		// Step 1
		nextBox = currentBox.checkNeighbours();
		if (nextBox != null) {
			nextBox.setVisited(true);

			// Step 2
			stack.push(currentBox);

			// Step 3
			removeWall(currentBox, nextBox);

			// Step 4
			currentBox = nextBox;
			currentBox.changeColor();
		} else {
			if (stack.size() > 0) {
				currentBox = stack.pop();
			} else {
				running = false;
			}
		}

		if (!running) {
			timer.stop();
		}
	}

	private BufferedImage getImage() {
		BufferedImage image = new BufferedImage(MAZEIMAGE_WIDTH, MAZEIMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, MAZEIMAGE_WIDTH, MAZEIMAGE_HEIGHT);

		// g2d.setStroke(new BasicStroke((float) (1.0f / scale)));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < grid.size(); i++) {
			((Box) grid.get(i)).show(g2d);
		}

		return image;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			image = getImage();
			getNextBox();
		}
		this.mainFrame.repaint();
	}

}
