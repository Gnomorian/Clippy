package nz.burntoast.clippy;

import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

public class ClipboardControl extends Thread implements ClipboardOwner {
	// sets how many recent strings it can hold
	int history;
	// contains the recent strings on the clipboard
	private ArrayList<MenuItem> stringHistory = new ArrayList<MenuItem>();
	// contains the recent images on the clipboard
	private ArrayList<MenuItem> imageHistory = new ArrayList<MenuItem>();
	
	Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	/**
	 * @param history sets the amount of clipboard history to store
	 * @param tc 
	 */
	public ClipboardControl(int history) {
		this.history = history;
	}
	
	public void run() {
	    Transferable trans = sysClip.getContents(this);
	    regainOwnership(trans);
	    System.out.println("Listening to board...");
	    while(true) {}
	}
	
	/**
	  * Place a String on the clipboard, and make this class the
	  * owner of the Clipboard's contents.
	  */
	  public void setClipboardContents(Transferable transferable) {
		  sysClip.setContents(transferable, this);
	  }

	  public Transferable getClipboardContents() {
	    Transferable contents = sysClip.getContents(null);
	    return contents;
	  }
	/** use this to find when something new */
	@Override
	public void lostOwnership(Clipboard board, Transferable clip) {
		try {
		    Thread.sleep(20);
		  } catch(Exception e) {
		    System.out.println("Exception: " + e);
		  }
		Transferable contents = sysClip.getContents(this);
		addNewSavedClip(contents);
		regainOwnership(contents);
		System.out.println("clipboard changed, adding new option");
	}
	
	/**
	 * works out if the contents of clipboard is image or text, then saves it to clipboard
	 * @param clipboardContents
	 */
	public void addNewSavedClip(Transferable clipboardContents) {
		boolean hasTransferableText = (clipboardContents != null) && clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor);
	    if (hasTransferableText) {
	      MenuItem clip = App.tc.createTextMenuItem(clipboardContents);
	      stringHistory.add(clip);
	      if(stringHistory.size() > 10) {
	    	  stringHistory.remove(0);
	      }
	    }
	}
	void regainOwnership(Transferable t) {
	    sysClip.setContents(t, this);
	  }
}
