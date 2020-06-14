package bc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import bc.data.RecInfo;

import han.lib.Debug;
import han.lib.FileObj;
import bc.data.ConInfo;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;

//import bc.gui.Readmode.clr;

public class ReadMode extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static ReadMode FR;

	private static JTable tbltable;
	public static JComboBox<String> cbbData = new JComboBox<String>();
	Object[][] str2d;
	JFileChooser fc;
	private JButton btnback;
	private JButton btnToCST;
	clr WFTU;
	Thread WTFITN;

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReadMode frame = new ReadMode(BackFrame.window.frameback);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	{
		WFTU = new clr();
		WTFITN = new Thread(WFTU);
	}

	static ReadMode frame(JFrame frame) {
		if (FR == null)
			FR = new ReadMode(frame);
		
		Dimension D = new Dimension(623, 425);
		BackFrame.repaint(frame, D);
		// frame.setBounds(100, 100, 623, 425);
		frame.setResizable(true);
		frame.setTitle("ReadMode Menu");
		FR.updatepicker();
		return FR;
	};

	private class clr implements Runnable {
		boolean stop;
		// Java is funny...fudge whoever's fault it was to make sure the back panel was
		// themable!

		public clr() {
			stop = false;
		}

		@Override
		public void run() {
			try {
				while (fc.getParent() == null) {
					Thread.sleep(1);
					if (stop) {
						updateclr(fc);
						return;
					}
				}
				Object frame = fc.getParent().getParent().getParent().getParent();
				if (frame instanceof JDialog)
					((JDialog) frame).setIconImage(Toolkit.getDefaultToolkit().getImage("./icon.png"));

				// Debug.out(frame.toString());

				updateclr(fc.getParent());
			} catch (InterruptedException e) {

			}
		}
	}

	private void updateclr(Component com) {
		Component[] cmp;
		com.setForeground(Color.WHITE);
		com.setBackground(Color.DARK_GRAY);
		if (com instanceof Container) {
			cmp = ((Container) com).getComponents();
			for (int i = 0; i < cmp.length; i++) {
				updateclr(cmp[i]);
			}
		}
	}

	private void Export() {
		UIManager.put("FileChooser.noPlacesBar", Boolean.TRUE);

		fc = new JFileChooser();

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setSelectedFile(ConInfo.getFile());
		fc.setDialogTitle("Save file");

		// fudge java being weird. I needed to do this hacky mess or my theming would
		// break!
		if (WTFITN.isAlive()) {
			WTFITN.interrupt();
		}
		;
		WFTU.stop = false;
		WTFITN = new Thread(WFTU);
		WTFITN.start();
		int returnVal = fc.showSaveDialog(fc);
		WFTU.stop = true;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();

			// This is where a real application would open the file.
			String Path = file.getAbsolutePath() + "/";
			ConInfo.setFilepath(Path);
			File sf = new File(Path + cbbData.getModel().getSelectedItem() + ".csv");
			String[][] str2d = (String[][]) this.str2d;
			String[] lines = new String[str2d.length];
			for (int i = 0; i < lines.length; i++) {
				lines[i] = str2d[i][0];
				for (int i2 = 1; i2 < str2d[i].length; i2++) {
					if (str2d[i][i2].contains(",")) lines[i] = lines[i] + ",\"" + str2d[i][i2] + "\"";
					else lines[i] = lines[i]+ "," + str2d[i][i2] ;
					
					
					
				}
			}
			try {
				FileObj.writeNF(lines, sf, "csv");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				Debug.Trace(e);
			}
			Debug.out("Saving to: " + Path);

		} else {
			Debug.out("Open command cancelled by user.");
		}
	}

	private void updatepicker() {
		List<Date> lines = new ArrayList<Date>();
		String[] ls = FileObj.FileListString(ConInfo.DBfile);
		SimpleDateFormat SDF = new SimpleDateFormat("dd-MMMM-yyyy");
		for (int i = 0; i < ls.length; i++) {
			try {
				lines.add(SDF.parse(ls[i]));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				Debug.out("If there isn't any data, ignore this");
			}
		}

		Collections.sort(lines, new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				return o2.compareTo(o1);
			}
		});

		String[] fls = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			// Debug.out(lines.get(i).toString());
			fls[i] = SDF.format(lines.get(i));
		}
		if ((fls == null) || (fls.length < 1))
			fls = new String[] { "No Data loadable" };
		cbbData.setToolTipText("Date Selector");
		cbbData.setModel(new DefaultComboBoxModel<String>(fls));
	}

	private void updatetable() {
		str2d = RecInfo.Read();
		tbltable.setModel(new DefaultTableModel(str2d, RecInfo.header()));
		tbltable.setAutoResizeMode(0);
		resizeColumnWidth(tbltable);
	}

	// Special credit to Paul Vargas, code taken from
	// (https://stackoverflow.com/a/17627497)
	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	/**
	 * Create the frame.
	 */
	public ReadMode(JFrame frame) {
	
		setForeground(UIManager.getColor("Button.background"));
		setBackground(UIManager.getColor("Button.foreground"));
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.foreground"));
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel pnlSearch = new JPanel();
		pnlSearch.setBackground(UIManager.getColor("Button.foreground"));
		panel.add(pnlSearch, BorderLayout.NORTH);

		JLabel lblDateSelector = new JLabel("Date Selector");
		lblDateSelector.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDateSelector.setForeground(UIManager.getColor("Button.background"));
		pnlSearch.add(lblDateSelector);
		cbbData.setFont(new Font("Dialog", Font.BOLD, 14));

		cbbData.setBackground(UIManager.getColor("Button.foreground"));
		cbbData.setForeground(UIManager.getColor("Button.background"));

		pnlSearch.add(cbbData);

		tbltable = new JTable();
		tbltable.setFont(new Font("Dialog", Font.PLAIN, 14));
		tbltable.setMinimumSize(new Dimension(500, 0));
		// panel_1.add(table);

		tbltable.setForeground(UIManager.getColor("Button.background"));
		tbltable.setBackground(UIManager.getColor("Button.foreground"));
		tbltable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbltable.setFillsViewportHeight(true);
		tbltable.setModel(new DefaultTableModel(new Object[][] { RecInfo.header(), }, RecInfo.header()));

		JPanel pnltable = new JPanel();
		pnltable.setLayout(new BorderLayout(0, 0));
		// panel.add(pnltable, BorderLayout.WEST);

		Component horizontalStrut = Box.createHorizontalStrut(310);
		horizontalStrut.setPreferredSize(new Dimension(500, 0));
		pnltable.add(horizontalStrut);
		pnltable.add(tbltable);

		JScrollPane jsptable = new JScrollPane(pnltable);
		jsptable.setMinimumSize(new Dimension(500, 22));
		jsptable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsptable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(jsptable);

		JPanel pnlbuttons = new JPanel();
		pnlbuttons.setBorder(new LineBorder(UIManager.getColor("Button.foreground"), 3));
		pnlbuttons.setForeground(UIManager.getColor("Button.background"));
		pnlbuttons.setBackground(UIManager.getColor("Button.foreground"));
		panel.add(pnlbuttons, BorderLayout.SOUTH);
		pnlbuttons.setLayout(new GridLayout(0, 2, 2, 0));

		btnToCST = new JButton("Export");

		btnToCST.setFont(new Font("Dialog", Font.BOLD, 14));
		// btnToCST.addActionListener(Actions.Export);
		btnToCST.setMinimumSize(new Dimension(90, 25));
		btnToCST.setMaximumSize(new Dimension(90, 25));
		btnToCST.setForeground(Color.WHITE);
		btnToCST.setBackground(Color.DARK_GRAY);
		pnlbuttons.add(btnToCST);

		btnback = new JButton("Back");

		btnback.setFont(new Font("Dialog", Font.BOLD, 14));

		btnback.setMinimumSize(new Dimension(90, 25));
		btnback.setMaximumSize(new Dimension(90, 25));
		btnback.setForeground(Color.WHITE);
		btnback.setBackground(Color.DARK_GRAY);
		btnback.setAlignmentX(0.5f);
		pnlbuttons.add(btnback);

		cbbData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updatetable();
			}
		});
		btnback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackFrame.window.setpanel(MainMenu.frame(frame));
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				updatepicker();
			}
		});
		btnToCST.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Export();
			}
		});

		updatepicker();
		updatetable();

	}
}
