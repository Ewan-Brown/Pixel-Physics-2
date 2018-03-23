package pixelphysics2.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

public class InitView {

	public JFrame frame;
	
	/**
	 * Launch the application.
	 */
	/**
	 * Create the application.
	 */
	public InitView() {
		initialize();
	}
	public JSlider slider;
	public JToggleButton tglPerformance;
	public boolean finished = false;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 277, 218);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lbl1 = new JLabel("Maximum Particle count");
		lbl1.setBounds(68, 11, 157, 14);
		frame.getContentPane().add(lbl1);
		
		slider = new JSlider();
		slider.setMaximum(1000000);
		slider.setMinimum(1);
		slider.setBounds(33, 44, 200, 53);
		slider.setMajorTickSpacing(999999);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(10000);
		frame.getContentPane().add(slider);
		
		tglPerformance = new JToggleButton("Low performance mode");
		tglPerformance.setBounds(33, 99, 190, 35);
		frame.getContentPane().add(tglPerformance);
		
		JButton btnBegin = new JButton("Begin");
		btnBegin.setBounds(85, 145, 89, 23);
		btnBegin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				finished = true;
			}
			
		});
		frame.getContentPane().add(btnBegin);
		frame.setVisible(true);
	}
}
