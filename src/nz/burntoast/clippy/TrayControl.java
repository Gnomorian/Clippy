package nz.burntoast.clippy;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TrayControl {
	static Image image = Toolkit.getDefaultToolkit().getImage("res/icon.png");
	
	static TrayIcon trayIcon;
	static PopupMenu popup;
	static MenuItem defaultItem;
	static Menu history;
	
	public TrayControl() throws Exception {
		popup = new PopupMenu();
		
		trayIcon = new TrayIcon(image, "Clippy", popup);
		if(SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			
			trayIcon.setImageAutoSize(true);
			
			createDefaultItems();
			createTextMenuItem(App.cc.getClipboardContents());
			try {
				tray.add(trayIcon);
			}catch(AWTException e) {
				System.err.println("Tray Icon couldnt be added");
			}
		}
	}
	public void createImageMenuItem(Transferable clipboardContents) {
		
	}
	public MenuItem createTextMenuItem(Transferable clipboardContents) {
		String name = "";
			try {
				name = (String)clipboardContents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
		CustomMenuItem item = new CustomMenuItem(name, clipboardContents);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				App.cc.setClipboardContents(((CustomMenuItem)arg0.getSource()).getData());
				trayIcon.setToolTip(((MenuItem)arg0.getSource()).getLabel());
			}
		});
		history.add(item);
		return item;
	}
	
	public void removeMenuItem(MenuItem item) {
		history.remove(item);
	}
	
	public void createDefaultItems() {
		history = new Menu("Recent Clipboards");
		popup.add(history);
		
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		popup.add(exit);
	}
	
	class CustomMenuItem extends MenuItem {
		private static final long serialVersionUID = 1L;
		private Transferable data;
		public CustomMenuItem(String name, Transferable data) {
			super(name);
			this.data = data;
		}
		public Transferable getData() {
			return data;
		}
	}
}