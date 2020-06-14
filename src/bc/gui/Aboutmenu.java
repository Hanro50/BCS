package bc.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import han.lib.Debug;

import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class Aboutmenu extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Aboutmenu frame(JFrame frame) {

		if (FR == null)
			FR = new Aboutmenu(frame);

		Dimension D = new Dimension(400, 250);

		frame.setTitle("About");
		frame.setResizable(false);

		BackFrame.repaint(frame, D);

		return FR;
	};

	static Aboutmenu FR;

	/**
	 * Create the panel.
	 */
	public Aboutmenu(JFrame frame) {
		setForeground(UIManager.getColor("Button.background"));
		setBackground(UIManager.getColor("Button.foreground"));
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(UIManager.getColor("CheckBox.foreground"));
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblCopywriteHanro = new JLabel("Copywrite Hanro50");
		lblCopywriteHanro.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopywriteHanro.setForeground(Color.WHITE);
		lblCopywriteHanro.setBackground(Color.WHITE);
		panel.add(lblCopywriteHanro);

		JLabel lblCopywriteHanro_1 = new JLabel("Licence: GPL v3 (2020)");
		lblCopywriteHanro_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopywriteHanro_1.setForeground(Color.WHITE);
		lblCopywriteHanro_1.setBackground(Color.WHITE);
		panel.add(lblCopywriteHanro_1);

		JLabel lblVersion = new JLabel("Version: " + Debug.version);
		lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
		lblVersion.setForeground(Color.WHITE);
		lblVersion.setBackground(Color.WHITE);
		panel.add(lblVersion);

		JButton btnBack = new JButton("Back");
		btnBack.setBackground(Color.GRAY);
		btnBack.setForeground(Color.WHITE);
		panel.add(btnBack);

		Component verticalStrut = Box.createVerticalStrut(51);
		add(verticalStrut, BorderLayout.SOUTH);

		Component horizontalStrut = Box.createHorizontalStrut(101);
		add(horizontalStrut, BorderLayout.WEST);

		Component horizontalStrut_1 = Box.createHorizontalStrut(101);
		add(horizontalStrut_1, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(10, 60));
		panel_1.setBackground(UIManager.getColor("ColorChooser.foreground"));
		add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblAbout = new JLabel("About");
		panel_1.add(lblAbout);
		lblAbout.setForeground(UIManager.getColor("Button.background"));
		lblAbout.setHorizontalAlignment(SwingConstants.CENTER);
		lblAbout.setFont(new Font("Dialog", Font.BOLD, 30));

		// Common.repaint(frame);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// TODO Auto-generated method stub
				BackFrame.window.setpanel(MainMenu.frame(frame));
			}
		});
	}
}
