package bc.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import han.lib.Debug;
import han.lib.FileObj;
import bc.data.ConInfo;
import java.awt.Rectangle;
import javax.swing.UIManager;

public class BackFrame {

	public JFrame frameback;
	static public BackFrame window;
	JPanel BPBACK = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new BackFrame();
					Debug.boot(args);
					Debug.Version();

					FileObj.FileChk(ConInfo.DBfile);

					window.setpanel(MainMenu.frame(window.frameback));
					window.frameback.setVisible(true);

				} catch (Exception e) {
					Debug.Trace(e);
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public BackFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameback = new JFrame("Menu");

		frameback.setForeground(UIManager.getColor("Button.background"));
		frameback.setBackground(UIManager.getColor("Button.foreground"));
		frameback.setBounds(new Rectangle(0, 0, 0, 0));
		frameback.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameback.getContentPane().setLayout(new BorderLayout(0, 0));
		frameback.getContentPane().add(BPBACK, BorderLayout.CENTER);
		frameback.setIconImage(Toolkit.getDefaultToolkit().getImage("./icon.png"));

		BPBACK.setBackground(UIManager.getColor("Button.foreground"));
		BPBACK.setForeground(UIManager.getColor("Button.foreground"));
		BPBACK.setLayout(new BorderLayout(0, 0));

	}

	public static void repaint(JFrame frame, Dimension D) {
		frame.setMaximumSize(D);
		frame.setMinimumSize(D);
		frame.setSize(D);

	}

	public void setpanel(JPanel panel) {

		// TODO Auto-generated method stub
		BPBACK.removeAll();
		BPBACK.getRootPane().revalidate();
		BPBACK.add(panel, BorderLayout.CENTER);

		BPBACK.getRootPane().revalidate();

	}

}
