package nz.burntoast.clippy;

// TODO: on first run, show should i run at startup button

public class App {
	public static TrayControl tc;
	public static ClipboardControl cc;
	public static void main(String[] args) {
	    try {
	    	cc = new ClipboardControl(10);
			tc = new TrayControl();
			cc.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}