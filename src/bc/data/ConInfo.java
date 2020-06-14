package bc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.annotations.Expose;

import han.lib.Debug;
import han.lib.FileObj;

//ConfigInfo
public class ConInfo {
	private static File fself = FileObj.Fetch("", "Config", "json");
	private static ConInfo self;
	public static final String DBfile = "DB/";
	static {
		FileObj.FileChk(DBfile);
		try {
			String F = FileObj.read(fself, "");
			self = FileObj.fromjson(F, ConInfo.class);
		} catch (FileNotFoundException e) {
			self = new ConInfo();
			self.Save();
		} catch (IOException e) {
			Debug.Trace(e);
		}
	}

	public static File getFile() {
		if (self.filepath == null)
			setFilepath(FileObj.Root);
		File f = new File(self.filepath);
		if (!f.exists()) {
			setFilepath(System.getenv("user.home"));
			f = new File(self.filepath);
		}
		return f;
	}

	public static void setFilepath(String filepath) {
		self.filepath = filepath;
		self.Save();
	}

	public static String getTemp() {
		if (self.Temp == null) {
			self.Temp = "38C";
			self.Save();
		}
		return self.Temp;
	}

	private ConInfo() {
	}

	private void Save() {
		try {
			FileObj.writeNF(new String[] { FileObj.tojson(self) }, fself, "config");
		} catch (IOException e) {
			Debug.Trace(e);
		}
	}

	@Expose
	private String filepath = FileObj.Root;
	@Expose
	private String Temp = "38C";
}
