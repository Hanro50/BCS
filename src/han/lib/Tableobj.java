package han.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Tableobj {
	public String[][] Table;
	final static String rchar = " ▣ ";

	public Tableobj(String Path, String name) throws FileNotFoundException {
		File file = new File(FileObj.Root + Path + name + ".csv");
		if (file.exists()) {
			try {
				String[] F = FileObj.read(file);
				Table = new String[F.length][];
				for (int i = 0; i < F.length; i++) {
					String line = "";
					if (F[i].contains("\"")) {
						String[] F2 = F[i].split("\"");

						line = "";
						for (int i2 = 0; i2 < F2.length; i2++) {

							if (i2 % 2 != 0)
								line = line + F2[i2].replaceAll("\\,", rchar);
							else
								line = line + F2[i2];
						}
					} else {
						line = F[i];
					}
					Table[i] = line.split(",");
					for (int i2 = 0; i2 < Table[i].length; i2++) {
						Table[i][i2] = Table[i][i2].replaceAll(rchar, "\\,");
					}
				}

			} catch (IOException e) {
				Debug.Trace(e);
			}
		} else
			throw new FileNotFoundException("Could not find file");
	}

	public void Debug_read() {
		for (int i = 0; i < Table.length; i++) {
			String line = "";
			for (int i2 = 0; i2 < Table[i].length; i2++) {
				line = line + "|" + Table[i][i2];
			}
			Debug.out(line);
		}
	}

}
