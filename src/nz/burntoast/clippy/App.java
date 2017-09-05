package nz.burntoast.clippy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class App {
	// controls the Tray icon.
	public static TrayControl tc;
	// catches and deals with clipboard events.
	public static ClipboardControl cc;
	// contains settings from file
	// first number is a boolean of whether to launch at startup
	// second number is how many clips to save, default is 10
	public static int[] settings = {0, 10};
	
	public static void main(String[] args) {
		getSettingsFile();
	    try {
	    	cc = new ClipboardControl(10);
			tc = new TrayControl();
			cc.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * check if a settings file exists, if it does read it and set the int[]
	 * settings to the correct settings
	 */
	private static void getSettingsFile() {
		File file =  new File("res/settings.ini");
		if(file.exists()) {
			try {
				FileReader fr = new FileReader(file);
				int character;
				String content = "";
				while((character = fr.read()) != -1) {
					content += (char)character;
				}
				settings[0] = Integer.valueOf(content.charAt(0));
				settings[1] = Integer.valueOf((content.substring(2, content.length()-1)).trim());
				fr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * saves to file the settings user wants
	 * @param startup start at startup or not
	 * @param saves how many clips to save
	 */
	public static void updateSettings(int startup, int saves) {
		settings[0] = startup;
		settings[1] = saves;
		
		File file = new File("res/settings.ini");
		try {
			PrintStream ps = new PrintStream(file);
			ps.println(settings[0]);
			ps.println(settings[1]);
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * will set the program to run at startup if true
	 * or remove its self from the startup list if false
	 * @param startup
	 */
	public static void runOnStartup(boolean startup) {
		//TODO: Add to startup options for known operating systems if possible
	}
}