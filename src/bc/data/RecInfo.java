package bc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import han.lib.Debug;
import han.lib.FileObj;

public class RecInfo {
	@Expose
	String ID;
	@Expose
	boolean[] Questions;
	@Expose
	String Temp = "Below " + ConInfo.getTemp();

	static final String[] layout = { "scanned", "Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Temp" };

	static public void Save(RecInfo Data) {
		String Datefile = ConInfo.DBfile + bc.gui.ScanMode.editor.getTextField().getText() + "/";
		FileObj.FileChk(Datefile);
		try {
			String[] data = { FileObj.tojson(Data) };
			File file = FileObj.Fetch(Datefile, Data.ID, "rec");
			FileObj.writeNF(data, file, "Data");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Debug.Trace(e);
		}
	}

	static public RecInfo Read(String ID) {
		String Datefile = "DB/" + bc.gui.ReadMode.cbbData.getModel().getSelectedItem() + "/";
		try {
			// String data = {FileObj.tojson(Data)};
			File file = FileObj.Fetch(Datefile, ID, "rec");
			String temp = FileObj.read(file, "");
			return FileObj.fromjson(temp, RecInfo.class);
			// (data, file, "Data");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Debug.Trace(e);
			return null;
		}
	}

	static public Object[][] Read() {
		String Datefile = ConInfo.DBfile + bc.gui.ReadMode.cbbData.getModel().getSelectedItem() + "/";
		File[] F = FileObj.FileList(Datefile, "rec");
		// RecInfoObj[] data = new RecInfoObj[F.length];
		if (F == null) {
			Debug.err("Null pointer error reverted");
			return new Object[][] { header() };
		}

		String[][] ret = new String[StuInfo.DB.size() + 1][];
		List<String> keys = new ArrayList<String>();
		keys.addAll(StuInfo.DB.keySet());
		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		keys.sort(cmp);
		Map<String, File> files = new HashMap<String, File>();
		for (int i = 0; i < F.length; i++) {
			try {
				files.put(F[i].getName().split("\\.")[0], F[i]);
				// Debug.out(F[i].getName().split("\\.")[0]);
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {

			}
		}

		// String[] keys = StuInfo.DB.keySet();

		// Debug.out(" ");

		ret[0] = header();
		for (int i = 0; i < keys.size(); i++) {
			String line;
			try {
				String key = keys.get(i).toLowerCase();
				if (files.containsKey(key)) {
					line = FileObj.read(files.get(key), "");
					RecInfo temp = FileObj.fromjson(line, RecInfo.class);
					ret[i + 1] = temp.toObj(true);
				} else {

					ret[i + 1] = (new RecInfo(keys.get(i))).toObj(false);
				}

			} catch (FileNotFoundException e) {
				Debug.Trace(e);
				F[i].delete();
			} catch (IOException e) {

				// TODO Auto-generated catch block
				Debug.Trace(e);
			}
		}
		return ret;
	}

	public RecInfo(String ID, String Temp, boolean[] Questions) {
		this.ID = ID;
		this.Questions = Questions;
		this.Temp = Temp;
	}

	public RecInfo(String ID, boolean[] Questions) {
		this.ID = ID;
		this.Questions = Questions;
	}

	public RecInfo(String ID) {
		this.ID = ID;
		Questions = new boolean[6];
		for (int i = 0; i < Questions.length; i++) {
			Questions[i] = false;
		}
		this.Temp = "Not present";
	}

	public static String[] header() {
		String[] h = new String[StuInfo.header.length + layout.length];// {"scanned", "Q1", "Q2", "Q3", "Q4", "Q5",
																		// "Q6", "Temp"}
		for (int i = 0; i < StuInfo.header.length; i++) {
			h[i] = StuInfo.header[i];
		}
		for (int i = StuInfo.header.length; i < h.length; i++) {
			h[i] = layout[i - StuInfo.header.length];
		}
		return h;

	}

	public String[] toObj(boolean valid) {
		// Mostly here for reference
		// {"ID", "Name", "Surname", "cemes", "grade", "scanned", "Q1", "Q2", "Q3",
		// "Q4", "Q5", "Q6", "Temp"}

		StuInfo stuobj = StuInfo.getinfo(ID);
		String[] lst = new String[StuInfo.header.length + layout.length];

		int i = 0;
		for (; i < StuInfo.header.length; i++) {
			try {
				lst[i] = stuobj.Data[i];
			} catch (ArrayIndexOutOfBoundsException e) {
				lst[i] = "null";
			}
		}

		lst[i] = valid ? "Yes" : "No";
		//Debug.out(valid ? "Yes" : "No");
		int i2 = i;
		for (; i2 < i + Questions.length; i2++) {
			try {
				lst[i2 + 1] = Questions[i2 - i] ? "Yes" : "No";
			} catch (ArrayIndexOutOfBoundsException e) {
				lst[i2 + 1] = "null";
			}
		}
		lst[i2 + 1] = Temp;

		return lst;

	}

}
