package bc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import bc.data.ConInfo;
import bc.data.RecInfo;
import bc.data.StuInfo;
import han.lib.Debug;

public class ScanMode extends JPanel {
	static ScanMode FR;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel panel = new JPanel();

	JPanel pnlData = new JPanel();
	JTextArea lbldata = new JTextArea("");

	JPanel pnlscan = new JPanel();
	JTextField tfdscan = new JTextField(10);
	JLabel lblscan = new JLabel("Enter ID here:");

	JPanel pnlsubmit = new JPanel();
	private final JPanel pnlselector = new JPanel();
	JRadioButton rdbQ1 = new JRadioButton("Do you have a cough");
	JRadioButton rdbQ2 = new JRadioButton("Do you have shortness of breath");
	JRadioButton rdbQ3 = new JRadioButton("Do you have a fever?");
	JRadioButton rdbQ4 = new JRadioButton("Do you have a sore throat?");
	JRadioButton rdbQ5 = new JRadioButton("Have you traveled outside ZA in the past 14 days");
	JRadioButton rdbQ6 = new JRadioButton("Have you made contact with \nsomeone with Covid in the last 14 days?");
	// The joys of using a gui builder...
	JRadioButton[] ListRad = { rdbQ1, rdbQ2, rdbQ3, rdbQ4, rdbQ5, rdbQ6 };

	private final JButton btnSubmit = new JButton("Submit");

	JLabel lbltemp = new JLabel("Tempreture here:");
	JTextField tfdTemp = new JTextField(10);
	static Date today = new Date();
	public static final JSpinner Datepick = new JSpinner(new SpinnerDateModel(today, null, null, Calendar.MONTH));
	public static final JSpinner.DateEditor editor = new JSpinner.DateEditor(Datepick, "dd-MMMM-yyyy");
	public static final JPanel PnlMainWin = new JPanel();
	private final JButton btnback = new JButton("Back");
	JPanel pnltemp = new JPanel();
	JPanel pnlbuttons = new JPanel();
	public final JLabel lblReady = new JLabel("Ready!");
	private final JPanel pnloutput = new JPanel();
	StuInfo getinfo = new StuInfo();

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FR = new ScanMode(BackFrame.window.frameback);
					FR.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	static ScanMode frame(JFrame frame) {
		if (FR == null)
			FR = new ScanMode(frame);
		
		Dimension D = new Dimension(558, 544);
		BackFrame.repaint(frame, D);
		frame.setResizable(true);
		frame.setTitle("Scan Mode");
		return FR;
	};

	// gets called by the by the Text in box
	void Scanin() {
		// TODO Auto-generated method stub
		if (getinfo.Found) {
			Debug.out("Someone forgot to hit submit");
			Submit();
		}

		if (tfdscan.getText().length() > 0)
			Debug.out(tfdscan.getText());
		getinfo = StuInfo.getinfo(tfdscan.getText());

		lbldata.setText(getinfo.toString());

		tfdscan.setText("");

		if (getinfo.Found) {
			rdbswitch(true);
			// ScanMode.pnltemp.setVisible(false);
		} else {
			rdbswitch(false);
		}
		for (JRadioButton rdbx : ListRad) {
			rdbx.setSelected(false);
		}
		pnlsubmit.remove(pnltemp);
		pnlsubmit.setLayout(new GridLayout(0, 1, 0, 3));
		this.revalidate();
		this.doLayout();
		this.repaint();
		tfdTemp.setText(ConInfo.getTemp());
	}

	// Gets called by the submit button
	void Submit() {
		if (SubmitSave()) {

			lblReady.setText("Output: Wrote data of: [" + getinfo.Data[0] + "] "
					+ " successfully.");
			tfdscan.grabFocus();

			// successfully. Waiting for next submission.
		}
	}

	// I split the submit function in 2. Calling the UI elements caused a nasty
	// crash. When the app was closing
	boolean SubmitSave() {
		if (getinfo.Found) {
			boolean[] select = { rdbQ1.isSelected(), rdbQ2.isSelected(), rdbQ3.isSelected(), rdbQ4.isSelected(),
					rdbQ5.isSelected(), rdbQ6.isSelected() };
			RecInfo RIO;
			if (rdbQ3.isSelected())
				RIO = new RecInfo(getinfo.ID(), tfdTemp.getText(), select);
			else
				RIO = new RecInfo(getinfo.ID(), select);
			RecInfo.Save(RIO);
			return true;
			// successfully. Waiting for next submission.
		}
		return false;
	}

	// Temperature pop up menu
	void Tempreture() {
		pnltemp.setVisible(rdbQ3.isSelected());
		if (rdbQ3.isSelected()) {
			pnlsubmit.remove(pnlbuttons);
			pnlsubmit.add(pnltemp);
			pnlsubmit.add(pnlbuttons);
			tfdTemp.grabFocus();
		} else
			pnlsubmit.remove(pnltemp);
		pnlsubmit.setLayout(new GridLayout((rdbQ3.isSelected() ? 2 : 0), 1, 0, 3));
		this.revalidate();
		this.doLayout();
		this.repaint();

	}

