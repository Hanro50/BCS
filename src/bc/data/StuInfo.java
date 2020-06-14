package bc.data;

import java.io.FileNotFoundException;
import java.util.HashMap;

import han.lib.Debug;
import han.lib.Tableobj;

public class StuInfo {
	
	public static Tableobj datafile;
	public static HashMap<String, StuInfo> DB = new HashMap<String, StuInfo>();
	public static String[] header; 
	public String[] Data; 
	public final boolean Found;
	
	static {
		try {
			datafile = new Tableobj("","data");
		} catch (FileNotFoundException e) {
			Debug.Trace(e);
			
		}
		if (datafile.Table[0][0].equals("ID")) {
			header = datafile.Table[0];
			for (int i =1; i < datafile.Table.length; i++) {
				DB.put(datafile.Table[i][0], new StuInfo(datafile.Table[i]));
			}	
		}
	}
	StuInfo(String[] Data) {
		this.Data = Data;
		this.Found = true;
	}
	
	public StuInfo() {
		this.Found = false;
	}
	
	public String ID() {
		if (Found) {
			return Data[0];
		}
		else return "-1"; 
	}
	
	static public StuInfo getinfo(String ID) {
		if (DB.containsKey(ID))
			return DB.get(ID);
		else
			return new StuInfo();
	}
	
	public String toString() {
		if (Found) {
			String out = "";
			for (int i = 0; i < header.length; i++) {
				out = out + header[i] + ":" + Data[i] + "\n";
			}
			return out;
		}
		return ("No Data found");

	}
	
	
	
	
	
	
	
	
	/*
	public String ID;
	public String Name;
	public String SurName;
	public String Grade;
	public String cemis;

	final public boolean Found;

	StuInfo(String ID, String Name, String SurName, String Grade, String cemis) {
		this.ID = ID;
		this.Name = Name;
		this.SurName = SurName;
		this.Grade = Grade;
		this.cemis = cemis;
		Found = true;
	}

	public StuInfo() {
		this.ID = "null";
		this.Name = "null";
		this.SurName = "null";
		this.Grade = "null";
		this.cemis = "null";
		Found = false;
	}

	public String toString() {
		if (Found) {
			return ("ID:\t" + ID + "\n" + "Name:\t" + Name.trim() + "\n" + "Surname:\t" + SurName + "\n" + "Grade:\t"
					+ Grade + "\n" + "cemis:\t" + cemis);
		}
		return ("No Data found");

	}

	static HashMap<String, StuInfo> DB = new HashMap<String, StuInfo>();
	static String err = "";

	static public void read() {
		Tableobj Data = new Tableobj("","data");
		Data.Debug_read();
		
		
		File Importer = new File(FileObj.Root + "data.csv");
		DB.clear();
		try {
			Scanner scan = new Scanner(Importer);
			if (scan.hasNext()) {
				if (!scan.nextLine().contains("ID,cemis,name,grade"))
					err = "Incorrect database format";
				while (scan.hasNext()) {
					String line = scan.nextLine().replaceAll("\"", "");
					String[] dataparse = line.split(",");
					StuInfo inf = new StuInfo(dataparse[0], dataparse[3], dataparse[2],
							dataparse[4].split(" ")[dataparse[4].split(" ").length - 1], dataparse[1]);
					DB.put(inf.ID, inf);
				}
			} else
				err = "No Database file found";
			scan.close();
		} catch (FileNotFoundException e) {
			Debug.Trace(e);
		}

	}

	static public StuInfo getinfo(String ID) {
		if (DB.containsKey(ID))
			return DB.get(ID);
		else
			return new StuInfo();
	}
*/
}
