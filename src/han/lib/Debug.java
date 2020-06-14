package han.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//Hanro Debug library 
public class Debug {
	static Debug DB;

	static public final String version = "1.8";
	
	
	static public boolean Debug = true;
	static public boolean Asciidebug = true;
	static private String Lastclass = "";
	static private ASCII_CODES HD = ASCII_CODES.RESET;
	static private final File LOG = FileObj.Fetch("", "Log", "txt");

	static {
		LOG.delete();
		System.setProperty("app.path", FileObj.Root);
		
	}

	public static void boot(String[] args) {
		if (args != null && args.length > 0) {
			for (String str : args) {
				String str0 = str.toLowerCase().trim();
				if (str0.contains("nocolor") || str0.contains("nocolour") || System.getProperty("os.name").contains("Window")) {
					Asciidebug = false;
					out("Turning off Ascii mode");
					break;
				}
			}
		}
		if (Asciidebug)
			out("If you see Ascii controll characters. Launch with the perameter \"nocolour\".");
	}

	private static boolean getAsciidebug() {
		return Asciidebug;
	}

	public static void Version() {
		out("Printing Debug Information:");
		System.setProperty("app.version", "Barcode scanner "+version);
		System.setProperty("app.date", (new Date()).toString());
		System.setProperty("app.author", "Hanro50");
		
		List<String> keys = new ArrayList<String>();
		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		keys.addAll(System.getProperties().stringPropertyNames());
		keys.sort(cmp);

		for (int i = 0; i < keys.size(); i++) {
			out("-> " + String.format("%-30s", keys.get(i)) + " : " + System.getProperty(keys.get(i)));
		}
		out("Continuing on with rest of application->");
	}

	// Custom version of System.err.print
	public static void err(String Line) {
		
		String out = Format(Line, ASCII_CODES.Bright_YELLOW, ASCII_CODES.Bright_WHITE, ASCII_CODES.Tab);
		if (Debug) {
			System.err.println(out);
		}
		writetolog(out);
	}

	// Custom version of System.out.print
	public static void out(String Line) {
		String out = Format(Line, ASCII_CODES.Bright_GREEN, ASCII_CODES.Bright_WHITE, ASCII_CODES.Tab);
		if (Debug) {
			System.out.println(out);
		}
		writetolog(out);
	}
	
	private static void writetolog(String Line) {
		String FL = "";
		try {
			if (!LOG.exists()) {
				if (!LOG.createNewFile()) {
					throw new IOException("Could not save Log");
				}
			}
			else {
				FL = FileObj.read(LOG, "\n");
			}
		
			FL = FL +Line.replaceAll("\033\\[.*?m", "");
			
			
			FileWriter writer = new FileWriter(LOG);

			writer.write(FL);
			writer.close();
			
			if (((double) LOG.length() / (1024)) > 50) {
				LOG.delete();
				err("Error Log File to big!");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Trace(e);
		} catch (StackOverflowError e) {
			System.exit(0);
			LOG.delete();
			err("Error Log File to big!");
			Trace(e);
		}
	}

	// Custom error tracer
	public static void Trace(Throwable e) {
		System.err.println(Caller(ASCII_CODES.Bright_RED) + ASCII_CODES.Bright_YELLOW + ASCII_CODES.Tab + "<"
				+ e.toString().replace(": ", "> [") + "] {" + ASCII_CODES.RESET);
		for (StackTraceElement STE : e.getStackTrace()) {
			System.err.println(ASCII_CODES.Tab.toString() + ASCII_CODES.Tab.toString() + ASCII_CODES.Bright_RED + "<E>"
					+ ASCII_CODES.Bright_WHITE + " " + STE.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(")
					+ ASCII_CODES.YELLOW + " " /*
												 * + (STE.getModuleVersion() ==
												 * null?"":"version of class: "+STE.getModuleVersion())
												 */ + ASCII_CODES.RESET);
		}
		System.err.println(ASCII_CODES.Bright_YELLOW + (ASCII_CODES.Tab + "}") + ASCII_CODES.RESET);
	}

	// Header writer
	private static String Caller(ASCII_CODES headerc) {
		String ls = "";
		CHK: {
			Exception e = new Exception();
			for (StackTraceElement STE : e.getStackTrace()) {
				if (STE.getClassName() != Debug.class.getName()) {
					ls = STE.getClassName();
					break CHK;
				}
			}
			ls = "UNKNOWN";
		}

		if (Lastclass.equals(ls) && (HD == headerc))
			return "";
		Lastclass = ls;
		HD = headerc;
		return headerc + "[" + Lastclass + "]" + ASCII_CODES.RESET + "\n";

	}

	// Formatter
	private static String Format(String Line, ASCII_CODES headerc, ASCII_CODES bodyc, ASCII_CODES indent) {
		return Caller(headerc) + bodyc + indent
				+ Line.trim().replaceAll("\n", ASCII_CODES.RESET + "\n" + bodyc + indent) + ASCII_CODES.RESET;
	}

	//Ascii color printing
	public static enum ASCII_CODES {
		//Normal colors|Bright versions       |Custom characters
		RESET(0),							   Tab("    "),
		BLACK(30)		,Bright_BLACK(30,1),
		RED(31)			,Bright_RED(31,1),
		GREEN(32)		,Bright_GREEN(32,1),
		YELLOW(33)		,Bright_YELLOW(33,1),
		BLUE(34)		,Bright_BLUE(34,1),
		PURPLE(35)		,Bright_PURPLE(35,1),
		CYAN(36)		,Bright_CYAN(36,1),
		WHITE(37)		,Bright_WHITE(37,1)
		;

		ASCII_CODES(String value) {
			Key2 = value;
			Key = value;
		}

		ASCII_CODES(int value) {
			Key = "\033[" + value + "m";
			Key2 = "";
		}

		ASCII_CODES(int value1, int value2) {
			Key = "\033[" + value1 + ";" + value2 + "m";
			Key2 = "";
		}

		// 2 Key values are needed as the non Ascii "Custom characters" need to be
		// accounted for
		final String Key2;
		final String Key;

		public String toString() {
			if (!getAsciidebug()) {
				return Key2;
			}
			return Key;
		};
	}
}