	void rdbswitch(boolean b) {
		for (JRadioButton rdbx : ListRad) {
			rdbx.setEnabled(b);
		}
	}

	/**
	 * Create the frame.
	 */
	public ScanMode(JFrame frame) {

		setForeground(Color.WHITE);
		setBackground(UIManager.getColor("Button.foreground"));

		setBackground(UIManager.getColor("ComboBox.selectionForeground"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setSize(558, 544);

		// PnlMainWin setup
		PnlMainWin.setBackground(UIManager.getColor("Button.foreground"));
		PnlMainWin.setLayout(new BorderLayout(0, 0));
		add(PnlMainWin);

		// pnlsubmit setup
		pnlsubmit.setBorder(new LineBorder(UIManager.getColor("Button.foreground"), 3));
		pnlsubmit.setBackground(UIManager.getColor("Button.foreground"));
		pnlsubmit.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorSelectionForeground"));
		pnlsubmit.setLayout(new GridLayout(2, 1, 0, 3));
		PnlMainWin.add(pnlsubmit, BorderLayout.SOUTH);

		// pnltemp setup
		pnltemp.setVisible(false);
		pnltemp.setBackground(UIManager.getColor("Button.foreground"));
		pnltemp.setForeground(UIManager.getColor("Button.background"));
		pnltemp.setLayout(new GridLayout(0, 2, 0, 0));
		pnlsubmit.add(pnltemp);

		// lbltemp setup
		lbltemp.setFont(new Font("Dialog", Font.BOLD, 14));
		lbltemp.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltemp.setForeground(Color.WHITE);
		lbltemp.setBorder(new LineBorder(UIManager.getColor("Button.foreground"), 3));
		lbltemp.setBackground(UIManager.getColor("Button.foreground"));
		pnltemp.add(lbltemp);

		// tfdTemp setup
		tfdTemp.setFont(new Font("Dialog", Font.PLAIN, 14));
		tfdTemp.setText(ConInfo.getTemp());
		tfdTemp.setToolTipText("Put the tempreture in degrees Celcius here");
		tfdTemp.setHorizontalAlignment(SwingConstants.LEFT);
		tfdTemp.setForeground(Color.WHITE);
		tfdTemp.setCaretColor(UIManager.getColor("Button.background"));
		tfdTemp.setBorder(new LineBorder(UIManager.getColor("Button.background"), 1, true));
		tfdTemp.setBackground(UIManager.getColor("Button.foreground"));
		pnltemp.add(tfdTemp);

		// pnlbuttons setup
		pnlbuttons.setForeground(UIManager.getColor("Button.background"));
		pnlbuttons.setBackground(UIManager.getColor("Button.foreground"));
		pnlbuttons.setLayout(new GridLayout(0, 2, 2, 0));
		pnlsubmit.add(pnlbuttons);

		// btnSubmit setup
		btnSubmit.setFont(new Font("Dialog", Font.BOLD, 14));
		btnSubmit.setMinimumSize(new Dimension(90, 25));
		btnSubmit.setMaximumSize(new Dimension(90, 25));
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBackground(Color.DARK_GRAY);
		pnlbuttons.add(btnSubmit);
		btnback.setDoubleBuffered(true);

		// btnback setup
		btnback.setFont(new Font("Dialog", Font.BOLD, 14));
		btnback.setMinimumSize(new Dimension(90, 25));
		btnback.setMaximumSize(new Dimension(90, 25));
		btnback.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnback.setForeground(Color.WHITE);
		btnback.setBackground(Color.DARK_GRAY);
		pnlbuttons.add(btnback);

		// panel setup
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		PnlMainWin.add(panel, BorderLayout.CENTER);

		// pnlscan setup
		pnlscan = new JPanel();
		pnlscan.setBorder(new LineBorder(UIManager.getColor("Button.foreground"), 3));
		pnlscan.setBackground(UIManager.getColor("Button.foreground"));
		pnlscan.setLayout(new GridLayout(1, 3, 0, 0));
		panel.add(pnlscan);

		// lblscan setup
		lblscan.setToolTipText("");
		lblscan.setFont(new Font("Dialog", Font.BOLD, 14));
		lblscan.setBorder(new LineBorder(UIManager.getColor("Button.foreground"), 3));
		lblscan.setBackground(UIManager.getColor("Button.foreground"));
		lblscan.setForeground(UIManager.getColor("Button.light"));
		pnlscan.add(lblscan);

		// tfdscan setup
		tfdscan.setFont(new Font("Dialog", Font.PLAIN, 14));
		tfdscan.setBorder(new LineBorder(UIManager.getColor("Button.background"), 1, true));
		tfdscan.setHorizontalAlignment(SwingConstants.LEFT);
		tfdscan.setCaretColor(UIManager.getColor("Button.background"));
		tfdscan.setForeground(UIManager.getColor("Button.highlight"));
		tfdscan.setBackground(UIManager.getColor("Button.foreground"));
		tfdscan.setToolTipText("Press enter to submit an ID manually");
		pnlscan.add(tfdscan);

		// Datepick setup
		Datepick.setToolTipText("This auto populates. Make sure the date is correct");
		Datepick.setFont(new Font("Dialog", Font.BOLD, 14));
		editor.setToolTipText(Datepick.getToolTipText());
		Datepick.setEditor(editor);
		panel.add(Datepick);

		// pnloutput setup
		pnloutput.setToolTipText("Output panel. Tells you what was the latest action that was performed");
		pnloutput.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(64, 64, 64), new Color(64, 64, 64),
				Color.DARK_GRAY, Color.DARK_GRAY));
		pnloutput.setBackground(UIManager.getColor("Button.foreground"));
		panel.add(pnloutput);

		// lblReady setup
		lblReady.setFont(new Font("Dialog", Font.BOLD, 14));
		lblReady.setHorizontalAlignment(SwingConstants.CENTER);
		lblReady.setHorizontalTextPosition(SwingConstants.CENTER);
		lblReady.setForeground(UIManager.getColor("Button.background"));
		lblReady.setAlignmentX(Component.CENTER_ALIGNMENT);
		pnloutput.add(lblReady);

		// pnlData setup
		pnlData = new JPanel();
		pnlData.setToolTipText("Output Window ");
		pnlData.setBorder(new LineBorder(UIManager.getColor("Button.foreground"), 3, true));
		pnlData.setLayout(new BoxLayout(pnlData, 0));
		pnlData.setAutoscrolls(true);
		panel.add(pnlData);

		// lbldata setup
		lbldata.setFont(new Font("Dialog", Font.PLAIN, 16));
		lbldata.setRows(5);
		lbldata.setEditable(false);
		pnlData.add(lbldata);

		// pnlselector setup
		pnlselector.setToolTipText("Select the radio box to indicate a tick for \"yes\"");
		pnlselector.setBackground(UIManager.getColor("Button.foreground"));
		pnlselector.setLayout(new GridLayout(6, 1, 0, 0));
		panel.add(pnlselector);

		// Radio list setup
		rdbQ1.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbQ1.setForeground(UIManager.getColor("Button.light"));
		rdbQ1.setBackground(UIManager.getColor("Button.foreground"));
		rdbQ1.setEnabled(false);
		rdbQ1.setToolTipText(rdbQ1.getText());
		pnlselector.add(rdbQ1);

		rdbQ2.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbQ2.setForeground(UIManager.getColor("Button.light"));
		rdbQ2.setBackground(UIManager.getColor("Button.foreground"));
		rdbQ2.setEnabled(false);
		rdbQ2.setToolTipText(rdbQ2.getText());
		pnlselector.add(rdbQ2);

		rdbQ3.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbQ3.setForeground(UIManager.getColor("Button.light"));
		rdbQ3.setBackground(UIManager.getColor("Button.foreground"));
		rdbQ3.setEnabled(false);
		rdbQ3.setToolTipText(rdbQ3.getText());
		pnlselector.add(rdbQ3);

		rdbQ4.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbQ4.setForeground(UIManager.getColor("Button.light"));
		rdbQ4.setBackground(UIManager.getColor("Button.foreground"));
		rdbQ4.setEnabled(false);
		rdbQ4.setToolTipText(rdbQ4.getText());
		pnlselector.add(rdbQ4);

		rdbQ5.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbQ5.setForeground(UIManager.getColor("Button.light"));
		rdbQ5.setBackground(UIManager.getColor("Button.foreground"));
		rdbQ5.setEnabled(false);
		rdbQ5.setToolTipText(rdbQ5.getText());
		pnlselector.add(rdbQ5);

		rdbQ6.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbQ6.setForeground(UIManager.getColor("Button.light"));
		rdbQ6.setBackground(UIManager.getColor("Button.foreground"));
		rdbQ6.setEnabled(false);
		rdbQ6.setToolTipText(rdbQ6.getText());
		pnlselector.add(rdbQ6);

		// Someone is going to forget to press submit before closing this app...
		// I calculate a 0% chance they'll catch their mistake
		tfdscan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Scanin();
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SubmitSave();
				Debug.out("Shutting down");
				super.run();
			}
		});

		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Submit();
			}
		});
		rdbQ3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tempreture();
			}
		});
		btnback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackFrame.window.setpanel(MainMenu.frame(frame));
			}
		});

		// Final prepwork and config
		setVisible(true);

		Scanin();
		lbldata.setText("Welcome User");
	}

}
