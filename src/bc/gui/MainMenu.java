package bc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import han.lib.Debug;

public class MainMenu extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static MainMenu FR;
	JLabel lblMainMenu = new JLabel("Main Menu");
	JButton btnScanMode = new JButton("Scan Mode");
	JButton btnReadMode = new JButton("Read mode");
	JButton btnExit = new JButton("Exit");

	private final JPanel pnlbuttons = new JPanel();
	private final JPanel pnlcontroll = new JPanel();
	private final Component verticalStrut = Box.createVerticalStrut(50);
	private final Panel pnlmain = new Panel();
	private final Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
	private final JPanel panel = new JPanel();
	private final JButton btnAbout = new JButton("About");

	static MainMenu frame(JFrame frame) {
		if (FR == null)
			FR = new MainMenu(frame);
		frame.setResizable(false);
	
		Dimension D = new Dimension(350, 450);
		frame.setMaximumSize(D);
		frame.setMinimumSize(D);
		frame.setSize(D);
		
		frame.setTitle("Main Menu");

		BackFrame.repaint(frame, D);

		

		return FR;
	};

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FR = new MainMenu(BackFrame.window.frameback);
				} catch (Exception e) {
					Debug.Trace(e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainMenu(JFrame frame) {
		setBackground(UIManager.getColor("Button.foreground"));
		setForeground(UIManager.getColor("Button.background"));
		setBackground(UIManager.getColor("Button.foreground"));
		setLayout(new BorderLayout(0, 0));
		panel.setBackground(UIManager.getColor("Button.foreground"));
		add(panel, BorderLayout.CENTER);
		pnlbuttons.setPreferredSize(new Dimension(300, 250));
		panel.add(pnlbuttons);
		pnlbuttons.setForeground(UIManager.getColor("Button.background"));
		pnlbuttons.setBackground(UIManager.getColor("Button.foreground"));
		pnlbuttons.setLayout(new GridLayout(0, 1, 10, 5));

		btnScanMode.setFont(new Font("Dialog", Font.BOLD, 16));
		btnScanMode.setBackground(Color.DARK_GRAY);
		btnScanMode.setForeground(UIManager.getColor("Button.background"));
		pnlbuttons.add(btnScanMode);

		btnReadMode.setFont(new Font("Dialog", Font.BOLD, 16));

		btnReadMode.setBackground(Color.DARK_GRAY);
		btnReadMode.setForeground(UIManager.getColor("Button.background"));
		pnlbuttons.add(btnReadMode);

		btnAbout.setForeground(Color.WHITE);
		btnAbout.setFont(new Font("Dialog", Font.BOLD, 16));
		btnAbout.setBackground(Color.DARK_GRAY);

		pnlbuttons.add(btnAbout);
		btnExit.setFont(new Font("Dialog", Font.BOLD, 16));

		btnExit.setForeground(Color.WHITE);
		btnExit.setBackground(Color.DARK_GRAY);
		pnlbuttons.add(btnExit);
		
		add(pnlmain, BorderLayout.NORTH);
		pnlmain.setLayout(new BoxLayout(pnlmain, BoxLayout.Y_AXIS));
		rigidArea.setPreferredSize(new Dimension(300, 20));

		pnlmain.add(rigidArea);
		pnlmain.add(pnlcontroll);
		pnlcontroll.setBackground(UIManager.getColor("Button.foreground"));
		pnlcontroll.setLayout(new BoxLayout(pnlcontroll, BoxLayout.Y_AXIS));

		lblMainMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnlcontroll.add(lblMainMenu);

		lblMainMenu.setFont(new Font("Dialog", Font.BOLD, 35));
		lblMainMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMainMenu.setForeground(UIManager.getColor("Button.background"));
		verticalStrut.setPreferredSize(new Dimension(0, 40));

		pnlmain.add(verticalStrut);

		// setLocationRelativeTo(null);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				System.exit(0);
			}
		});

		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				BackFrame.window.setpanel(Aboutmenu.frame(frame));
			}
		});

		btnScanMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackFrame.window.setpanel(ScanMode.frame(frame));
			}
		});
		btnReadMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackFrame.window.setpanel(ReadMode.frame(frame));
			}
		});
		
		//Common.repaint(frame);
	}

}
