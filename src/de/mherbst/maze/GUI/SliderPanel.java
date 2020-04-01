package de.mherbst.maze.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JSlider slider;
	private final JLabel prozentLabel;
	private final JLabel mousePositionLabel;
	private final JPanel rightPanel;
	private final JPanel leftPanel;
	
	private int sliderWert;
	
	public SliderPanel() {
		
		sliderWert = 110;
		
		prozentLabel = new JLabel("(0100 %)");
		mousePositionLabel = new JLabel("Mouse: (0000|0000)");
		
		setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		
		slider = new JSlider(JSlider.HORIZONTAL, 50, 1000, 100);
		slider.setMinorTickSpacing(50);
		slider.setMajorTickSpacing(100);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setPaintTrack(true);
		slider.setSnapToTicks(true);
		slider.setPreferredSize(new Dimension(400, 50));
		
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderWert = slider.getValue();
				prozentLabel.setText("(" + String.format("%04d", sliderWert) + " %)");
				Table.genGUIupdate(sliderWert);
			}
		});

		rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		
		rightPanel.add(slider);
		rightPanel.add(prozentLabel);
		
		leftPanel.add(mousePositionLabel);
		
		add(leftPanel);
		add(rightPanel);
	}
	
	public void setMouseWert(int mouseX, int mouseY) {
		mousePositionLabel.setText("Mouse: (" + String.format("%04d", mouseX) + "|" + String.format("%04d", mouseY) + ")");
	}
	
	public void setSliderWert(int wert) {
		this.sliderWert = wert;
		slider.setValue(this.sliderWert);
	}
}
