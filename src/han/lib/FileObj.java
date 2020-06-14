package han.lib;

import static han.lib.Debug.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileObj {
	public static final String Root;
	public static final String ClassPath;

	static {
		String file = "";
		if (System.getProperty("os.name").contains("Window")) {
			file = System.getProperty("user.dir") + "/";
		} else {
			if (!System.getProperty("os.name").contains("Linux")) {
				out("Unsupported OS -> Assuming Linux config");
			}

			file = System.getProperty("java.class.path").split(System.getProperty("path.separator"))[0];

			String[] path = file.split("/");
			file = path[0];
			for (int i = 1; i < path.length - 1; i++) {
				file = file + "/" + path[i];
			}
		}
		Root = file + "/";
		ClassPath = Root + "Data/";
		FileChk("");
	}

	public static String SafeStr(String raw) {
		return raw.trim().replaceAll("\\.", "").replaceAll("[^a-zA-Z0-9\\._]+", "_").toLowerCase();
	}

	public static void FileChk(String Path) {
		FileChkroot(ClassPath + Path);
	}
	
	public static void FileChkroot(String Path) {
		try {
			File F = new File(Path);
			if (!F.exists())
				F.mkdir();
		} catch (Throwable e) {
			Trace(e);
		}
	}

	static public File Fetch(String Path, String Name, String extention) {
		Name = SafeStr(Name);
		extention = SafeStr(extention);
		return new File(ClassPath + Path + Name + "." + extention);
	}

	static public boolean erase(String Path, String Name, String extention) throws FileNotFoundException {
		File file = Fetch(Path, Name, extention);
		return erase(file);
	}

	static public boolean erase(File file) throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException();
		return file.delete();
	}

	static public String[] read(File file) throws FileNotFoundException, IOException {
		return read(file, " \u2029 ").split(" \u2029 ");
	}

	static public String read(File file, String Seperator) throws FileNotFoundException, IOException {
		if (file.exists() && file.canRead()) {
			String T = "";
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String T2 = buffReader.readLine();
			while (T2 != null) {
				T = T + T2 + Seperator;
				T2 = buffReader.readLine();
			}
			buffReader.close();
			return T;
		} else if (!file.exists()) {
			err("Cannot find File " + file.getPath());
			throw new FileNotFoundException("Cannot find File " + file.getPath());
		} else {
			System.err.print("File is not readable");
			throw new IOException("File is not readable");
		}
	}

	static public void write(String[] In, File file, String objType) throws IOException {
		write(In, file, objType, true);
	}

	static public void writeNF(String[] In, File file, String objType) throws IOException {
		write(In, file, objType, false);
	}

	static public void write(String[] In, File file, String objType, boolean writelock) throws IOException {
		
		if ((file.exists()) && (writelock)) {
			throw new IOException(objType + " already exists");
		}
		out("Writing: " + file.getPath() + '\n');

		if (!file.getParentFile().exists())
			file.getParentFile().mkdir();
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("Could not save " + objType);
			}
		}
		

		String T = "";
		for (int i = 0; i < In.length; i++) {
			T = T + In[i] + "\n";
		}


		FileWriter writer = new FileWriter(file);

		writer.write(T);
		writer.close();


	}

	static private Gson builder() {
		GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		return builder.create();
	}

	static public <T> T fromjson(String json, Class<T> classOfT) {
		T R = builder().fromJson(json, classOfT);
		return R;
	}

	static public String tojson(Object raw) throws IOException {
		String s = builder().toJson(raw);
		out(s.replace(",", ",\n") + "\n");
		return s;
	}

	static public File[] FileList(String path, String extention) {
		final String ext = "." + SafeStr(extention);
		File F = new File(ClassPath + path);
		FileFilter Ff = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(ext);
			}
		};

		return F.listFiles(Ff);
	}

	static public String[] FileListString(String path, String extention) throws FileNotFoundException {
		extention = SafeStr(extention);
		File[] FL = FileList(path, extention);
		if (FL == null || FL.length == 0) {
			throw new FileNotFoundException("No files in directory");
		}
		String[] R = new String[FL.length];

		for (int i = 0; i < FL.length; i++) {
			String T = FL[i].getName();
			int WithoutExtLen = T.length() - extention.length() - 1;
			R[i] = T.substring(0, WithoutExtLen);
		}
		return R;
	}

	static public String[] FileListString(String path) {

		File[] FL = FileList(path);
		if (FL == null || FL.length == 0) {
			return new String[] { "No files found" };
		}
		String[] R = new String[FL.length];

		for (int i = 0; i < FL.length; i++) {
			String T = FL[i].getName();

			R[i] = T;
		}
		return R;
	}

	static public File[] FileList(String path) {
		File F = new File(ClassPath + path);
		FileFilter Ff = new FileFilter() {
			public boolean accept(File f) {
				return true;
			}
		};

		return F.listFiles(Ff);
	}
}
