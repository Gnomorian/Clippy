package nz.burntoast.clippy;

public class App {
	// controls the Tray icon.
	public static TrayControl tc;
	// catches and deals with clipboard events.
	public static ClipboardControl cc;
	// contains settings from file
	// first number is a boolean of whether to launch at startup
	// second number is how many clips to save, default is 10
	public static int[] settings = {0, 10};
	// settings menu may check this often, saves unnessersary read/write
	public static boolean hasSettingsFile = false;
	
	public static void main(String[] args) {
	    try {
	    	cc = new ClipboardControl(10);
			tc = new TrayControl();
			cc.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void getSettingsFile() {
		//TODO: check if settings file exists, if it does update settings then update hasSettingsFile
	}
	/**
	 * saves to file the settings user wants
	 * @param startup start at startup or not
	 * @param saves how many clips to save
	 */
	public static void updateSettings(int startup, int saves) {
		//TODO: create a settings file
		if(!hasSettingsFile) {
			
		}
		//TODO: update settings in file
	}
}